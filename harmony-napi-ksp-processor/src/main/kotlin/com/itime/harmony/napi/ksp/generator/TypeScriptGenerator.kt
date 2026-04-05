package com.itime.harmony.napi.ksp.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.itime.harmony.napi.ksp.mapper.TypeMapper
import com.itime.harmony.napi.ksp.models.HarmonyModuleModel
import com.itime.harmony.napi.ksp.models.HarmonyTypeModel
import java.io.OutputStreamWriter

class TypeScriptGenerator(
    private val codeGenerator: CodeGenerator,
    private val options: Map<String, String> = emptyMap()
) {

    fun generate(modules: List<HarmonyModuleModel>) {
        if (modules.isEmpty()) return

        val libName = options["harmony.napi.libName"] ?: "libkhn.so"

        val tsContent = buildString {
            val allTypes = mutableSetOf<HarmonyTypeModel>()
            fun collectTypes(type: HarmonyTypeModel) {
                if (allTypes.add(type)) {
                    type.arguments.forEach { collectTypes(it) }
                    type.properties.forEach { collectTypes(it.type) }
                    type.sealedSubclasses.forEach { collectTypes(it) }
                }
            }

            fun getTsType(typeModel: HarmonyTypeModel): String {
                if (typeModel.isTypeParameter) return typeModel.simpleName
                val matchedModule = modules.find { it.className == typeModel.simpleName }
                if (typeModel.isSerializable || typeModel.isEnum || typeModel.isSealed || matchedModule != null) {
                    val typeArgs = if (typeModel.arguments.isNotEmpty()) {
                        "<${typeModel.arguments.joinToString(", ") { getTsType(it) }}>"
                    } else ""
                    
                    val parentSealedModel = allTypes.find { parent ->
                        parent.isSealed && parent.sealedSubclasses.any { sub -> sub.qualifiedName == typeModel.qualifiedName }
                    }
                    val parentSealedModule = modules.find { parent ->
                        parent.isSealed && parent.sealedSubclasses.any { sub -> sub.qualifiedName == typeModel.qualifiedName }
                    }
                    
                    val prefix = if (parentSealedModel != null) {
                        "${parentSealedModel.simpleName}."
                    } else if (parentSealedModule != null) {
                        "${parentSealedModule.moduleName}."
                    } else {
                        ""
                    }
                    val baseName = matchedModule?.moduleName ?: typeModel.simpleName
                    return "$prefix$baseName$typeArgs"
                }
                return when (typeModel.simpleName) {
                    "Double", "Int" -> "number"
                    "String" -> "string"
                    "Boolean" -> "boolean"
                    "Unit" -> "void"
                    "Any" -> "unknown"
                    "List", "Array" -> {
                        val elementTsType = typeModel.arguments.firstOrNull()?.let { getTsType(it) } ?: "unknown"
                        "Array<$elementTsType>"
                    }
                    "Map" -> {
                        val keyTsType = typeModel.arguments.getOrNull(0)?.let { getTsType(it) } ?: "unknown"
                        val valueTsType = typeModel.arguments.getOrNull(1)?.let { getTsType(it) } ?: "unknown"
                        "Record<$keyTsType, $valueTsType>"
                    }
                    else -> "unknown"
                }
            }

            modules.forEach { module ->
                module.sealedSubclasses.forEach { collectTypes(it) }
                module.exportFunctions.forEach { func ->
                    func.parameters.forEach { collectTypes(it.type) }
                    collectTypes(func.returnType)
                }
            }

            allTypes.filter { it.isEnum }.forEach { type ->
                appendLine("export type ${type.simpleName} = ${type.enumValues.joinToString(" | ") { "\"$it\"" }};")
                appendLine()
            }

            val allSealedSubclasses = mutableSetOf<String>()
            allTypes.filter { it.isSealed }.forEach { type -> allSealedSubclasses.addAll(type.sealedSubclasses.map { it.qualifiedName }) }
            modules.filter { it.isSealed }.forEach { mod -> allSealedSubclasses.addAll(mod.sealedSubclasses.map { it.qualifiedName }) }

            val serializableTypes = allTypes.filter { it.isSerializable }

            serializableTypes.filter { it.isSealed }.forEach { type ->
                // 如果这个 sealed class 同时也是一个 HarmonyModule，那么我们跳过它，交给模块处理逻辑去生成 interface 和 namespace
                val isModule = modules.any { it.className == type.simpleName && !it.isFileExtension }
                if (isModule) return@forEach

                val typeParams = if (type.typeParameters.isNotEmpty()) {
                    "<${type.typeParameters.joinToString(", ")}>"
                } else ""
                
                val subclassesStr = if (type.sealedSubclasses.isNotEmpty()) {
                    type.sealedSubclasses.joinToString(" | ") { sub ->
                        val subTypeParams = if (sub.typeParameters.isNotEmpty()) {
                            "<${sub.typeParameters.joinToString(", ")}>"
                        } else ""
                        "${type.simpleName}.${sub.simpleName}$subTypeParams"
                    }
                } else {
                    "never"
                }
                appendLine("export type ${type.simpleName}$typeParams = $subclassesStr;")

                appendLine("export namespace ${type.simpleName} {")
                type.sealedSubclasses.forEach { sub ->
                    val subTypeParams = if (sub.typeParameters.isNotEmpty()) {
                        "<${sub.typeParameters.joinToString(", ")}>"
                    } else ""
                    val extendsClause = ""
                    
                    appendLine("    export interface ${sub.simpleName}$subTypeParams$extendsClause {")
                    val discriminator = sub.serialName ?: sub.simpleName
                    appendLine("        type: \"$discriminator\";")
                    sub.properties.forEach { prop ->
                        appendLine("        ${prop.name}: ${getTsType(prop.type)};")
                    }
                    appendLine("    }")
                }
                appendLine("}")
                appendLine()
            }

            serializableTypes.filter { !it.isSealed && it.qualifiedName !in allSealedSubclasses }.forEach { type ->
                val isModule = modules.any { it.className == type.simpleName && !it.isFileExtension }
                if (isModule) return@forEach

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
                    appendLine("    ${prop.name}: ${getTsType(prop.type)};")
                }
                appendLine("}")
                appendLine()
            }

            modules.forEach { module ->
                if (module.isFileExtension) {
                    appendLine("export declare namespace ${module.moduleName} {")
                    module.exportFunctions.forEach { func ->
                        val params = func.parameters.joinToString(", ") { "${it.name}: ${getTsType(it.type)}" }
                        val returnType = getTsType(func.returnType)
                        appendLine("    function ${func.functionName}($params): $returnType;")
                    }
                    appendLine("}")
                    return@forEach
                }

                if (module.isInterface || module.isAbstract || module.isSealed || module.isData || (!module.isObject && !module.isFileExtension)) {
                    val typeParams = if (module.typeParameters.isNotEmpty()) {
                        "<${module.typeParameters.joinToString(", ")}>"
                    } else ""
                    
                    val extendsClause = if (module.superTypes.isNotEmpty()) {
                        " extends ${module.superTypes.joinToString(", ") { getTsType(it) }}"
                    } else ""
                    
                    if (module.isAbstract) {
                        appendLine("export declare abstract class ${module.moduleName}$typeParams$extendsClause {")
                    } else if (module.isData) {
                        appendLine("export interface ${module.moduleName}$typeParams$extendsClause {")
                        module.primaryConstructorParams.forEach { param ->
                            appendLine("    ${param.name}: ${getTsType(param.type)};")
                        }
                    } else if (!module.isObject && !module.isFileExtension && !module.isInterface && !module.isSealed) {
                        appendLine("export declare class ${module.moduleName}$typeParams$extendsClause {")
                        val constructorParams = module.primaryConstructorParams.joinToString(", ") { param ->
                            "${param.name}: ${getTsType(param.type)}"
                        }
                        appendLine("    constructor($constructorParams);")
                    } else {
                        appendLine("export interface ${module.moduleName}$typeParams$extendsClause {")
                    }
                    
                    module.exportFunctions.forEach { func ->
                        val params = func.parameters.joinToString(", ") { param ->
                            val tsType = getTsType(param.type)
                            "${param.name}: $tsType"
                        }
                        val tsReturnType = getTsType(func.returnType)
                        val abstractModifier = if (module.isAbstract && func.isAbstract) "abstract " else ""
                        appendLine("    $abstractModifier${func.functionName}($params): $tsReturnType;")
                    }
                    appendLine("}")
                    
                    if (module.isSealed && module.sealedSubclasses.isNotEmpty()) {
                        appendLine("export namespace ${module.moduleName} {")
                        module.sealedSubclasses.forEach { sub ->
                            val subTypeParams = if (sub.typeParameters.isNotEmpty()) {
                                "<${sub.typeParameters.joinToString(", ")}>"
                            } else ""
                            val extendsClause = " extends ${module.moduleName}$typeParams"
                            
                            appendLine("    export interface ${sub.simpleName}$subTypeParams$extendsClause {")
                            val discriminator = sub.serialName ?: sub.simpleName
                            appendLine("        type: \"$discriminator\";")
                            sub.properties.forEach { prop ->
                                appendLine("        ${prop.name}: ${getTsType(prop.type)};")
                            }
                            appendLine("    }")
                        }
                        appendLine("}")
                    }
                } else {
                    appendLine("export declare namespace ${module.moduleName} {")
                    module.exportFunctions.forEach { func ->
                        val params = func.parameters.joinToString(", ") { param ->
                            val tsType = getTsType(param.type)
                            "${param.name}: $tsType"
                        }
                        val tsReturnType = getTsType(func.returnType)
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

        // Generate ArkTS exports file
        val etsContent = buildString {
            val runtimeValues = mutableSetOf<String>()
            val typeDefinitions = mutableSetOf<String>()

            modules.forEach { module ->
                if (module.isInterface || module.isSealed || module.isData) {
                    typeDefinitions.add(module.moduleName)
                } else if (!module.isFileExtension) {
                    runtimeValues.add(module.moduleName)
                }
            }

            // `allTypes` includes pure types like Enum, Serializable Data/Sealed Classes
            val allTypes = mutableSetOf<HarmonyTypeModel>()
            fun collectTypes(type: HarmonyTypeModel) {
                if (allTypes.add(type)) {
                    type.arguments.forEach { collectTypes(it) }
                    type.properties.forEach { collectTypes(it.type) }
                    type.sealedSubclasses.forEach { collectTypes(it) }
                }
            }
            modules.forEach { module ->
                module.sealedSubclasses.forEach { collectTypes(it) }
                module.exportFunctions.forEach { func ->
                    func.parameters.forEach { collectTypes(it.type) }
                    collectTypes(func.returnType)
                }
            }

            val allSealedSubclasses = mutableSetOf<String>()
            allTypes.filter { it.isSealed }.forEach { type -> allSealedSubclasses.addAll(type.sealedSubclasses.map { it.qualifiedName }) }
            modules.filter { it.isSealed }.forEach { mod -> allSealedSubclasses.addAll(mod.sealedSubclasses.map { it.qualifiedName }) }

            allTypes.filter { it.isEnum || it.isSerializable }.forEach { type ->
                val isModule = modules.any { m -> m.className == type.simpleName && !m.isFileExtension }
                if (!isModule && type.qualifiedName !in allSealedSubclasses) {
                    typeDefinitions.add(type.simpleName)
                }
            }

            if (runtimeValues.isNotEmpty()) {
                appendLine("import { ${runtimeValues.joinToString(", ")} } from '$libName';")
            }
            if (typeDefinitions.isNotEmpty()) {
                appendLine("import type { ${typeDefinitions.joinToString(", ")} } from '$libName';")
            }
            appendLine()

            if (runtimeValues.isNotEmpty()) {
                appendLine("export { ${runtimeValues.joinToString(", ")} };")
            }
            if (typeDefinitions.isNotEmpty()) {
                appendLine("export type { ${typeDefinitions.joinToString(", ")} };")
            }
        }

        val etsFile = codeGenerator.createNewFileByPath(dependencies, "ts/GeneratedExports", "ets")
        OutputStreamWriter(etsFile).use { writer ->
            writer.write(etsContent)
        }
    }
}