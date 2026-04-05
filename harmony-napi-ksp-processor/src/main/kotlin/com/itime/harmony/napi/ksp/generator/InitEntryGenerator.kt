package com.itime.harmony.napi.ksp.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.itime.harmony.napi.ksp.models.HarmonyModuleModel
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import java.io.OutputStreamWriter

class InitEntryGenerator(private val codeGenerator: CodeGenerator) {

    fun generate(modules: List<HarmonyModuleModel>) {
        if (modules.isEmpty()) return

        val packageName = "com.itime.harmony.napi.generated"
        val fileName = "InitGeneratedWrappers"

        val fileBuilder = FileSpec.builder(packageName, fileName)
            .addImport("kotlinx.cinterop", "alloc", "memScoped", "ptr", "value", "ExperimentalForeignApi", "staticCFunction", "convert", "reinterpret", "cstr")
            .addImport("napi", "napi_env", "napi_value", "napi_valueVar", "napi_property_descriptor", "napi_define_properties", "napi_create_string_utf8", "napi_set_named_property", "napi_create_object")
            .addAnnotation(AnnotationSpec.builder(ClassName("kotlin", "OptIn"))
                .addMember("kotlinx.cinterop.ExperimentalForeignApi::class")
                .addMember("kotlin.experimental.ExperimentalNativeApi::class")
                .build())

        val funBuilder = FunSpec.builder("InitGeneratedWrappers")
            .addAnnotation(AnnotationSpec.builder(ClassName("kotlin.native", "CName"))
                .addMember("%S", "InitGeneratedWrappers")
                .build())
            .addParameter("env", ClassName("napi", "napi_env").copy(nullable = true))
            .addParameter("exports", ClassName("napi", "napi_value").copy(nullable = true))
            .returns(ClassName("napi", "napi_value").copy(nullable = true))

        val codeBlock = buildString {
            appendLine("memScoped {")
            modules.forEach { module ->
                appendLine("    // Register ${module.className} (${module.moduleName})")
                appendLine("    val ${module.className}_obj = alloc<napi_valueVar>()")
                appendLine("    napi_create_object(env, ${module.className}_obj.ptr)")
                appendLine()
                
                module.exportFunctions.forEach { func ->
                    val wrapperFuncName = "${module.className}_${func.functionName}_wrapper"
                    
                    appendLine("    val desc_${func.functionName} = alloc<napi_property_descriptor>()")
                    appendLine("    desc_${func.functionName}.utf8name = \"${func.functionName}\".cstr.ptr")
                    appendLine("    desc_${func.functionName}.method = staticCFunction(::$wrapperFuncName)")
                    appendLine("    desc_${func.functionName}.attributes = 0u.convert() // napi_default")
                    appendLine("    napi_define_properties(env, ${module.className}_obj.value, 1u, desc_${func.functionName}.ptr)")
                    appendLine()
                }
                
                appendLine("    napi_set_named_property(env, exports, \"${module.moduleName}\", ${module.className}_obj.value)")
            }
            appendLine("}")
            appendLine("return exports")
        }

        funBuilder.addCode(codeBlock)
        fileBuilder.addFunction(funBuilder.build())

        val fileSpec = fileBuilder.build()
        val dependencies = Dependencies(false, *modules.mapNotNull { it.containingFile }.toTypedArray())
        val file = codeGenerator.createNewFile(dependencies, packageName, fileName)
        OutputStreamWriter(file).use { writer ->
            fileSpec.writeTo(writer)
        }
    }
}