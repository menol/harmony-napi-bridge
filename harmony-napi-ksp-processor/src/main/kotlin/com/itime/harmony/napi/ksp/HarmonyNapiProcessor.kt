package com.itime.harmony.napi.ksp

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.itime.harmony.napi.ksp.generator.InitEntryGenerator
import com.itime.harmony.napi.ksp.generator.KotlinWrapperGenerator
import com.itime.harmony.napi.ksp.generator.TypeScriptGenerator
import com.itime.harmony.napi.ksp.models.HarmonyExportModel
import com.itime.harmony.napi.ksp.models.HarmonyModuleModel
import com.itime.harmony.napi.ksp.models.HarmonyParameterModel
import com.itime.harmony.napi.ksp.models.HarmonyTypeModel
import com.itime.harmony.napi.ksp.models.HarmonyPropertyModel
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.Modifier
import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.getDeclaredProperties

class HarmonyNapiProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    private var isGenerated = false

    private fun resolveType(typeRef: KSTypeReference): HarmonyTypeModel {
        val resolved = typeRef.resolve()
        val simpleName = resolved.declaration.simpleName.asString()
        val arguments = resolved.arguments.mapNotNull { arg ->
            arg.type?.let { resolveType(it) }
        }
        val qualifiedName = resolved.declaration.qualifiedName?.asString() ?: ""
        val isSerializable = resolved.declaration.annotations.any { it.shortName.asString() == "Serializable" }
        val isEnum = (resolved.declaration as? KSClassDeclaration)?.classKind == ClassKind.ENUM_CLASS
        val isTypeParameter = resolved.declaration is KSTypeParameter
        val isSealed = (resolved.declaration as? KSClassDeclaration)?.modifiers?.contains(Modifier.SEALED) == true
        val isAbstract = (resolved.declaration as? KSClassDeclaration)?.modifiers?.contains(Modifier.ABSTRACT) == true
        val serialName = resolved.declaration.annotations.firstOrNull { it.shortName.asString() == "SerialName" }
            ?.arguments?.firstOrNull { it.name?.asString() == "value" }?.value as? String
        val typeParameters = (resolved.declaration as? KSClassDeclaration)?.typeParameters?.map { it.name.asString() } ?: emptyList()
        val sealedSubclasses = (resolved.declaration as? KSClassDeclaration)?.getSealedSubclasses()?.map { subclass ->
            val subSimpleName = subclass.simpleName.asString()
            val subQualifiedName = subclass.qualifiedName?.asString() ?: ""
            val subIsSerializable = subclass.annotations.any { it.shortName.asString() == "Serializable" }
            val subIsAbstract = subclass.modifiers.contains(Modifier.ABSTRACT)
            val subSerialName = subclass.annotations.firstOrNull { it.shortName.asString() == "SerialName" }
                ?.arguments?.firstOrNull { it.name?.asString() == "value" }?.value as? String
            val subTypeParams = subclass.typeParameters.map { it.name.asString() }
            val subProps = if (subIsSerializable) {
                subclass.getDeclaredProperties()
                    .map { HarmonyPropertyModel(it.simpleName.asString(), resolveType(it.type)) }
                    .toList()
            } else emptyList()
            HarmonyTypeModel(
                simpleName = subSimpleName,
                qualifiedName = subQualifiedName,
                isSerializable = subIsSerializable,
                isAbstract = subIsAbstract,
                properties = subProps,
                typeParameters = subTypeParams,
                serialName = subSerialName
            )
        }?.toList() ?: emptyList()

        val properties = if (isSerializable) {
            (resolved.declaration as? KSClassDeclaration)?.getDeclaredProperties()
                ?.map { HarmonyPropertyModel(it.simpleName.asString(), resolveType(it.type)) }
                ?.toList() ?: emptyList()
        } else emptyList()
        val enumValues = if (isEnum) {
            (resolved.declaration as? KSClassDeclaration)?.declarations
                ?.filterIsInstance<KSClassDeclaration>()
                ?.filter { it.classKind == ClassKind.ENUM_ENTRY }
                ?.map { it.simpleName.asString() }
                ?.toList() ?: emptyList()
        } else emptyList()

        return HarmonyTypeModel(
            simpleName = simpleName,
            arguments = arguments,
            qualifiedName = qualifiedName,
            isSerializable = isSerializable,
            isEnum = isEnum,
            properties = properties,
            enumValues = enumValues,
            isTypeParameter = isTypeParameter,
            isSealed = isSealed,
            isAbstract = isAbstract,
            sealedSubclasses = sealedSubclasses,
            typeParameters = typeParameters,
            serialName = serialName
        )
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (isGenerated) return emptyList()
        logger.info("HarmonyNapiProcessor started processing...")

        val moduleSymbols = resolver.getSymbolsWithAnnotation("com.itime.harmony.napi.annotations.HarmonyModule")
            .filterIsInstance<KSClassDeclaration>()
            .toList()

        val extensionFileSymbols = resolver.getSymbolsWithAnnotation("com.itime.harmony.napi.annotations.HarmonyExtensions")
            .filterIsInstance<KSFile>()
            .toList()

        val extensionModules = mutableListOf<HarmonyModuleModel>()

        if (moduleSymbols.isEmpty() && extensionFileSymbols.isEmpty()) {
            return emptyList()
        }

        val modules = moduleSymbols.map { classDecl ->
            val moduleName = classDecl.annotations
                .firstOrNull { it.shortName.asString() == "HarmonyModule" }
                ?.arguments?.firstOrNull { it.name?.asString() == "name" }?.value as? String ?: classDecl.simpleName.asString()

            val exportFunctions = classDecl.getAllFunctions()
                .filter { funcDecl ->
                    val isHarmonyExport = funcDecl.annotations.any { ann -> ann.shortName.asString() == "HarmonyExport" }
                    val isAnyMethod = funcDecl.simpleName.asString() in listOf("equals", "hashCode", "toString", "<init>", "copy", "component1", "component2", "component3", "component4", "component5")
                    val isPublic = funcDecl.isPublic()
                    isHarmonyExport || (!isAnyMethod && isPublic)
                }
                .map { funcDecl ->
                    val params = funcDecl.parameters.map { param ->
                        HarmonyParameterModel(
                            name = param.name?.asString() ?: "arg",
                            type = resolveType(param.type)
                        )
                    }
                    val returnType = funcDecl.returnType?.let { resolveType(it) } ?: HarmonyTypeModel("Unit")
                    HarmonyExportModel(
                        functionName = funcDecl.simpleName.asString(),
                        parameters = params,
                        returnType = returnType,
                        isAbstract = funcDecl.isAbstract,
                        isExtension = false
                    )
                }.toList()

            val isInterface = classDecl.classKind == ClassKind.INTERFACE
            val isAbstract = classDecl.modifiers.contains(Modifier.ABSTRACT)
            val isSealed = classDecl.modifiers.contains(Modifier.SEALED)
            val isData = classDecl.modifiers.contains(Modifier.DATA)
            val isObject = classDecl.classKind == ClassKind.OBJECT
            val isClass = classDecl.classKind == ClassKind.CLASS && !isAbstract && !isSealed && !isData
            val typeParameters = classDecl.typeParameters.map { it.name.asString() }

            // Handle data class internal methods as global tools, conceptually similar to extensions
            if (isData) {
                val dataClassMethods = classDecl.getAllFunctions()
                    .filter { funcDecl ->
                        val isHarmonyExport = funcDecl.annotations.any { ann -> ann.shortName.asString() == "HarmonyExport" }
                        val isAnyMethod = funcDecl.simpleName.asString() in listOf("equals", "hashCode", "toString", "<init>", "copy", "component1", "component2", "component3", "component4", "component5")
                        val isPublic = funcDecl.isPublic()
                        val isAbstract = funcDecl.isAbstract
                        val isExtension = funcDecl.extensionReceiver != null
                        !isAbstract && !isExtension && (isHarmonyExport || (!isAnyMethod && isPublic))
                    }
                if (dataClassMethods.iterator().hasNext()) {
                    val extensionFunctions = dataClassMethods.map { func ->
                        val params = mutableListOf(
                            HarmonyParameterModel(
                                name = "receiver",
                                type = HarmonyTypeModel(
                                    simpleName = classDecl.simpleName.asString(),
                                    qualifiedName = classDecl.qualifiedName?.asString() ?: "",
                                    packageName = classDecl.packageName.asString()
                                )
                            )
                        )
                        params.addAll(func.parameters.map { param ->
                            HarmonyParameterModel(
                                name = param.name?.asString() ?: "arg",
                                type = resolveType(param.type)
                            )
                        })
                        
                        val returnType = func.returnType?.let { resolveType(it) } ?: HarmonyTypeModel("Unit")
                        HarmonyExportModel(
                            functionName = func.simpleName.asString(),
                            parameters = params,
                            returnType = returnType,
                            isAbstract = false,
                            isExtension = true
                        )
                    }
                    
                    val utilsName = "${classDecl.simpleName.asString()}Utils"
                    val existingExtensionModule = extensionModules.find { it.className == utilsName }
                    if (existingExtensionModule != null) {
                        existingExtensionModule.exportFunctions.addAll(extensionFunctions)
                    } else {
                        extensionModules.add(
                            HarmonyModuleModel(
                                className = utilsName,
                                moduleName = "${moduleName}Utils",
                                packageName = classDecl.packageName.asString(),
                                classDeclaration = null,
                                containingFile = classDecl.containingFile,
                                exportFunctions = extensionFunctions.toMutableList(),
                                isInterface = false,
                                isAbstract = false,
                                isSealed = false,
                                isData = false,
                                isObject = false,
                                isClass = false,
                                isFileExtension = true
                            )
                        )
                    }
                }
            }

            val primaryConstructorParams = classDecl.primaryConstructor?.parameters?.map { param ->
                HarmonyParameterModel(
                    name = param.name?.asString() ?: "arg",
                    type = resolveType(param.type)
                )
            } ?: emptyList()

            val sealedSubclasses = if (isSealed) {
                classDecl.getSealedSubclasses().map { subclass ->
                    val subSimpleName = subclass.simpleName.asString()
                    val subQualifiedName = subclass.qualifiedName?.asString() ?: ""
                    val subIsSerializable = subclass.annotations.any { it.shortName.asString() == "Serializable" }
                    val subIsAbstract = subclass.modifiers.contains(Modifier.ABSTRACT)
                    val subSerialName = subclass.annotations.firstOrNull { it.shortName.asString() == "SerialName" }
                        ?.arguments?.firstOrNull { it.name?.asString() == "value" }?.value as? String
                    val subTypeParams = subclass.typeParameters.map { it.name.asString() }
                    val subProps = if (subIsSerializable) {
                        subclass.getDeclaredProperties()
                            .map { HarmonyPropertyModel(it.simpleName.asString(), resolveType(it.type)) }
                            .toList()
                    } else emptyList()
                    HarmonyTypeModel(
                        simpleName = subSimpleName,
                        qualifiedName = subQualifiedName,
                        isSerializable = subIsSerializable,
                        isAbstract = subIsAbstract,
                        properties = subProps,
                        typeParameters = subTypeParams,
                        serialName = subSerialName
                    )
                }.toList()
            } else emptyList()

            val superTypes = classDecl.superTypes
                .map { resolveType(it) }
                .filter { it.qualifiedName != "kotlin.Any" && it.simpleName != "Any" }
                .toList()

            // If it's a data class, clear its exportFunctions because they are already exported as utils
            val finalExportFunctions = if (isData) emptyList() else exportFunctions

            HarmonyModuleModel(
                className = classDecl.simpleName.asString(),
                moduleName = moduleName,
                packageName = classDecl.packageName.asString(),
                classDeclaration = classDecl,
                containingFile = classDecl.containingFile,
                exportFunctions = finalExportFunctions.toMutableList(),
                isInterface = isInterface,
                isAbstract = isAbstract,
                isSealed = isSealed,
                isData = isData,
                isObject = isObject,
                isClass = isClass,
                primaryConstructorParams = primaryConstructorParams,
                typeParameters = typeParameters,
                sealedSubclasses = sealedSubclasses,
                superTypes = superTypes
            )
        }.toMutableList()

        extensionFileSymbols.forEach { fileDecl ->
            val exportName = fileDecl.annotations
                .firstOrNull { it.shortName.asString() == "HarmonyExtensions" }
                ?.arguments?.firstOrNull { it.name?.asString() == "name" }?.value as? String ?: fileDecl.fileName.removeSuffix(".kt")

            val exportFunctions = fileDecl.declarations
                .filterIsInstance<KSFunctionDeclaration>()
                .filter { funcDecl ->
                    val isHarmonyExport = funcDecl.annotations.any { ann -> ann.shortName.asString() == "HarmonyExport" }
                    val isPublic = funcDecl.isPublic()
                    isHarmonyExport || isPublic
                }
                .map { funcDecl ->
                    val params = mutableListOf<HarmonyParameterModel>()
                    // Add receiver as the first parameter
                    funcDecl.extensionReceiver?.let { receiver ->
                        params.add(
                            HarmonyParameterModel(
                                name = "receiver",
                                type = resolveType(receiver)
                            )
                        )
                    }
                    // Add other parameters
                    params.addAll(funcDecl.parameters.map { param ->
                        HarmonyParameterModel(
                            name = param.name?.asString() ?: "arg",
                            type = resolveType(param.type)
                        )
                    })

                    val returnType = funcDecl.returnType?.let { resolveType(it) } ?: HarmonyTypeModel("Unit")

                    HarmonyExportModel(
                        functionName = funcDecl.simpleName.asString(),
                        parameters = params,
                        returnType = returnType,
                        isAbstract = false,
                        isExtension = true
                    )
                }.toList()

            extensionModules.add(HarmonyModuleModel(
                className = exportName,
                moduleName = exportName,
                packageName = fileDecl.packageName.asString(),
                classDeclaration = null,
                containingFile = fileDecl,
                exportFunctions = exportFunctions.toMutableList(),
                isInterface = false,
                isAbstract = false,
                isSealed = false,
                isObject = false,
                isFileExtension = true
            ))
        }
        
        modules.addAll(extensionModules)

        // 调用生成器
        val kotlinWrapperGenerator = KotlinWrapperGenerator(codeGenerator)
        val initEntryGenerator = InitEntryGenerator(codeGenerator)
        val typeScriptGenerator = TypeScriptGenerator(codeGenerator, options)

        modules.forEach { module ->
            kotlinWrapperGenerator.generate(module)
        }

        initEntryGenerator.generate(modules)
        typeScriptGenerator.generate(modules)

        isGenerated = true
        return emptyList()
    }
}
