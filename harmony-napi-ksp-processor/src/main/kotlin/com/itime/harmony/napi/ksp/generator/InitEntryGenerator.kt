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
        val validModules = modules.filter { !it.isInterface && !it.isSealed && !it.isData }
        if (validModules.isEmpty()) return

        val packageName = "com.itime.harmony.napi.generated"
        val fileName = "InitGeneratedWrappers"

        val fileBuilder = FileSpec.builder(packageName, fileName)
            .addImport("kotlinx.cinterop", "alloc", "allocArray", "memScoped", "ptr", "value", "ExperimentalForeignApi", "staticCFunction", "convert", "reinterpret", "cstr", "get")
            .addImport("napi", "napi_env", "napi_value", "napi_valueVar", "napi_property_descriptor", "napi_define_properties", "napi_create_string_utf8", "napi_set_named_property", "napi_create_object", "napi_define_class", "napi_create_reference", "napi_refVar", "napi_set_property", "napi_create_function", "NAPI_AUTO_LENGTH")
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
            validModules.forEach { module ->
                if (module.isFileExtension) {
                    appendLine("    val obj_${module.className} = alloc<napi_valueVar>()")
                    appendLine("    napi_create_object(env, obj_${module.className}.ptr)")
                    module.exportFunctions.forEach { func ->
                        val wrapperFuncName = "${module.className}_${func.functionName}_wrapper"
                        appendLine("    val str_${module.className}_${func.functionName} = alloc<napi_valueVar>()")
                        appendLine("    napi_create_string_utf8(env, \"${func.functionName}\", NAPI_AUTO_LENGTH.convert(), str_${module.className}_${func.functionName}.ptr)")
                        appendLine("    val fn_${module.className}_${func.functionName} = alloc<napi_valueVar>()")
                        appendLine("    napi_create_function(env, \"${func.functionName}\", NAPI_AUTO_LENGTH.convert(), staticCFunction(::$wrapperFuncName), null, fn_${module.className}_${func.functionName}.ptr)")
                        appendLine("    napi_set_property(env, obj_${module.className}.value, str_${module.className}_${func.functionName}.value, fn_${module.className}_${func.functionName}.value)")
                    }
                    appendLine("    val str_export_${module.className} = alloc<napi_valueVar>()")
                    appendLine("    napi_create_string_utf8(env, \"${module.moduleName}\", NAPI_AUTO_LENGTH.convert(), str_export_${module.className}.ptr)")
                    appendLine("    napi_set_property(env, exports, str_export_${module.className}.value, obj_${module.className}.value)")
                    appendLine()
                    return@forEach
                }

                if (!module.isObject) {
                    appendLine("    // Register Class ${module.className} (${module.moduleName})")
                    appendLine("    val ${module.className}_constructorVar = alloc<napi_valueVar>()")
                    if (module.exportFunctions.isNotEmpty()) {
                        appendLine("    val ${module.className}_descArray = allocArray<napi_property_descriptor>(${module.exportFunctions.size})")
                        module.exportFunctions.forEachIndexed { index, func ->
                            val wrapperFuncName = "${module.className}_${func.functionName}_wrapper"
                            appendLine("    ${module.className}_descArray[$index].utf8name = \"${func.functionName}\".cstr.ptr")
                            appendLine("    ${module.className}_descArray[$index].method = staticCFunction(::$wrapperFuncName)")
                            appendLine("    ${module.className}_descArray[$index].attributes = 0u.convert() // napi_default")
                        }
                        appendLine("    napi_define_class(env, \"${module.moduleName}\", napi.NAPI_AUTO_LENGTH.convert(), staticCFunction(::${module.className}_constructor), null, ${module.exportFunctions.size}u.convert(), ${module.className}_descArray, ${module.className}_constructorVar.ptr)")
                    } else {
                        appendLine("    napi_define_class(env, \"${module.moduleName}\", napi.NAPI_AUTO_LENGTH.convert(), staticCFunction(::${module.className}_constructor), null, 0u.convert(), null, ${module.className}_constructorVar.ptr)")
                    }
                    appendLine("    val ${module.className}_refVar = alloc<napi_refVar>()")
                    appendLine("    napi_create_reference(env, ${module.className}_constructorVar.value, 1u.convert(), ${module.className}_refVar.ptr)")
                    appendLine("    com.itime.harmony.napi.runtime.utils.ConstructorRegistry.refs[\"${module.className}\"] = ${module.className}_refVar.value!!")
                    appendLine("    napi_set_named_property(env, exports, \"${module.moduleName}\", ${module.className}_constructorVar.value)")
                } else {
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
            }
            appendLine("}")
            appendLine("return exports")
        }

        funBuilder.addCode(codeBlock)
        fileBuilder.addFunction(funBuilder.build())

        val fileSpec = fileBuilder.build()
        val dependencies = Dependencies(false, *validModules.mapNotNull { it.containingFile }.toTypedArray())
        val file = codeGenerator.createNewFile(dependencies, packageName, fileName)
        OutputStreamWriter(file).use { writer ->
            fileSpec.writeTo(writer)
        }
    }
}