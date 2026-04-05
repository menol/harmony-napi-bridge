package com.itime.harmony.napi.ksp.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.itime.harmony.napi.ksp.mapper.TypeMapper
import com.itime.harmony.napi.ksp.models.HarmonyModuleModel
import com.itime.harmony.napi.ksp.models.HarmonyTypeModel
import java.io.OutputStreamWriter

class TypeScriptGenerator(private val codeGenerator: CodeGenerator) {

    fun generate(modules: List<HarmonyModuleModel>) {
        if (modules.isEmpty()) return

        val tsContent = buildString {
            val allTypes = mutableSetOf<HarmonyTypeModel>()
            fun collectTypes(type: HarmonyTypeModel) {
                if (allTypes.add(type)) {
                    type.arguments.forEach { collectTypes(it) }
                    type.properties.forEach { collectTypes(it.type) }
                    type.sealedSubclasses.forEach { collectTypes(it) }
                }
            }

            modules.forEach { module ->
                module.exportFunctions.forEach { func ->
                    func.parameters.forEach { collectTypes(it.type) }
                    collectTypes(func.returnType)
                }
            }

            allTypes.filter { it.isEnum }.forEach { type ->
                appendLine("export type ${type.simpleName} = ${type.enumValues.joinToString(" | ") { "\"$it\"" }};")
                appendLine()
            }

            val serializableTypes = allTypes.filter { it.isSerializable }

            serializableTypes.filter { it.isSealed }.forEach { type ->
                val typeParams = if (type.typeParameters.isNotEmpty()) {
                    "<${type.typeParameters.joinToString(", ")}>"
                } else ""
                
                val subclassesStr = if (type.sealedSubclasses.isNotEmpty()) {
                    type.sealedSubclasses.joinToString(" | ") { sub ->
                        val subTypeParams = if (sub.typeParameters.isNotEmpty()) {
                            "<${sub.typeParameters.joinToString(", ")}>"
                        } else ""
                        "${sub.simpleName}$subTypeParams"
                    }
                } else {
                    "never"
                }
                
                appendLine("export type ${type.simpleName}$typeParams = $subclassesStr;")
                appendLine()
            }

            serializableTypes.filter { !it.isSealed }.forEach { type ->
                val typeParams = if (type.typeParameters.isNotEmpty()) {
                    "<${type.typeParameters.joinToString(", ")}>"
                } else ""
                
                appendLine("export interface ${type.simpleName}$typeParams {")
                
                // 查找当前类是否为某个 sealed class 的子类
                val parentSealed = allTypes.find { parent ->
                    parent.isSealed && parent.sealedSubclasses.any { sub -> sub.qualifiedName == type.qualifiedName }
                }
                if (parentSealed != null) {
                    val discriminator = type.serialName ?: type.qualifiedName
                    appendLine("    type: \"$discriminator\";")
                }
                
                type.properties.forEach { prop ->
                    appendLine("    ${prop.name}: ${TypeMapper.getTsType(prop.type)};")
                }
                appendLine("}")
                appendLine()
            }

            modules.forEach { module ->
                if (module.isInterface || module.isAbstract || module.isSealed) {
                    val typeParams = if (module.typeParameters.isNotEmpty()) {
                        "<${module.typeParameters.joinToString(", ")}>"
                    } else ""
                    appendLine("export interface ${module.moduleName}$typeParams {")
                    
                    val funcsToExport = if (module.isAbstract && !module.isInterface) {
                        module.exportFunctions.filter { it.isAbstract }
                    } else {
                        module.exportFunctions
                    }
                    
                    funcsToExport.forEach { func ->
                        val params = func.parameters.joinToString(", ") { param ->
                            val tsType = TypeMapper.getTsType(param.type)
                            "${param.name}: $tsType"
                        }
                        val tsReturnType = TypeMapper.getTsType(func.returnType)
                        appendLine("    ${func.functionName}($params): $tsReturnType;")
                    }
                    appendLine("}")
                } else {
                    appendLine("export declare namespace ${module.moduleName} {")
                    module.exportFunctions.forEach { func ->
                        val params = func.parameters.joinToString(", ") { param ->
                            val tsType = TypeMapper.getTsType(param.type)
                            "${param.name}: $tsType"
                        }
                        val tsReturnType = TypeMapper.getTsType(func.returnType)
                        appendLine("    function ${func.functionName}($params): $tsReturnType;")
                    }
                    appendLine("}")
                }
            }
        }

        val dependencies = Dependencies(false, *modules.mapNotNull { it.containingFile }.toTypedArray())
        val file = codeGenerator.createNewFileByPath(dependencies, "ts/Index", "d.ts")
        OutputStreamWriter(file).use { writer ->
            writer.write(tsContent)
        }
    }
}