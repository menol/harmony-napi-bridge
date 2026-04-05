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
        // 对于接口、抽象类和密封类，不生成 NAPI Wrapper
        if (module.isInterface || module.isAbstract || module.isSealed) return
        val packageName = "com.itime.harmony.napi.generated"
        val fileName = "${module.className}_NapiWrapper"

        val fileBuilder = FileSpec.builder(packageName, fileName)
            .addImport("kotlinx.cinterop", "alloc", "allocArray", "memScoped", "ptr", "value", "ExperimentalForeignApi", "convert", "refTo", "get")
            .addImport("platform.posix", "size_tVar")
            .addImport("napi", "napi_env", "napi_callback_info", "napi_value", "napi_valueVar", "napi_get_cb_info")
            .addImport("com.itime.harmony.napi.runtime.utils", "toNapiValue", "toKotlinDouble", "toKotlinInt", "toKotlinBoolean", "toKotlinString", "toKotlinStringList", "toKotlinStringStringMap", "toKotlinAny", "toKotlinAnyList", "toKotlinStringAnyMap", "toKotlinIntList", "toKotlinDoubleList", "toKotlinBooleanList", "toKotlinStringIntMap", "toKotlinStringDoubleMap", "toKotlinStringBooleanMap", "toKotlinObject", "toNapiObject", "toKotlinEnum", "toNapiString", "toNapiValueIntList", "toNapiValueDoubleList", "toNapiValueBooleanList", "toNapiValueStringIntMap", "toNapiValueStringDoubleMap", "toNapiValueStringBooleanMap", "toNapiValueAnyList", "toNapiValueStringAnyMap")
            .addImport(module.packageName, module.className)
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
                appendLine("        napi_get_cb_info(env, info, argc.ptr, argv, null, null)")
                appendLine()

                val args = mutableListOf<String>()
                params.forEachIndexed { index, param ->
                    val convertMethod = TypeMapper.getNapiToKotlinMethod(param.type)
                    appendLine("        val arg$index = argv[$index]!!.$convertMethod")
                    args.add("arg$index")
                }

                appendLine("        val result = ${module.className}.${func.functionName}(${args.joinToString(", ")})")
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