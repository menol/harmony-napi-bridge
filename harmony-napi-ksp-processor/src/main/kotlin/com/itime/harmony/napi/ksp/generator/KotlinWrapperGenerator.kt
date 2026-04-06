package com.itime.harmony.napi.ksp.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.itime.harmony.napi.ksp.mapper.TypeMapper
import com.itime.harmony.napi.ksp.models.HarmonyModuleModel
import com.itime.harmony.napi.ksp.models.HarmonyTypeModel
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import java.io.OutputStreamWriter

class KotlinWrapperGenerator(private val codeGenerator: CodeGenerator) {

    fun generate(module: HarmonyModuleModel) {
        if (module.isInterface) {
            generateInterfaceProxy(module)
            return
        }
        // 对于密封类，不生成 NAPI Wrapper
        if (module.isSealed || module.isData) return
        val packageName = "com.itime.harmony.napi.generated"
        val fileName = "${module.className}_NapiWrapper"

        val fileBuilder = FileSpec.builder(packageName, fileName)
            .addImport("kotlinx.cinterop", "alloc", "allocArray", "memScoped", "ptr", "value", "ExperimentalForeignApi", "convert", "refTo", "get", "COpaquePointer", "asStableRef", "staticCFunction", "StableRef")
            .addImport("platform.posix", "size_tVar")
            .addImport("napi", "napi_env", "napi_callback_info", "napi_value", "napi_valueVar", "napi_get_cb_info", "napi_wrap", "napi_get_value_external", "napi_typeof", "napi_valuetype", "napi_get_null")
            .addImport("com.itime.harmony.napi.runtime.utils", "toNapiValue", "toKotlinDouble", "toKotlinInt", "toKotlinBoolean", "toKotlinString", "toKotlinStringList", "toKotlinStringStringMap", "toKotlinAny", "toKotlinAnyList", "toKotlinStringAnyMap", "toKotlinIntList", "toKotlinDoubleList", "toKotlinBooleanList", "toKotlinStringIntMap", "toKotlinStringDoubleMap", "toKotlinStringBooleanMap", "toKotlinObject", "toNapiObject", "toKotlinEnum", "toNapiString", "toNapiValueIntList", "toNapiValueDoubleList", "toNapiValueBooleanList", "toNapiValueStringIntMap", "toNapiValueStringDoubleMap", "toNapiValueStringBooleanMap", "toNapiValueAnyList", "toNapiValueStringAnyMap", "unwrapKotlinObject", "toNapiWrappedObject", "launchNapiCoroutine", "toKotlinEnumList", "toNapiValueEnumList", "toKotlinObjectList", "toNapiValueObjectList", "toKotlinStringObjectMap", "toNapiValueStringObjectMap", "toKotlinStringEnumMap", "toNapiValueStringEnumMap")
            .apply {
                if (module.packageName.isNotEmpty() && !module.isFileExtension) {
                    addImport(module.packageName, module.className)
                }
                if (module.isFileExtension) {
                    module.exportFunctions.forEach { func ->
                        if (func.isExtension) {
                            addImport(module.packageName, func.functionName)
                        }
                    }
                }
            }
            .addAnnotation(AnnotationSpec.builder(ClassName("kotlin", "OptIn"))
                .addMember("kotlinx.cinterop.ExperimentalForeignApi::class")
                .build())

        module.exportFunctions.forEach { func ->
            val wrapperFuncName = "${module.className}_${func.functionName}_wrapper"
            val params = func.parameters
            val paramCount = params.size

            val funBuilder = FunSpec.builder(wrapperFuncName)
                .addParameter("env", ClassName("napi", "napi_env").copy(nullable = true))
                .addParameter("info", ClassName("napi", "napi_callback_info").copy(nullable = true))
                .returns(ClassName("napi", "napi_value").copy(nullable = true))

            val codeBlock = buildString {
                appendLine("return try {")
                appendLine("    memScoped {")
                appendLine("        val argc = alloc<size_tVar>()")
                appendLine("        argc.value = ${paramCount}u")
                appendLine("        val argv = allocArray<napi_valueVar>($paramCount)")
                
                if (!module.isObject && !func.isExtension) {
                    val typeArgs = getDynamicTypeArgs(module)
                    appendLine("        val thisVar = alloc<napi_valueVar>()")
                    appendLine("        napi_get_cb_info(env, info, argc.ptr, argv, thisVar.ptr, null)")
                    appendLine("        val instance = thisVar.value!!.unwrapKotlinObject<${module.className}$typeArgs>(env!!)!!")
                } else {
                    appendLine("        napi_get_cb_info(env, info, argc.ptr, argv, null, null)")
                }
                appendLine()

                val args = mutableListOf<String>()
                params.forEachIndexed { index, param ->
                    appendLine("        val arg$index = ${buildNapiToKotlinExpression("argv[$index]!!", param.type)}")
                    args.add("arg$index")
                }

                val invokeLine = if (!module.isObject && !func.isExtension) {
                    "instance.${func.functionName}(${args.joinToString(", ")})"
                } else if (func.isExtension) {
                    if (args.isNotEmpty() && params.first().name == "receiver") {
                        val receiverArg = args.first()
                        val otherArgs = args.drop(1)
                        "$receiverArg.${func.functionName}(${otherArgs.joinToString(", ")})"
                    } else {
                        "${func.functionName}(${args.joinToString(", ")})"
                    }
                } else {
                    "${module.className}.${func.functionName}(${args.joinToString(", ")})"
                }
                
                val returnMethod = TypeMapper.getKotlinToNapiMethod(func.returnType)
                
                if (func.isSuspend) {
                    appendLine("        launchNapiCoroutine(env!!) {")
                    appendLine("            val result = $invokeLine")
                    if (returnMethod.isNotEmpty()) {
                        appendLine("            return@launchNapiCoroutine { cbEnv -> result.${returnMethod.replace("env!!", "cbEnv")} }")
                    } else {
                        appendLine("            return@launchNapiCoroutine { cbEnv -> memScoped { val nullVal = alloc<napi_valueVar>(); napi_get_null(cbEnv, nullVal.ptr); nullVal.value!! } }")
                    }
                    appendLine("        }")
                } else {
                    appendLine("        val result = $invokeLine")
                    if (returnMethod.isNotEmpty()) {
                        appendLine("        result.$returnMethod")
                    } else {
                        appendLine("        null")
                    }
                }
                appendLine("    }")
                appendLine("} catch (e: Throwable) {")
            appendLine("    println(\"Error in ${wrapperFuncName}: \${e.message}\")")
            appendLine("    e.printStackTrace()")
            appendLine("    napi.napi_throw_error(env, null, e.message ?: \"Unknown Kotlin exception\")")
            appendLine("    null")
            appendLine("}")
            }
            funBuilder.addCode(codeBlock)
            fileBuilder.addFunction(funBuilder.build())
        }

        if (!module.isObject && !module.isFileExtension) {
            val finalizeBuilder = FunSpec.builder("${module.className}_finalize")
                .addParameter("env", ClassName("napi", "napi_env").copy(nullable = true))
                .addParameter("data", ClassName("kotlinx.cinterop", "COpaquePointer").copy(nullable = true))
                .addParameter("hint", ClassName("kotlinx.cinterop", "COpaquePointer").copy(nullable = true))
                .returns(Unit::class)
                .addCode("data?.asStableRef<Any>()?.dispose()\n")
            fileBuilder.addFunction(finalizeBuilder.build())

            val constructorBuilder = FunSpec.builder("${module.className}_constructor")
                .addParameter("env", ClassName("napi", "napi_env").copy(nullable = true))
                .addParameter("info", ClassName("napi", "napi_callback_info").copy(nullable = true))
                .returns(ClassName("napi", "napi_value").copy(nullable = true))

            val paramCount = module.primaryConstructorParams.size
            val constructorCode = buildString {
                appendLine("return try {")
                appendLine("    memScoped {")
                appendLine("        val argc = alloc<size_tVar>()")
                appendLine("        argc.value = ${paramCount.coerceAtLeast(1)}u")
                appendLine("        val argv = allocArray<napi_valueVar>(${paramCount.coerceAtLeast(1)})")
                appendLine("        val thisVar = alloc<napi_valueVar>()")
                appendLine("        napi_get_cb_info(env, info, argc.ptr, argv, thisVar.ptr, null)")
                appendLine()
                appendLine("        val typeVar = alloc<napi_valuetype.Var>()")
                appendLine("        if (argc.value > 0u) {")
                appendLine("            napi_typeof(env, argv[0], typeVar.ptr)")
                appendLine("        }")
                appendLine()
                appendLine("        if (argc.value > 0u && typeVar.value == napi.napi_valuetype.napi_external) {")
                appendLine("            // Called from Kotlin toNapiWrappedObject")
                appendLine("            val externalPtr = alloc<kotlinx.cinterop.COpaquePointerVar>()")
                appendLine("            napi_get_value_external(env, argv[0], externalPtr.ptr)")
                appendLine("            napi_wrap(env, thisVar.value, externalPtr.value, staticCFunction(::${module.className}_finalize), null, null)")
                appendLine("            return@memScoped thisVar.value")
                appendLine("        }")
                appendLine()
                if (module.isAbstract) {
                    appendLine("        // For abstract classes instantiated from JS via inheritance,")
                    appendLine("        // we don't have the Kotlin instance yet.")
                    appendLine("        // We just return thisVar.value to let JS prototype chain initialize.")
                    appendLine("        // The actual Kotlin method overrides must be manually registered if called.")
                    appendLine("        return@memScoped thisVar.value")
                } else {
                    appendLine("        // Called from JS 'new'")
                    val args = mutableListOf<String>()
                    module.primaryConstructorParams.forEachIndexed { index, param ->
                        appendLine("        val arg$index = ${buildNapiToKotlinExpression("argv[$index]!!", param.type)}")
                        args.add("arg$index")
                    }
                    appendLine("        val instance = ${module.className}(${args.joinToString(", ")})")
                    appendLine("        val stableRef = StableRef.create(instance)")
                    appendLine("        napi_wrap(env, thisVar.value, stableRef.asCPointer(), staticCFunction(::${module.className}_finalize), null, null)")
                    appendLine("        return@memScoped thisVar.value")
                }
                appendLine("    }")
                appendLine("} catch (e: Throwable) {")
                appendLine("    napi.napi_throw_error(env, null, e.message ?: \"Unknown Kotlin exception\")")
                appendLine("    null")
                appendLine("}")
            }
            constructorBuilder.addCode(constructorCode)
            fileBuilder.addFunction(constructorBuilder.build())
        }

        val fileSpec = fileBuilder.build()
        
        // Ensure containingFile is non-null for Dependencies
        val dependencyFile = module.containingFile 
            ?: throw IllegalStateException("Containing file for module ${module.className} is null")
            
        val dependencies = Dependencies(false, dependencyFile)
        val file = codeGenerator.createNewFile(dependencies, packageName, fileName)
        OutputStreamWriter(file).use { writer ->
            fileSpec.writeTo(writer)
        }
    }

    private fun generateInterfaceProxy(module: HarmonyModuleModel) {
        val packageName = "com.itime.harmony.napi.generated"
        val fileName = "${module.className}_NapiProxy"

        val fileContent = buildString {
            appendLine("@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)")
            appendLine()
            appendLine("package $packageName")
            appendLine()
            appendLine("import kotlinx.cinterop.*")
            appendLine("import platform.posix.size_tVar")
            appendLine("import napi.*")
            appendLine("import com.itime.harmony.napi.runtime.utils.*")
            if (module.packageName.isNotEmpty()) {
                appendLine("import ${module.packageName}.${module.className}")
            }
            appendLine()

            val typeParams = if (module.typeParameters.isNotEmpty()) {
                "<${module.typeParameters.joinToString(", ")}>"
            } else ""

            appendLine("class ${module.className}_NapiProxy$typeParams(")
            appendLine("    private val env: napi_env,")
            appendLine("    jsObj: napi_value")
            appendLine(") : ${module.className}$typeParams {")
            appendLine()
            appendLine("    private val jsObjRef: napi_ref = memScoped {")
            appendLine("        val refVar = alloc<napi_refVar>()")
            appendLine("        napi_create_reference(env, jsObj, 1u, refVar.ptr)")
            appendLine("        refVar.value!!")
            appendLine("    }")
            appendLine()

            module.exportFunctions.forEach { func ->
                val returnTypeStr = TypeMapper.getKotlinTypeString(func.returnType)
                val paramsStr = func.parameters.joinToString(", ") { "${it.name}: ${TypeMapper.getKotlinTypeString(it.type)}" }
                val suspendModifier = if (func.isSuspend) "suspend " else ""
                
                appendLine("    override ${suspendModifier}fun ${func.functionName}($paramsStr): $returnTypeStr {")
                appendLine("        return com.itime.harmony.napi.runtime.utils.runOnMainThread(env, jsObjRef, \"${func.functionName}\") { jsObjValue, funcVar ->")
                appendLine("            memScoped {")
                val paramCount = func.parameters.size
                if (paramCount > 0) {
                    appendLine("                val argv = allocArray<napi_valueVar>($paramCount)")
                    func.parameters.forEachIndexed { index, param ->
                        val toNapiMethod = TypeMapper.getKotlinToNapiMethod(param.type)
                        appendLine("                argv[$index] = ${param.name}.$toNapiMethod")
                    }
                    appendLine("                val resultVar = alloc<napi_valueVar>()")
                    appendLine("                napi_call_function(env, jsObjValue, funcVar, ${paramCount}u.convert(), argv, resultVar.ptr)")
                    if (func.returnType.simpleName != "Unit") {
                        appendLine("                ${buildNapiToKotlinExpression("resultVar.value!!", func.returnType)}")
                    }
                } else {
                    appendLine("                val resultVar = alloc<napi_valueVar>()")
                    appendLine("                napi_call_function(env, jsObjValue, funcVar, 0u.convert(), null, resultVar.ptr)")
                    if (func.returnType.simpleName != "Unit") {
                        appendLine("                ${buildNapiToKotlinExpression("resultVar.value!!", func.returnType)}")
                    }
                }
                appendLine("            }")
                appendLine("        }")
                appendLine("    }")
                appendLine()
            }
            appendLine("}")
        }

        val dependencyFile = module.containingFile ?: throw IllegalStateException("Containing file for module ${module.className} is null")
        val dependencies = Dependencies(false, dependencyFile)
        val file = codeGenerator.createNewFile(dependencies, packageName, fileName)
        OutputStreamWriter(file).use { writer ->
            writer.write(fileContent)
        }
    }

    private fun buildNapiToKotlinExpression(source: String, type: HarmonyTypeModel): String {
        val expression = "$source.${TypeMapper.getNapiToKotlinMethod(type)}"
        return if (!type.isNullable && type.simpleName != "Unit") "$expression!!" else expression
    }

    private fun getDynamicTypeArgs(module: HarmonyModuleModel): String {
        if (module.typeParameters.isEmpty()) return ""
        // Generated wrappers invoke generic members through a runtime-erased bridge.
        // Using Any? keeps unbounded generic exports callable from generated code.
        return "<${module.typeParameters.joinToString(", ") { "Any?" }}>"
    }
}
