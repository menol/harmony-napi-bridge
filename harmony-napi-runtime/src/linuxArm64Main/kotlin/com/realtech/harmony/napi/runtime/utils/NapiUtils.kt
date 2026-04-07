package com.realtech.harmony.napi.runtime.utils

import kotlinx.cinterop.*
import napi.*
import platform.posix.uint32_tVar
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

// String? -> napi_value
@OptIn(ExperimentalForeignApi::class)
fun String?.toNapiValue(env: napi_env): napi_value {
    if (this == null) {
        memScoped {
            val nullVar = alloc<napi_valueVar>()
            napi_get_null(env, nullVar.ptr)
            return nullVar.value!!
        }
    }
    memScoped {
        val result = alloc<napi_valueVar>()
        napi_create_string_utf8(env, this@toNapiValue, napi.NAPI_AUTO_LENGTH.convert(), result.ptr)
        return result.value!!
    }
}

// napi_value -> String?
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinString(env: napi_env): String? {
    memScoped {
        val typeVar = alloc<IntVar>()
        napi_typeof(env, this@toKotlinString, typeVar.ptr.reinterpret())
        val jsType = typeVar.value.toUInt()
        if (jsType == napi_valuetype.napi_null.value || jsType == napi_valuetype.napi_undefined.value) {
            return null
        }

        val lengthVar = alloc<size_tVar>()
        napi_get_value_string_utf8(env, this@toKotlinString, null, 0u.convert(), lengthVar.ptr)
        
        val length = lengthVar.value.toInt()
        val buffer = ByteArray(length + 1)
        
        napi_get_value_string_utf8(env, this@toKotlinString, buffer.refTo(0), (length + 1).convert(), null)
        return buffer.decodeToString(endIndex = length)
    }
}

// Double -> napi_value
@OptIn(ExperimentalForeignApi::class)
fun Double.toNapiValue(env: napi_env): napi_value {
    memScoped {
        val result = alloc<napi_valueVar>()
        napi_create_double(env, this@toNapiValue, result.ptr)
        return result.value!!
    }
}

// napi_value -> Double?
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinDouble(env: napi_env): Double? {
    memScoped {
        val typeVar = alloc<IntVar>()
        napi_typeof(env, this@toKotlinDouble, typeVar.ptr.reinterpret())
        val jsType = typeVar.value.toUInt()
        if (jsType == napi_valuetype.napi_null.value || jsType == napi_valuetype.napi_undefined.value) {
            return null
        }
        
        val result = alloc<DoubleVar>()
        napi_get_value_double(env, this@toKotlinDouble, result.ptr)
        return result.value
    }
}

// List<String>? -> napi_value
@OptIn(ExperimentalForeignApi::class)
fun List<String>?.toNapiValue(env: napi_env): napi_value {
    if (this == null) {
        memScoped {
            val nullVar = alloc<napi_valueVar>()
            napi_get_null(env, nullVar.ptr)
            return nullVar.value!!
        }
    }
    memScoped {
        val arrayVar = alloc<napi_valueVar>()
        napi_create_array_with_length(env, this@toNapiValue.size.convert(), arrayVar.ptr)
        val jsArray = arrayVar.value!!
        
        for ((index, item) in this@toNapiValue.withIndex()) {
            val jsItem = item.toNapiValue(env)
            napi_set_element(env, jsArray, index.convert(), jsItem)
        }
        return jsArray
    }
}

// napi_value -> List<String>?
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinStringList(env: napi_env): List<String>? {
    memScoped {
        val typeVar = alloc<IntVar>()
        napi_typeof(env, this@toKotlinStringList, typeVar.ptr.reinterpret())
        val jsType = typeVar.value.toUInt()
        if (jsType == napi_valuetype.napi_null.value || jsType == napi_valuetype.napi_undefined.value) {
            return null
        }

        val isArray = alloc<BooleanVar>()
        napi_is_array(env, this@toKotlinStringList, isArray.ptr)
        if (!isArray.value) return emptyList()

        val lengthVar = alloc<uint32_tVar>()
        napi_get_array_length(env, this@toKotlinStringList, lengthVar.ptr)
        val length = lengthVar.value.toInt()
        
        val list = mutableListOf<String>()
        for (i in 0 until length) {
            val elementVar = alloc<napi_valueVar>()
            napi_get_element(env, this@toKotlinStringList, i.convert(), elementVar.ptr)
            val str = elementVar.value!!.toKotlinString(env)
            if (str != null) {
                list.add(str)
            }
        }
        return list
    }
}

// Map<String, String>? -> napi_value
@OptIn(ExperimentalForeignApi::class)
fun Map<String, String>?.toNapiValue(env: napi_env): napi_value {
    if (this == null) {
        memScoped {
            val nullVar = alloc<napi_valueVar>()
            napi_get_null(env, nullVar.ptr)
            return nullVar.value!!
        }
    }
    memScoped {
        val objVar = alloc<napi_valueVar>()
        napi_create_object(env, objVar.ptr)
        val jsObj = objVar.value!!
        
        for ((key, value) in this@toNapiValue) {
            val jsValue = value.toNapiValue(env)
            napi_set_named_property(env, jsObj, key, jsValue)
        }
        return jsObj
    }
}

// napi_value -> Map<String, String>?
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinStringStringMap(env: napi_env): Map<String, String>? {
    memScoped {
        val typeVar = alloc<IntVar>()
        napi_typeof(env, this@toKotlinStringStringMap, typeVar.ptr.reinterpret())
        val jsType = typeVar.value.toUInt()
        if (jsType == napi_valuetype.napi_null.value || jsType == napi_valuetype.napi_undefined.value) {
            return null
        }
        
        val map = mutableMapOf<String, String>()
        val keysVar = alloc<napi_valueVar>()
        
        napi_get_property_names(env, this@toKotlinStringStringMap, keysVar.ptr)
        val keysArray = keysVar.value!!
        
        val lengthVar = alloc<uint32_tVar>()
        napi_get_array_length(env, keysArray, lengthVar.ptr)
        val length = lengthVar.value.toInt()
        
        for (i in 0 until length) {
            val keyVar = alloc<napi_valueVar>()
            napi_get_element(env, keysArray, i.convert(), keyVar.ptr)
            val keyStr = keyVar.value!!.toKotlinString(env) ?: continue
            
            val valueVar = alloc<napi_valueVar>()
            napi_get_named_property(env, this@toKotlinStringStringMap, keyStr, valueVar.ptr)
            val valueStr = valueVar.value!!.toKotlinString(env)
            if (valueStr != null) {
                map[keyStr] = valueStr
            }
        }
        return map
    }
}

// Boolean -> napi_value
@OptIn(ExperimentalForeignApi::class)
fun Boolean.toNapiValue(env: napi_env): napi_value {
    memScoped {
        val result = alloc<napi_valueVar>()
        napi_get_boolean(env, this@toNapiValue, result.ptr)
        return result.value!!
    }
}

// napi_value -> Boolean?
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinBoolean(env: napi_env): Boolean? {
    memScoped {
        val typeVar = alloc<IntVar>()
        napi_typeof(env, this@toKotlinBoolean, typeVar.ptr.reinterpret())
        val jsType = typeVar.value.toUInt()
        if (jsType == napi_valuetype.napi_null.value || jsType == napi_valuetype.napi_undefined.value) {
            return null
        }
        
        val result = alloc<BooleanVar>()
        napi_get_value_bool(env, this@toKotlinBoolean, result.ptr)
        return result.value
    }
}

// Int -> napi_value
@OptIn(ExperimentalForeignApi::class)
fun Int.toNapiValue(env: napi_env): napi_value {
    memScoped {
        val result = alloc<napi_valueVar>()
        napi_create_int32(env, this@toNapiValue, result.ptr)
        return result.value!!
    }
}

// napi_value -> Int?
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinInt(env: napi_env): Int? {
    memScoped {
        val typeVar = alloc<IntVar>()
        napi_typeof(env, this@toKotlinInt, typeVar.ptr.reinterpret())
        val jsType = typeVar.value.toUInt()
        if (jsType == napi_valuetype.napi_null.value || jsType == napi_valuetype.napi_undefined.value) {
            return null
        }
        
        val result = alloc<IntVar>()
        napi_get_value_int32(env, this@toKotlinInt, result.ptr)
        return result.value
    }
}

// Any? -> napi_value
@OptIn(ExperimentalForeignApi::class)
@Suppress("UNCHECKED_CAST")
fun Any?.toNapiValue(env: napi_env): napi_value {
    return when (this) {
        null -> memScoped {
            val result = alloc<napi_valueVar>()
            napi_get_null(env, result.ptr)
            result.value!!
        }
        is String -> this.toNapiValue(env)
        is Double -> this.toNapiValue(env)
        is Int -> this.toNapiValue(env)
        is Boolean -> this.toNapiValue(env)
        is List<*> -> (this as List<Any?>).toNapiValueAnyList(env)
        is Map<*, *> -> (this as Map<String, Any?>).toNapiValueStringAnyMap(env)
        else -> memScoped {
            // Fallback to string if we don't know how to serialize it dynamically
            val result = alloc<napi_valueVar>()
            napi_create_string_utf8(env, this@toNapiValue.toString(), napi.NAPI_AUTO_LENGTH.convert(), result.ptr)
            result.value!!
        }
    }
}

// napi_value -> Any?
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinAny(env: napi_env): Any? {
    memScoped {
        val typeVar = alloc<IntVar>()
        napi_typeof(env, this@toKotlinAny, typeVar.ptr.reinterpret())
        
        return when (typeVar.value.toUInt()) {
            napi_valuetype.napi_null.value, napi_valuetype.napi_undefined.value -> null
            napi_valuetype.napi_number.value -> {
                // Determine if it's an int or double
                val doubleValue = this@toKotlinAny.toKotlinDouble(env) ?: 0.0
                if (doubleValue % 1 == 0.0) doubleValue.toInt() else doubleValue
            }
            napi_valuetype.napi_string.value -> this@toKotlinAny.toKotlinString(env)
            napi_valuetype.napi_boolean.value -> this@toKotlinAny.toKotlinBoolean(env)
            napi_valuetype.napi_object.value -> {
                val isArray = alloc<BooleanVar>()
                napi_is_array(env, this@toKotlinAny, isArray.ptr)
                if (isArray.value) {
                    this@toKotlinAny.toKotlinAnyList(env)
                } else {
                    this@toKotlinAny.toKotlinStringAnyMap(env)
                }
            }
            else -> null
        }
    }
}

// List<Any?>? -> napi_value
@OptIn(ExperimentalForeignApi::class)
@Suppress("UNCHECKED_CAST")
fun List<Any?>?.toNapiValueAnyList(env: napi_env): napi_value {
    if (this == null) {
        memScoped {
            val nullVar = alloc<napi_valueVar>()
            napi_get_null(env, nullVar.ptr)
            return nullVar.value!!
        }
    }
    memScoped {
        val arrayVar = alloc<napi_valueVar>()
        napi_create_array_with_length(env, this@toNapiValueAnyList.size.convert(), arrayVar.ptr)
        val jsArray = arrayVar.value!!

        for ((index, item) in this@toNapiValueAnyList.withIndex()) {
            val jsItem = item.toNapiValue(env)
            napi_set_element(env, jsArray, index.convert(), jsItem)
        }
        return jsArray
    }
}

// napi_value -> List<Any?>?
@OptIn(ExperimentalForeignApi::class)
@Suppress("UNCHECKED_CAST")
fun napi_value.toKotlinAnyList(env: napi_env): List<Any?>? {
    memScoped {
        val typeVar = alloc<IntVar>()
        napi_typeof(env, this@toKotlinAnyList, typeVar.ptr.reinterpret())
        val jsType = typeVar.value.toUInt()
        if (jsType == napi_valuetype.napi_null.value || jsType == napi_valuetype.napi_undefined.value) {
            return null
        }
        
        val isArray = alloc<BooleanVar>()
        napi_is_array(env, this@toKotlinAnyList, isArray.ptr)
        if (!isArray.value) return emptyList()

        val lengthVar = alloc<uint32_tVar>()
        napi_get_array_length(env, this@toKotlinAnyList, lengthVar.ptr)
        val length = lengthVar.value.toInt()
        
        val list = mutableListOf<Any?>()
        for (i in 0 until length) {
            val elementVar = alloc<napi_valueVar>()
            napi_get_element(env, this@toKotlinAnyList, i.convert(), elementVar.ptr)
            list.add(elementVar.value!!.toKotlinAny(env))
        }
        return list
    }
}

// Map<String, Any?>? -> napi_value
@OptIn(ExperimentalForeignApi::class)
@Suppress("UNCHECKED_CAST")
fun Map<String, Any?>?.toNapiValueStringAnyMap(env: napi_env): napi_value {
    if (this == null) {
        memScoped {
            val nullVar = alloc<napi_valueVar>()
            napi_get_null(env, nullVar.ptr)
            return nullVar.value!!
        }
    }
    memScoped {
        val objVar = alloc<napi_valueVar>()
        napi_create_object(env, objVar.ptr)
        val jsObj = objVar.value!!

        for ((key, value) in this@toNapiValueStringAnyMap) {
            val jsValue = value.toNapiValue(env)
            napi_set_named_property(env, jsObj, key, jsValue)
        }
        return jsObj
    }
}

// napi_value -> Map<String, Any?>?
@OptIn(ExperimentalForeignApi::class)
@Suppress("UNCHECKED_CAST")
fun napi_value.toKotlinStringAnyMap(env: napi_env): Map<String, Any?>? {
    memScoped {
        val typeVar = alloc<IntVar>()
        napi_typeof(env, this@toKotlinStringAnyMap, typeVar.ptr.reinterpret())
        val jsType = typeVar.value.toUInt()
        if (jsType == napi_valuetype.napi_null.value || jsType == napi_valuetype.napi_undefined.value) {
            return null
        }
        
        val map = mutableMapOf<String, Any?>()
        val keysVar = alloc<napi_valueVar>()
        
        napi_get_property_names(env, this@toKotlinStringAnyMap, keysVar.ptr)
        val keysArray = keysVar.value!!
        
        val lengthVar = alloc<uint32_tVar>()
        napi_get_array_length(env, keysArray, lengthVar.ptr)
        val length = lengthVar.value.toInt()
        
        for (i in 0 until length) {
            val keyVar = alloc<napi_valueVar>()
            napi_get_element(env, keysArray, i.convert(), keyVar.ptr)
            val keyStr = keyVar.value!!.toKotlinString(env) ?: continue
            
            val valueVar = alloc<napi_valueVar>()
            napi_get_named_property(env, this@toKotlinStringAnyMap, keyStr, valueVar.ptr)
            val valueAny = valueVar.value!!.toKotlinAny(env)
            
            map[keyStr] = valueAny
        }
        return map
    }
}

// ---- Primitive Specific Lists and Maps ----

// List<Int>? -> napi_value
@OptIn(ExperimentalForeignApi::class)
fun List<Int>?.toNapiValueIntList(env: napi_env): napi_value {
    if (this == null) {
        memScoped {
            val nullVar = alloc<napi_valueVar>()
            napi_get_null(env, nullVar.ptr)
            return nullVar.value!!
        }
    }
    memScoped {
        val arrayVar = alloc<napi_valueVar>()
        napi_create_array_with_length(env, this@toNapiValueIntList.size.convert(), arrayVar.ptr)
        val jsArray = arrayVar.value!!
        
        for ((index, item) in this@toNapiValueIntList.withIndex()) {
            val jsItem = item.toNapiValue(env)
            napi_set_element(env, jsArray, index.convert(), jsItem)
        }
        return jsArray
    }
}

// napi_value -> List<Int>?
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinIntList(env: napi_env): List<Int>? {
    memScoped {
        val typeVar = alloc<IntVar>()
        napi_typeof(env, this@toKotlinIntList, typeVar.ptr.reinterpret())
        val jsType = typeVar.value.toUInt()
        if (jsType == napi_valuetype.napi_null.value || jsType == napi_valuetype.napi_undefined.value) {
            return null
        }
        
        val isArray = alloc<BooleanVar>()
        napi_is_array(env, this@toKotlinIntList, isArray.ptr)
        if (!isArray.value) return emptyList()

        val lengthVar = alloc<uint32_tVar>()
        napi_get_array_length(env, this@toKotlinIntList, lengthVar.ptr)
        val length = lengthVar.value.toInt()
        
        val list = mutableListOf<Int>()
        for (i in 0 until length) {
            val elementVar = alloc<napi_valueVar>()
            napi_get_element(env, this@toKotlinIntList, i.convert(), elementVar.ptr)
            list.add(elementVar.value!!.toKotlinInt(env) ?: 0)
        }
        return list
    }
}

// List<Double>? -> napi_value
@OptIn(ExperimentalForeignApi::class)
fun List<Double>?.toNapiValueDoubleList(env: napi_env): napi_value {
    if (this == null) {
        memScoped {
            val nullVar = alloc<napi_valueVar>()
            napi_get_null(env, nullVar.ptr)
            return nullVar.value!!
        }
    }
    memScoped {
        val arrayVar = alloc<napi_valueVar>()
        napi_create_array_with_length(env, this@toNapiValueDoubleList.size.convert(), arrayVar.ptr)
        val jsArray = arrayVar.value!!
        
        for ((index, item) in this@toNapiValueDoubleList.withIndex()) {
            val jsItem = item.toNapiValue(env)
            napi_set_element(env, jsArray, index.convert(), jsItem)
        }
        return jsArray
    }
}

// napi_value -> List<Double>?
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinDoubleList(env: napi_env): List<Double>? {
    memScoped {
        val typeVar = alloc<IntVar>()
        napi_typeof(env, this@toKotlinDoubleList, typeVar.ptr.reinterpret())
        val jsType = typeVar.value.toUInt()
        if (jsType == napi_valuetype.napi_null.value || jsType == napi_valuetype.napi_undefined.value) {
            return null
        }
        
        val isArray = alloc<BooleanVar>()
        napi_is_array(env, this@toKotlinDoubleList, isArray.ptr)
        if (!isArray.value) return emptyList()

        val lengthVar = alloc<uint32_tVar>()
        napi_get_array_length(env, this@toKotlinDoubleList, lengthVar.ptr)
        val length = lengthVar.value.toInt()
        
        val list = mutableListOf<Double>()
        for (i in 0 until length) {
            val elementVar = alloc<napi_valueVar>()
            napi_get_element(env, this@toKotlinDoubleList, i.convert(), elementVar.ptr)
            list.add(elementVar.value!!.toKotlinDouble(env) ?: 0.0)
        }
        return list
    }
}

// List<Boolean>? -> napi_value
@OptIn(ExperimentalForeignApi::class)
fun List<Boolean>?.toNapiValueBooleanList(env: napi_env): napi_value {
    if (this == null) {
        memScoped {
            val nullVar = alloc<napi_valueVar>()
            napi_get_null(env, nullVar.ptr)
            return nullVar.value!!
        }
    }
    memScoped {
        val arrayVar = alloc<napi_valueVar>()
        napi_create_array_with_length(env, this@toNapiValueBooleanList.size.convert(), arrayVar.ptr)
        val jsArray = arrayVar.value!!
        
        for ((index, item) in this@toNapiValueBooleanList.withIndex()) {
            val jsItem = item.toNapiValue(env)
            napi_set_element(env, jsArray, index.convert(), jsItem)
        }
        return jsArray
    }
}

// napi_value -> List<Boolean>?
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinBooleanList(env: napi_env): List<Boolean>? {
    memScoped {
        val typeVar = alloc<IntVar>()
        napi_typeof(env, this@toKotlinBooleanList, typeVar.ptr.reinterpret())
        val jsType = typeVar.value.toUInt()
        if (jsType == napi_valuetype.napi_null.value || jsType == napi_valuetype.napi_undefined.value) {
            return null
        }
        
        val isArray = alloc<BooleanVar>()
        napi_is_array(env, this@toKotlinBooleanList, isArray.ptr)
        if (!isArray.value) return emptyList()

        val lengthVar = alloc<uint32_tVar>()
        napi_get_array_length(env, this@toKotlinBooleanList, lengthVar.ptr)
        val length = lengthVar.value.toInt()
        
        val list = mutableListOf<Boolean>()
        for (i in 0 until length) {
            val elementVar = alloc<napi_valueVar>()
            napi_get_element(env, this@toKotlinBooleanList, i.convert(), elementVar.ptr)
            list.add(elementVar.value!!.toKotlinBoolean(env) ?: false)
        }
        return list
    }
}

// --- Generic Objects, Enums and Nullables Lists / Maps ---

// napi_value -> List<Enum>?
@OptIn(ExperimentalForeignApi::class)
inline fun <reified T : Enum<T>> napi_value.toKotlinEnumList(env: napi_env): List<T>? {
    memScoped {
        val typeVar = alloc<IntVar>()
        napi_typeof(env, this@toKotlinEnumList, typeVar.ptr.reinterpret())
        val jsType = typeVar.value.toUInt()
        if (jsType == napi_valuetype.napi_null.value || jsType == napi_valuetype.napi_undefined.value) {
            return null
        }
        
        val isArray = alloc<BooleanVar>()
        napi_is_array(env, this@toKotlinEnumList, isArray.ptr)
        if (!isArray.value) return emptyList()

        val lengthVar = alloc<uint32_tVar>()
        napi_get_array_length(env, this@toKotlinEnumList, lengthVar.ptr)
        val length = lengthVar.value.toInt()
        
        val list = mutableListOf<T>()
        for (i in 0 until length) {
            val elementVar = alloc<napi_valueVar>()
            napi_get_element(env, this@toKotlinEnumList, i.convert(), elementVar.ptr)
            val enumVal = elementVar.value!!.toKotlinEnum<T>(env)
            if (enumVal != null) {
                list.add(enumVal)
            }
        }
        return list
    }
}

// List<Enum>? -> napi_value
@OptIn(ExperimentalForeignApi::class)
inline fun <reified T : Enum<T>> List<T>?.toNapiValueEnumList(env: napi_env): napi_value {
    if (this == null) {
        memScoped {
            val nullVar = alloc<napi_valueVar>()
            napi_get_null(env, nullVar.ptr)
            return nullVar.value!!
        }
    }
    memScoped {
        val arrayVar = alloc<napi_valueVar>()
        napi_create_array_with_length(env, this@toNapiValueEnumList.size.convert(), arrayVar.ptr)
        val jsArray = arrayVar.value!!
        
        for ((index, item) in this@toNapiValueEnumList.withIndex()) {
            val jsItem = item.toNapiString(env)
            napi_set_element(env, jsArray, index.convert(), jsItem)
        }
        return jsArray
    }
}

// napi_value -> List<Object>?
@OptIn(ExperimentalForeignApi::class)
inline fun <reified T> napi_value.toKotlinObjectList(env: napi_env): List<T>? {
    memScoped {
        val typeVar = alloc<IntVar>()
        napi_typeof(env, this@toKotlinObjectList, typeVar.ptr.reinterpret())
        val jsType = typeVar.value.toUInt()
        if (jsType == napi_valuetype.napi_null.value || jsType == napi_valuetype.napi_undefined.value) {
            return null
        }
        
        val isArray = alloc<BooleanVar>()
        napi_is_array(env, this@toKotlinObjectList, isArray.ptr)
        if (!isArray.value) return emptyList()

        val lengthVar = alloc<uint32_tVar>()
        napi_get_array_length(env, this@toKotlinObjectList, lengthVar.ptr)
        val length = lengthVar.value.toInt()
        
        val list = mutableListOf<T>()
        for (i in 0 until length) {
            val elementVar = alloc<napi_valueVar>()
            napi_get_element(env, this@toKotlinObjectList, i.convert(), elementVar.ptr)
            val objVal = elementVar.value!!.toKotlinObject<T>(env)
            if (objVal != null) {
                list.add(objVal)
            }
        }
        return list
    }
}

// List<Object>? -> napi_value
@OptIn(ExperimentalForeignApi::class)
inline fun <reified T> List<T>?.toNapiValueObjectList(env: napi_env): napi_value {
    if (this == null) {
        memScoped {
            val nullVar = alloc<napi_valueVar>()
            napi_get_null(env, nullVar.ptr)
            return nullVar.value!!
        }
    }
    memScoped {
        val arrayVar = alloc<napi_valueVar>()
        napi_create_array_with_length(env, this@toNapiValueObjectList.size.convert(), arrayVar.ptr)
        val jsArray = arrayVar.value!!
        
        for ((index, item) in this@toNapiValueObjectList.withIndex()) {
            val jsItem = item?.toNapiObject(env) ?: run {
                val nullVar = alloc<napi_valueVar>()
                napi_get_null(env, nullVar.ptr)
                nullVar.value!!
            }
            napi_set_element(env, jsArray, index.convert(), jsItem)
        }
        return jsArray
    }
}

// napi_value -> Map<String, Object>?
@OptIn(ExperimentalForeignApi::class)
inline fun <reified T : Any> napi_value.toKotlinStringObjectMap(env: napi_env): Map<String, T>? {
    memScoped {
        val typeVar = alloc<IntVar>()
        napi_typeof(env, this@toKotlinStringObjectMap, typeVar.ptr.reinterpret())
        val jsType = typeVar.value.toUInt()
        if (jsType == napi_valuetype.napi_null.value || jsType == napi_valuetype.napi_undefined.value) {
            return null
        }
        
        val map = mutableMapOf<String, T>()
        val keysVar = alloc<napi_valueVar>()
        
        napi_get_property_names(env, this@toKotlinStringObjectMap, keysVar.ptr)
        val keysArray = keysVar.value!!
        
        val lengthVar = alloc<uint32_tVar>()
        napi_get_array_length(env, keysArray, lengthVar.ptr)
        val length = lengthVar.value.toInt()
        
        for (i in 0 until length) {
            val keyVar = alloc<napi_valueVar>()
            napi_get_element(env, keysArray, i.convert(), keyVar.ptr)
            val keyStr = keyVar.value!!.toKotlinString(env) ?: continue
            
            val valueVar = alloc<napi_valueVar>()
            napi_get_named_property(env, this@toKotlinStringObjectMap, keyStr, valueVar.ptr)
            val valueObj = valueVar.value!!.toKotlinObject<T>(env)
            if (valueObj != null) {
                map[keyStr] = valueObj
            }
        }
        return map
    }
}

// Map<String, Object>? -> napi_value
@OptIn(ExperimentalForeignApi::class)
inline fun <reified T : Any> Map<String, T>?.toNapiValueStringObjectMap(env: napi_env): napi_value {
    if (this == null) {
        memScoped {
            val nullVar = alloc<napi_valueVar>()
            napi_get_null(env, nullVar.ptr)
            return nullVar.value!!
        }
    }
    memScoped {
        val objVar = alloc<napi_valueVar>()
        napi_create_object(env, objVar.ptr)
        val jsObj = objVar.value!!
        
        for ((key, value) in this@toNapiValueStringObjectMap) {
            val jsValue = value?.toNapiObject(env) ?: run {
                val nullVar = alloc<napi_valueVar>()
                napi_get_null(env, nullVar.ptr)
                nullVar.value!!
            }
            napi_set_named_property(env, jsObj, key, jsValue)
        }
        return jsObj
    }
}

// napi_value -> Map<String, Enum>?
@OptIn(ExperimentalForeignApi::class)
inline fun <reified T : Enum<T>> napi_value.toKotlinStringEnumMap(env: napi_env): Map<String, T>? {
    memScoped {
        val typeVar = alloc<IntVar>()
        napi_typeof(env, this@toKotlinStringEnumMap, typeVar.ptr.reinterpret())
        val jsType = typeVar.value.toUInt()
        if (jsType == napi_valuetype.napi_null.value || jsType == napi_valuetype.napi_undefined.value) {
            return null
        }
        
        val map = mutableMapOf<String, T>()
        val keysVar = alloc<napi_valueVar>()
        
        napi_get_property_names(env, this@toKotlinStringEnumMap, keysVar.ptr)
        val keysArray = keysVar.value!!
        
        val lengthVar = alloc<uint32_tVar>()
        napi_get_array_length(env, keysArray, lengthVar.ptr)
        val length = lengthVar.value.toInt()
        
        for (i in 0 until length) {
            val keyVar = alloc<napi_valueVar>()
            napi_get_element(env, keysArray, i.convert(), keyVar.ptr)
            val keyStr = keyVar.value!!.toKotlinString(env) ?: continue
            
            val valueVar = alloc<napi_valueVar>()
            napi_get_named_property(env, this@toKotlinStringEnumMap, keyStr, valueVar.ptr)
            val valueEnum = valueVar.value!!.toKotlinEnum<T>(env)
            if (valueEnum != null) {
                map[keyStr] = valueEnum
            }
        }
        return map
    }
}

// Map<String, Enum>? -> napi_value
@OptIn(ExperimentalForeignApi::class)
inline fun <reified T : Enum<T>> Map<String, T>?.toNapiValueStringEnumMap(env: napi_env): napi_value {
    if (this == null) {
        memScoped {
            val nullVar = alloc<napi_valueVar>()
            napi_get_null(env, nullVar.ptr)
            return nullVar.value!!
        }
    }
    memScoped {
        val objVar = alloc<napi_valueVar>()
        napi_create_object(env, objVar.ptr)
        val jsObj = objVar.value!!
        
        for ((key, value) in this@toNapiValueStringEnumMap) {
            val jsValue = value.toNapiString(env)
            napi_set_named_property(env, jsObj, key, jsValue)
        }
        return jsObj
    }
}

// Map<String, Int>? -> napi_value
@OptIn(ExperimentalForeignApi::class)
fun Map<String, Int>?.toNapiValueStringIntMap(env: napi_env): napi_value {
    if (this == null) {
        memScoped {
            val nullVar = alloc<napi_valueVar>()
            napi_get_null(env, nullVar.ptr)
            return nullVar.value!!
        }
    }
    memScoped {
        val objVar = alloc<napi_valueVar>()
        napi_create_object(env, objVar.ptr)
        val jsObj = objVar.value!!
        
        for ((key, value) in this@toNapiValueStringIntMap) {
            val jsValue = value.toNapiValue(env)
            napi_set_named_property(env, jsObj, key, jsValue)
        }
        return jsObj
    }
}

// napi_value -> Map<String, Int>?
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinStringIntMap(env: napi_env): Map<String, Int>? {
    memScoped {
        val typeVar = alloc<IntVar>()
        napi_typeof(env, this@toKotlinStringIntMap, typeVar.ptr.reinterpret())
        val jsType = typeVar.value.toUInt()
        if (jsType == napi_valuetype.napi_null.value || jsType == napi_valuetype.napi_undefined.value) {
            return null
        }
        
        val map = mutableMapOf<String, Int>()
        val keysVar = alloc<napi_valueVar>()
        
        napi_get_property_names(env, this@toKotlinStringIntMap, keysVar.ptr)
        val keysArray = keysVar.value!!
        
        val lengthVar = alloc<uint32_tVar>()
        napi_get_array_length(env, keysArray, lengthVar.ptr)
        val length = lengthVar.value.toInt()
        
        for (i in 0 until length) {
            val keyVar = alloc<napi_valueVar>()
            napi_get_element(env, keysArray, i.convert(), keyVar.ptr)
            val keyStr = keyVar.value!!.toKotlinString(env) ?: continue
            
            val valueVar = alloc<napi_valueVar>()
            napi_get_named_property(env, this@toKotlinStringIntMap, keyStr, valueVar.ptr)
            val valueInt = valueVar.value!!.toKotlinInt(env) ?: 0
            
            map[keyStr] = valueInt
        }
        return map
    }
}

// Map<String, Double>? -> napi_value
@OptIn(ExperimentalForeignApi::class)
fun Map<String, Double>?.toNapiValueStringDoubleMap(env: napi_env): napi_value {
    if (this == null) {
        memScoped {
            val nullVar = alloc<napi_valueVar>()
            napi_get_null(env, nullVar.ptr)
            return nullVar.value!!
        }
    }
    memScoped {
        val objVar = alloc<napi_valueVar>()
        napi_create_object(env, objVar.ptr)
        val jsObj = objVar.value!!
        
        for ((key, value) in this@toNapiValueStringDoubleMap) {
            val jsValue = value.toNapiValue(env)
            napi_set_named_property(env, jsObj, key, jsValue)
        }
        return jsObj
    }
}

// napi_value -> Map<String, Double>?
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinStringDoubleMap(env: napi_env): Map<String, Double>? {
    memScoped {
        val typeVar = alloc<IntVar>()
        napi_typeof(env, this@toKotlinStringDoubleMap, typeVar.ptr.reinterpret())
        val jsType = typeVar.value.toUInt()
        if (jsType == napi_valuetype.napi_null.value || jsType == napi_valuetype.napi_undefined.value) {
            return null
        }
        
        val map = mutableMapOf<String, Double>()
        val keysVar = alloc<napi_valueVar>()
        
        napi_get_property_names(env, this@toKotlinStringDoubleMap, keysVar.ptr)
        val keysArray = keysVar.value!!
        
        val lengthVar = alloc<uint32_tVar>()
        napi_get_array_length(env, keysArray, lengthVar.ptr)
        val length = lengthVar.value.toInt()
        
        for (i in 0 until length) {
            val keyVar = alloc<napi_valueVar>()
            napi_get_element(env, keysArray, i.convert(), keyVar.ptr)
            val keyStr = keyVar.value!!.toKotlinString(env) ?: continue
            
            val valueVar = alloc<napi_valueVar>()
            napi_get_named_property(env, this@toKotlinStringDoubleMap, keyStr, valueVar.ptr)
            val valueDouble = valueVar.value!!.toKotlinDouble(env) ?: 0.0
            
            map[keyStr] = valueDouble
        }
        return map
    }
}

// Map<String, Boolean>? -> napi_value
@OptIn(ExperimentalForeignApi::class)
fun Map<String, Boolean>?.toNapiValueStringBooleanMap(env: napi_env): napi_value {
    if (this == null) {
        memScoped {
            val nullVar = alloc<napi_valueVar>()
            napi_get_null(env, nullVar.ptr)
            return nullVar.value!!
        }
    }
    memScoped {
        val objVar = alloc<napi_valueVar>()
        napi_create_object(env, objVar.ptr)
        val jsObj = objVar.value!!
        
        for ((key, value) in this@toNapiValueStringBooleanMap) {
            val jsValue = value.toNapiValue(env)
            napi_set_named_property(env, jsObj, key, jsValue)
        }
        return jsObj
    }
}

// napi_value -> Map<String, Boolean>?
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinStringBooleanMap(env: napi_env): Map<String, Boolean>? {
    memScoped {
        val typeVar = alloc<IntVar>()
        napi_typeof(env, this@toKotlinStringBooleanMap, typeVar.ptr.reinterpret())
        val jsType = typeVar.value.toUInt()
        if (jsType == napi_valuetype.napi_null.value || jsType == napi_valuetype.napi_undefined.value) {
            return null
        }
        
        val map = mutableMapOf<String, Boolean>()
        val keysVar = alloc<napi_valueVar>()
        
        napi_get_property_names(env, this@toKotlinStringBooleanMap, keysVar.ptr)
        val keysArray = keysVar.value!!
        
        val lengthVar = alloc<uint32_tVar>()
        napi_get_array_length(env, keysArray, lengthVar.ptr)
        val length = lengthVar.value.toInt()
        
        for (i in 0 until length) {
            val keyVar = alloc<napi_valueVar>()
            napi_get_element(env, keysArray, i.convert(), keyVar.ptr)
            val keyStr = keyVar.value!!.toKotlinString(env) ?: continue
            
            val valueVar = alloc<napi_valueVar>()
            napi_get_named_property(env, this@toKotlinStringBooleanMap, keyStr, valueVar.ptr)
            val valueBool = valueVar.value!!.toKotlinBoolean(env) ?: false
            
            map[keyStr] = valueBool
        }
        return map
    }
}

// ---- JSON Serialization Bridge ----

@OptIn(ExperimentalForeignApi::class)
fun String.toNapiObject(env: napi_env): napi_value {
    memScoped {
        val globalVar = alloc<napi_valueVar>()
        napi_get_global(env, globalVar.ptr)
        
        val jsonVar = alloc<napi_valueVar>()
        napi_get_named_property(env, globalVar.value!!, "JSON", jsonVar.ptr)
        
        val parseVar = alloc<napi_valueVar>()
        napi_get_named_property(env, jsonVar.value!!, "parse", parseVar.ptr)
        
        val args = allocArray<napi_valueVar>(1)
        args[0] = this@toNapiObject.toNapiValue(env)
        
        val resultVar = alloc<napi_valueVar>()
        napi_call_function(env, jsonVar.value!!, parseVar.value!!, 1.convert(), args, resultVar.ptr)
        
        return resultVar.value!!
    }
}

@OptIn(ExperimentalForeignApi::class)
fun napi_value.toJsonString(env: napi_env): String {
    memScoped {
        val globalVar = alloc<napi_valueVar>()
        napi_get_global(env, globalVar.ptr)
        
        val jsonVar = alloc<napi_valueVar>()
        napi_get_named_property(env, globalVar.value!!, "JSON", jsonVar.ptr)
        
        val stringifyVar = alloc<napi_valueVar>()
        napi_get_named_property(env, jsonVar.value!!, "stringify", stringifyVar.ptr)
        
        val args = allocArray<napi_valueVar>(1)
        args[0] = this@toJsonString
        
        val resultVar = alloc<napi_valueVar>()
        napi_call_function(env, jsonVar.value!!, stringifyVar.value!!, 1.convert(), args, resultVar.ptr)
        
        return resultVar.value!!.toKotlinString(env) ?: ""
    }
}

@OptIn(ExperimentalForeignApi::class)
inline fun <reified T> napi_value.toKotlinObject(env: napi_env): T? {
    memScoped {
        val typeVar = alloc<IntVar>()
        napi_typeof(env, this@toKotlinObject, typeVar.ptr.reinterpret())
        val jsType = typeVar.value.toUInt()
        if (jsType == napi_valuetype.napi_null.value || jsType == napi_valuetype.napi_undefined.value) {
            return null
        }
    }
    val jsonStr = this.toJsonString(env)
    try {
        return Json { ignoreUnknownKeys = true }.decodeFromString<T>(jsonStr)
    } catch (e: Throwable) {
        val errorMsg = "toKotlinObject failed for JSON: $jsonStr, Error: ${e.message}"
        println(errorMsg)
        throw e
    }
}

@OptIn(ExperimentalForeignApi::class)
inline fun <reified T> T.toNapiObject(env: napi_env): napi_value {
    try {
        val jsonStr = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }.encodeToString(this)
        return jsonStr.toNapiObject(env)
    } catch (e: Throwable) {
        val errorMsg = "toNapiObject failed for object: $this, Error: ${e.message}"
        println(errorMsg)
        throw e
    }
}

@OptIn(ExperimentalForeignApi::class)
inline fun <reified T : Enum<T>> napi_value.toKotlinEnum(env: napi_env): T? {
    memScoped {
        val typeVar = alloc<IntVar>()
        napi_typeof(env, this@toKotlinEnum, typeVar.ptr.reinterpret())
        val jsType = typeVar.value.toUInt()
        if (jsType == napi_valuetype.napi_null.value || jsType == napi_valuetype.napi_undefined.value) {
            return null
        }
    }
    val enumName = this.toKotlinString(env) ?: throw IllegalArgumentException("Expected string for Enum name, but got null")
    return enumValueOf<T>(enumName)
}

@OptIn(ExperimentalForeignApi::class)
inline fun <reified T : Enum<T>> T.toNapiString(env: napi_env): napi_value = this.name.toNapiValue(env)

@OptIn(ExperimentalForeignApi::class)
object ConstructorRegistry {
    val refs = mutableMapOf<String, napi_ref>()
}

@OptIn(ExperimentalForeignApi::class)
fun Any.toNapiWrappedObject(env: napi_env, className: String): napi_value {
    memScoped {
        val constructorRef = ConstructorRegistry.refs[className] 
            ?: throw IllegalArgumentException("Constructor for $className not found")
        val constructorVar = alloc<napi_valueVar>()
        napi_get_reference_value(env, constructorRef, constructorVar.ptr)
        
        val stableRef = StableRef.create(this@toNapiWrappedObject)
        val externalPtr = alloc<napi_valueVar>()
        napi_create_external(env, stableRef.asCPointer(), null, null, externalPtr.ptr)
        
        val args = allocArray<napi_valueVar>(1)
        args[0] = externalPtr.value!!
        
        val jsInstanceVar = alloc<napi_valueVar>()
        napi_new_instance(env, constructorVar.value!!, 1u.convert(), args, jsInstanceVar.ptr)
        
        return jsInstanceVar.value!!
    }
}

@OptIn(ExperimentalForeignApi::class)
inline fun <reified T : Any> napi_value.unwrapKotlinObject(env: napi_env): T? {
    memScoped {
        val typeVar = alloc<IntVar>()
        napi_typeof(env, this@unwrapKotlinObject, typeVar.ptr.reinterpret())
        val jsType = typeVar.value.toUInt()
        if (jsType == napi_valuetype.napi_null.value || jsType == napi_valuetype.napi_undefined.value) {
            return null
        }
        
        val nativeObjVar = alloc<COpaquePointerVar>()
        napi_unwrap(env, this@unwrapKotlinObject, nativeObjVar.ptr)
        val nativeObj = nativeObjVar.value 
            ?: throw IllegalArgumentException("napi_unwrap failed: native object is null")
        val stableRef = nativeObj.asStableRef<T>()
        return stableRef.get()
    }
}

// Exception / Error -> napi_value
@OptIn(ExperimentalForeignApi::class)
fun Throwable.toNapiError(env: napi_env): napi_value {
    memScoped {
        val codeStr = alloc<napi_valueVar>()
        napi_create_string_utf8(env, this@toNapiError::class.simpleName ?: "Error", napi.NAPI_AUTO_LENGTH.convert(), codeStr.ptr)
        
        val msgStr = alloc<napi_valueVar>()
        val msg = this@toNapiError.message ?: "Unknown Kotlin Error"
        napi_create_string_utf8(env, msg, napi.NAPI_AUTO_LENGTH.convert(), msgStr.ptr)
        
        val errObj = alloc<napi_valueVar>()
        napi_create_error(env, codeStr.value, msgStr.value, errObj.ptr)
        return errObj.value!!
    }
}
