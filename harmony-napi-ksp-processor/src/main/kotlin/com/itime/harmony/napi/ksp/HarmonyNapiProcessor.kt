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
        val serialName = resolved.declaration.annotations.firstOrNull { it.shortName.asString() == "SerialName" }
            ?.arguments?.firstOrNull { it.name?.asString() == "value" }?.value as? String
        val typeParameters = (resolved.declaration as? KSClassDeclaration)?.typeParameters?.map { it.name.asString() } ?: emptyList()
        val sealedSubclasses = (resolved.declaration as? KSClassDeclaration)?.getSealedSubclasses()?.map { subclass ->
            val subSimpleName = subclass.simpleName.asString()
            val subQualifiedName = subclass.qualifiedName?.asString() ?: ""
            val subIsSerializable = subclass.annotations.any { it.shortName.asString() == "Serializable" }
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

        if (moduleSymbols.isEmpty()) {
            return emptyList()
        }

        val modules = moduleSymbols.map { classDecl ->
            val moduleName = classDecl.annotations
                .first { it.shortName.asString() == "HarmonyModule" }
                .arguments.first { it.name?.asString() == "name" }.value as String

            val exportFunctions = classDecl.getAllFunctions()
                .filter { it.annotations.any { ann -> ann.shortName.asString() == "HarmonyExport" } }
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
                        isAbstract = funcDecl.isAbstract
                    )
                }.toList()

            val isInterface = classDecl.classKind == ClassKind.INTERFACE
            val isAbstract = classDecl.modifiers.contains(Modifier.ABSTRACT)
            val isSealed = classDecl.modifiers.contains(Modifier.SEALED)
            val typeParameters = classDecl.typeParameters.map { it.name.asString() }
            
            val sealedSubclasses = if (isSealed) {
                classDecl.getSealedSubclasses().map { subclass ->
                    val subSimpleName = subclass.simpleName.asString()
                    val subQualifiedName = subclass.qualifiedName?.asString() ?: ""
                    val subIsSerializable = subclass.annotations.any { it.shortName.asString() == "Serializable" }
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

            HarmonyModuleModel(
                className = classDecl.simpleName.asString(),
                moduleName = moduleName,
                packageName = classDecl.packageName.asString(),
                classDeclaration = classDecl,
                containingFile = classDecl.containingFile,
                exportFunctions = exportFunctions,
                isInterface = isInterface,
                isAbstract = isAbstract,
                isSealed = isSealed,
                typeParameters = typeParameters,
                sealedSubclasses = sealedSubclasses,
                superTypes = superTypes
            )
        }

        // 调用生成器
        val kotlinWrapperGenerator = KotlinWrapperGenerator(codeGenerator)
        val initEntryGenerator = InitEntryGenerator(codeGenerator)
        val typeScriptGenerator = TypeScriptGenerator(codeGenerator)

        modules.forEach { module ->
            kotlinWrapperGenerator.generate(module)
        }
        
        initEntryGenerator.generate(modules)
        typeScriptGenerator.generate(modules)

        isGenerated = true
        return emptyList()
    }
}
