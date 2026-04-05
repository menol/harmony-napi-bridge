package com.itime.harmony.napi.ksp.models

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile

data class HarmonyModuleModel(
    val className: String,
    val moduleName: String,
    val packageName: String,
    val classDeclaration: KSClassDeclaration?,
    val containingFile: KSFile?,
    val exportFunctions: MutableList<HarmonyExportModel> = mutableListOf(),
    val isInterface: Boolean = false,
    val isAbstract: Boolean = false,
    val isSealed: Boolean = false,
    val isData: Boolean = false,
    val isObject: Boolean = false,
    val isClass: Boolean = false,
    val primaryConstructorParams: List<HarmonyParameterModel> = emptyList(),
    val typeParameters: List<String> = emptyList(),
    val sealedSubclasses: List<HarmonyTypeModel> = emptyList(),
    val superTypes: List<HarmonyTypeModel> = emptyList(),
    val isFileExtension: Boolean = false
)

data class HarmonyExportModel(
    val functionName: String,
    val parameters: List<HarmonyParameterModel>,
    val returnType: HarmonyTypeModel,
    val isAbstract: Boolean = false,
    val isExtension: Boolean = false
)

data class HarmonyParameterModel(
    val name: String,
    val type: HarmonyTypeModel
)

data class HarmonyPropertyModel(val name: String, val type: HarmonyTypeModel)

data class HarmonyTypeModel(
    val simpleName: String,
    val arguments: List<HarmonyTypeModel> = emptyList(),
    val qualifiedName: String = "",
    val packageName: String = "",
    val isSerializable: Boolean = false,
    val isEnum: Boolean = false,
    val properties: List<HarmonyPropertyModel> = emptyList(),
    val enumValues: List<String> = emptyList(),
    val isTypeParameter: Boolean = false,
    val isSealed: Boolean = false,
    val isAbstract: Boolean = false,
    val sealedSubclasses: List<HarmonyTypeModel> = emptyList(),
    val typeParameters: List<String> = emptyList(),
    val serialName: String? = null
) {
    val hasGenerics: Boolean get() = arguments.isNotEmpty()
}