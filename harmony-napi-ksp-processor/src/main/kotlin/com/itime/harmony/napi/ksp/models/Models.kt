package com.itime.harmony.napi.ksp.models

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile

data class HarmonyModuleModel(
    val className: String,
    val moduleName: String,
    val packageName: String,
    val classDeclaration: KSClassDeclaration,
    val containingFile: KSFile?,
    val exportFunctions: List<HarmonyExportModel>
)

data class HarmonyExportModel(
    val functionName: String,
    val parameters: List<HarmonyParameterModel>,
    val returnType: HarmonyTypeModel
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
    val isSerializable: Boolean = false,
    val isEnum: Boolean = false,
    val properties: List<HarmonyPropertyModel> = emptyList(),
    val enumValues: List<String> = emptyList()
) {
    val hasGenerics: Boolean get() = arguments.isNotEmpty()
}