package com.itime.harmony.napi.ksp.mapper

import com.itime.harmony.napi.ksp.models.HarmonyTypeModel

object TypeMapper {

    private fun getKotlinTypeString(typeModel: HarmonyTypeModel): String {
        if (typeModel.isTypeParameter) return typeModel.simpleName
        val typeArgs = if (typeModel.arguments.isNotEmpty()) {
            "<${typeModel.arguments.joinToString(", ") { getKotlinTypeString(it) }}>"
        } else ""
        val name = if (typeModel.qualifiedName.isNotEmpty()) typeModel.qualifiedName else typeModel.simpleName
        return "$name$typeArgs"
    }

    /**
     * 将 Kotlin 类型转换为从 JS 取值 (napi_value) 到 Kotlin 的扩展方法名
     */
    fun getNapiToKotlinMethod(typeModel: HarmonyTypeModel): String {
        if (typeModel.isAbstract) return "unwrapKotlinObject<${getKotlinTypeString(typeModel)}>(env!!)"
        if (typeModel.isSealed || typeModel.isSerializable) return "toKotlinObject<${getKotlinTypeString(typeModel)}>(env!!)"
        if (typeModel.isEnum) return "toKotlinEnum<${getKotlinTypeString(typeModel)}>(env!!)"
        return when (typeModel.simpleName) {
            "Double" -> "toKotlinDouble(env!!)"
            "Int" -> "toKotlinInt(env!!)"
            "String" -> "toKotlinString(env!!)"
            "Boolean" -> "toKotlinBoolean(env!!)"
            "Any" -> "toKotlinAny(env!!)"
            "List" -> {
                val elementType = typeModel.arguments.firstOrNull()?.simpleName ?: "Any"
                "toKotlin${elementType}List(env!!)"
            }
            "Map" -> {
                val keyType = typeModel.arguments.getOrNull(0)?.simpleName ?: "Any"
                val valueType = typeModel.arguments.getOrNull(1)?.simpleName ?: "Any"
                "toKotlin${keyType}${valueType}Map(env!!)"
            }
            else -> throw IllegalArgumentException("Unsupported type ${typeModel.simpleName} in KSP mapper")
        }
    }

    /**
     * 将 Kotlin 类型转换为从 Kotlin 到 JS (napi_value) 的转换扩展名
     */
    fun getKotlinToNapiMethod(typeModel: HarmonyTypeModel): String {
        if (typeModel.isAbstract) return "toNapiWrappedObject(env!!, \"${typeModel.simpleName}\")"
        if (typeModel.isSealed || typeModel.isSerializable) return "toNapiObject(env!!)"
        if (typeModel.isEnum) return "toNapiString(env!!)"
        return when (typeModel.simpleName) {
            "Double", "Int", "String", "Boolean" -> "toNapiValue(env!!)"
            "Any" -> "toNapiValue(env!!)"
            "List" -> {
                val elementType = typeModel.arguments.firstOrNull()?.simpleName ?: "Any"
                if (elementType == "String") return "toNapiValue(env!!)"
                "toNapiValue${elementType}List(env!!)"
            }
            "Map" -> {
                val keyType = typeModel.arguments.getOrNull(0)?.simpleName ?: "Any"
                val valueType = typeModel.arguments.getOrNull(1)?.simpleName ?: "Any"
                if (keyType == "String" && valueType == "String") return "toNapiValue(env!!)"
                "toNapiValue${keyType}${valueType}Map(env!!)"
            }
            "Unit" -> ""
            else -> throw IllegalArgumentException("Unsupported type ${typeModel.simpleName} in KSP mapper")
        }
    }

    /**
     * 将 Kotlin 类型转换为 TypeScript 中的类型声明
     */
    fun getTsType(typeModel: HarmonyTypeModel): String {
        if (typeModel.isTypeParameter) return typeModel.simpleName
        if (typeModel.isAbstract || typeModel.isSerializable || typeModel.isEnum) {
            val typeArgs = if (typeModel.arguments.isNotEmpty()) {
                "<${typeModel.arguments.joinToString(", ") { getTsType(it) }}>"
            } else ""
            return "${typeModel.simpleName}$typeArgs"
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
}
