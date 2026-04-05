package com.itime.harmony.napi.ksp.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.itime.harmony.napi.ksp.mapper.TypeMapper
import com.itime.harmony.napi.ksp.models.HarmonyModuleModel
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import java.io.OutputStreamWriter

class KotlinWrapperGenerator(private val codeGenerator: CodeGenerator) {

    fun generate(module: HarmonyModuleModel) {
        // 对于接口和密封类，不生成 NAPI Wrapper
        if (module.isInterface || module.isSealed) return
        val packageName = "com.itime.harmony.napi.generated"
        val fileName = "${module.className}_NapiWrapper"

        val fileBuilder = FileSpec.builder(packageName, fileName)
            .addImport("kotlinx.cinterop", "alloc", "allocArray", "memScoped", "ptr", "value", "ExperimentalForeignApi", "convert", "refTo", "get", "COpaquePointer", "asStableRef", "staticCFunction", "StableRef")
            .addImport("platform.posix", "size_tVar")
            .addImport("napi", "napi_env", "napi_callback_info", "napi_value", "napi_valueVar", "napi_get_cb_info", "napi_wrap", "napi_get_value_external", "napi_typeof", "napi_valuetype")
            .addImport("com.itime.harmony.napi.runtime.utils", "toNapiValue", "toKotlinDouble", "toKotlinInt", "toKotlinBoolean", "toKotlinString", "toKotlinStringList", "toKotlinStringStringMap", "toKotlinAny", "toKotlinAnyList", "toKotlinStringAnyMap", "toKotlinIntList", "toKotlinDoubleList", "toKotlinBooleanList", "toKotlinStringIntMap", "toKotlinStringDoubleMap", "toKotlinStringBooleanMap", "toKotlinObject", "toNapiObject", "toKotlinEnum", "toNapiString", "toNapiValueIntList", "toNapiValueDoubleList", "toNapiValueBooleanList", "toNapiValueStringIntMap", "toNapiValueStringDoubleMap", "toNapiValueStringBooleanMap", "toNapiValueAnyList", "toNapiValueStringAnyMap", "unwrapKotlinObject")
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
                    val typeArgs = if (module.typeParameters.isNotEmpty()) {
                        "<${module.typeParameters.joinToString(", ") { "Any?" }}>"
                    } else ""
                    appendLine("        val thisVar = alloc<napi_valueVar>()")
                    appendLine("        napi_get_cb_info(env, info, argc.ptr, argv, thisVar.ptr, null)")
                    appendLine("        val instance = thisVar.value!!.unwrapKotlinObject<${module.className}$typeArgs>(env!!)")
                } else {
                    appendLine("        napi_get_cb_info(env, info, argc.ptr, argv, null, null)")
                }
                appendLine()

                val args = mutableListOf<String>()
                params.forEachIndexed { index, param ->
                    val convertMethod = TypeMapper.getNapiToKotlinMethod(param.type)
                    appendLine("        val arg$index = argv[$index]!!.$convertMethod")
                    args.add("arg$index")
                }

                if (!module.isObject && !func.isExtension) {
                    appendLine("        val result = instance.${func.functionName}(${args.joinToString(", ")})")
                } else if (func.isExtension) {
                    val receiverArg = args.first()
                    val otherArgs = args.drop(1)
                    appendLine("        val result = $receiverArg.${func.functionName}(${otherArgs.joinToString(", ")})")
                } else {
                    appendLine("        val result = ${module.className}.${func.functionName}(${args.joinToString(", ")})")
                }
                val returnMethod = TypeMapper.getKotlinToNapiMethod(func.returnType)
                if (returnMethod.isNotEmpty()) {
                    appendLine("        result.$returnMethod")
                } else {
                    appendLine("        null")
                }
                appendLine("    }")
                appendLine("} catch (e: Throwable) {")
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
                    appendLine("        throw IllegalStateException(\"Cannot instantiate abstract class ${module.className} from JS\")")
                } else {
                    appendLine("        // Called from JS 'new'")
                    val args = mutableListOf<String>()
                    module.primaryConstructorParams.forEachIndexed { index, param ->
                        val convertMethod = TypeMapper.getNapiToKotlinMethod(param.type)
                        appendLine("        val arg$index = argv[$index]!!.$convertMethod")
                        args.add("arg$index")
                    }
                    val typeArgs = if (module.typeParameters.isNotEmpty()) {
                        "<${module.typeParameters.joinToString(", ") { "Any?" }}>"
                    } else ""
                    appendLine("        val instance = ${module.className}$typeArgs(${args.joinToString(", ")})")
                    appendLine("        val stableRef = StableRef.create(instance)")
                    appendLine("        napi_wrap(env, thisVar.value, stableRef.asCPointer(), staticCFunction(::${module.className}_finalize), null, null)")
                    appendLine("        thisVar.value")
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
}