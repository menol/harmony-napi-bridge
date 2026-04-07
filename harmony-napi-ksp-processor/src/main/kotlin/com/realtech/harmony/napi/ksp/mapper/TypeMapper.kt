package com.realtech.harmony.napi.ksp.mapper

import com.realtech.harmony.napi.ksp.models.HarmonyTypeModel

object TypeMapper {

    fun getKotlinTypeString(typeModel: HarmonyTypeModel): String {
        if (typeModel.isTypeParameter) return typeModel.simpleName + if (typeModel.isNullable) "?" else ""
        if (typeModel.qualifiedName.isNotEmpty()) {
            val typeArgs = if (typeModel.arguments.isNotEmpty()) {
                "<${typeModel.arguments.joinToString(", ") { getKotlinTypeString(it) }}>"
            } else ""
            val name = typeModel.qualifiedName
            return "$name$typeArgs" + if (typeModel.isNullable) "?" else ""
        }
        return typeModel.simpleName + if (typeModel.isNullable) "?" else ""
    }

    /**
     * 将 Kotlin 类型转换为从 JS 取值 (napi_value) 到 Kotlin 的扩展方法名
     */
    fun getNapiToKotlinMethod(typeModel: HarmonyTypeModel): String {
        if (typeModel.isTypeParameter) return "toKotlinAny(env!!)"
        if (typeModel.isSealed || typeModel.isSerializable) return "toKotlinObject<${getKotlinTypeString(typeModel).removeSuffix("?")}>(env!!)"
        if (typeModel.isEnum) return "toKotlinEnum<${getKotlinTypeString(typeModel).removeSuffix("?")}>(env!!)"
        return when (typeModel.simpleName) {
            "Double" -> "toKotlinDouble(env!!)"
            "Int" -> "toKotlinInt(env!!)"
            "String" -> "toKotlinString(env!!)"
            "Boolean" -> "toKotlinBoolean(env!!)"
            "Any" -> "toKotlinAny(env!!)"
            "List", "MutableList" -> {
                val elementType = typeModel.arguments.firstOrNull()
                val elementTsName = elementType?.simpleName ?: "Any"
                val baseMethod = if (elementTsName == "Any") {
                    "toKotlinAnyList(env!!)"
                } else if (elementType?.isEnum == true) {
                    "toKotlinEnumList<${getKotlinTypeString(elementType).removeSuffix("?")}>(env!!)"
                } else if (elementType?.isSerializable == true || elementType?.isSealed == true) {
                    "toKotlinObjectList<${getKotlinTypeString(elementType).removeSuffix("?")}>(env!!)"
                } else {
                    if (elementTsName == "String") {
                        "toKotlinStringList(env!!)"
                    } else {
                        "toKotlin${elementTsName}List(env!!)"
                    }
                }
                if (typeModel.simpleName == "MutableList") {
                    "$baseMethod?.toMutableList()"
                } else {
                    baseMethod
                }
            }
            "Map", "MutableMap" -> {
                val keyType = typeModel.arguments.getOrNull(0)?.simpleName ?: "Any"
                val valueType = typeModel.arguments.getOrNull(1)
                val valueTsName = valueType?.simpleName ?: "Any"
                val baseMethod = if (keyType == "String" && valueTsName == "Any") {
                    "toKotlinStringAnyMap(env!!)"
                } else if (keyType == "String" && (valueType?.isSerializable == true || valueType?.isSealed == true)) {
                    "toKotlinStringObjectMap<${getKotlinTypeString(valueType).removeSuffix("?")}>(env!!)"
                } else if (keyType == "String" && valueType?.isEnum == true) {
                    "toKotlinStringEnumMap<${getKotlinTypeString(valueType).removeSuffix("?")}>(env!!)"
                } else {
                    if (keyType == "String" && valueTsName == "String") {
                        "toKotlinStringStringMap(env!!)"
                    } else {
                        "toKotlin${keyType}${valueTsName}Map(env!!)"
                    }
                }
                if (typeModel.simpleName == "MutableMap") {
                    "$baseMethod?.toMutableMap()"
                } else {
                    baseMethod
                }
            }
            "Unit" -> ""

            else -> {
                if (typeModel.isInterface) {
                    "let { com.realtech.harmony.napi.generated.${typeModel.simpleName}_NapiProxy(env!!, it) }"
                } else {
                    "unwrapKotlinObject<${getKotlinTypeString(typeModel).removeSuffix("?")}>(env!!)"
                }
            }
        }
    }

    /**
     * 将 Kotlin 类型转换为从 Kotlin 到 JS (napi_value) 的转换扩展名
     */
    fun getKotlinToNapiMethod(typeModel: HarmonyTypeModel): String {
        if (typeModel.isTypeParameter) return "toNapiValue(env!!)"
        if (typeModel.isSealed || typeModel.isSerializable) return "toNapiObject(env!!)"
        if (typeModel.isEnum) return "toNapiString(env!!)"
        return when (typeModel.simpleName) {
            "Double", "Int", "String", "Boolean" -> "toNapiValue(env!!)"
            "Any" -> "toNapiValue(env!!)"
            "List", "MutableList" -> {
                val elementType = typeModel.arguments.firstOrNull()
                val elementTsName = elementType?.simpleName ?: "Any"
                if (elementTsName == "String") return "toNapiValue(env!!)"
                if (elementTsName == "Any") return "toNapiValueAnyList(env!!)"
                if (elementType?.isEnum == true) return "toNapiValueEnumList(env!!)"
                if (elementType?.isSerializable == true || elementType?.isSealed == true) return "toNapiValueObjectList(env!!)"
                
                "toNapiValue${elementTsName}List(env!!)"
            }
            "Map", "MutableMap" -> {
                val keyType = typeModel.arguments.getOrNull(0)?.simpleName ?: "Any"
                val valueType = typeModel.arguments.getOrNull(1)
                val valueTsName = valueType?.simpleName ?: "Any"
                if (keyType == "String" && valueTsName == "String") return "toNapiValue(env!!)"
                if (keyType == "String" && valueTsName == "Any") return "toNapiValueStringAnyMap(env!!)"
                if (keyType == "String" && (valueType?.isSerializable == true || valueType?.isSealed == true)) return "toNapiValueStringObjectMap(env!!)"
                if (keyType == "String" && valueType?.isEnum == true) return "toNapiValueStringEnumMap(env!!)"
                
                "toNapiValue${keyType}${valueTsName}Map(env!!)"
            }
            "Unit" -> ""

            else -> {
                "toNapiWrappedObject(env!!, \"${typeModel.simpleName}\")"
            }
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
            return "${typeModel.simpleName}$typeArgs" + if (typeModel.simpleName == "String") " | null" else ""
        }
        return when (typeModel.simpleName) {
            "Double", "Int" -> "number"
            "String" -> "string | null"
            "Boolean" -> "boolean"
            "Unit" -> "void"
            "Any" -> "ESObject"
            "List", "Array", "MutableList", "ArrayList", "Set", "MutableSet" -> {
                val elementTsType = typeModel.arguments.firstOrNull()?.let { getTsType(it) } ?: "ESObject"
                "Array<$elementTsType> | null"
            }
            "Map", "MutableMap", "HashMap", "LinkedHashMap" -> {
                val keyTsType = typeModel.arguments.getOrNull(0)?.let { getTsType(it) } ?: "ESObject"
                val valueTsType = typeModel.arguments.getOrNull(1)?.let { getTsType(it) } ?: "ESObject"
                "Record<$keyTsType, $valueTsType>"
            }
            else -> {
                if (typeModel.isMutable) {
                    when (typeModel.simpleName) {
                        "MutableList" -> {
                            val elementTsType = typeModel.arguments.firstOrNull()?.let { getTsType(it) } ?: "ESObject"
                            "Array<$elementTsType> | null"
                        }
                        "MutableMap" -> {
                            val keyTsType = typeModel.arguments.getOrNull(0)?.let { getTsType(it) } ?: "ESObject"
                            val valueTsType = typeModel.arguments.getOrNull(1)?.let { getTsType(it) } ?: "ESObject"
                            "Record<$keyTsType, $valueTsType>"
                        }
                        "MutableSet" -> {
                            val elementTsType = typeModel.arguments.firstOrNull()?.let { getTsType(it) } ?: "ESObject"
                            "Array<$elementTsType> | null"
                        }
                        else -> typeModel.simpleName
                    }
                } else {
                    typeModel.simpleName
                }
            }
        }
    }
}
