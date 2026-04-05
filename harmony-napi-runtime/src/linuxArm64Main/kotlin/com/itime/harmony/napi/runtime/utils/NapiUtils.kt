package com.itime.harmony.napi.runtime.utils

import kotlinx.cinterop.*
import napi.*
import platform.posix.uint32_tVar
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

// String -> napi_value
@OptIn(ExperimentalForeignApi::class)
fun String.toNapiValue(env: napi_env): napi_value {
    memScoped {
        val result = alloc<napi_valueVar>()
        // cinterop 默认把 const char* 映射成了 Kotlin 的 String 类型
        // 所以这里直接传 this@toNapiValue 即可，不需要 .cstr.ptr
        napi_create_string_utf8(env, this@toNapiValue, napi.NAPI_AUTO_LENGTH.convert(), result.ptr)
        return result.value!!
    }
}

// napi_value -> String
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinString(env: napi_env): String {
    memScoped {
        val lengthVar = alloc<size_tVar>()
        // 第一次调用，传入 null 缓冲区，旨在获取字符串所需字节长度
        napi_get_value_string_utf8(env, this@toKotlinString, null, 0u.convert(), lengthVar.ptr)
        
        val length = lengthVar.value.toInt()
        // 分配 Kotlin 原生 ByteArray
        val buffer = ByteArray(length + 1)
        
        // 第二次调用，使用 refTo(0) 传递 CValuesRef<ByteVar>
        napi_get_value_string_utf8(env, this@toKotlinString, buffer.refTo(0), (length + 1).convert(), null)
        
        // 将 ByteArray 解码为 Kotlin String，舍弃末尾的 \0
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

// napi_value -> Double
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinDouble(env: napi_env): Double {
    memScoped {
        val result = alloc<DoubleVar>()
        napi_get_value_double(env, this@toKotlinDouble, result.ptr)
        return result.value
    }
}

// List<String> -> napi_value
@OptIn(ExperimentalForeignApi::class)
fun List<String>.toNapiValue(env: napi_env): napi_value {
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

// napi_value -> List<String>
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinStringList(env: napi_env): List<String> {
    memScoped {
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
            list.add(elementVar.value!!.toKotlinString(env))
        }
        return list
    }
}

// Map<String, String> -> napi_value
@OptIn(ExperimentalForeignApi::class)
fun Map<String, String>.toNapiValue(env: napi_env): napi_value {
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

// napi_value -> Map<String, String>
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinStringStringMap(env: napi_env): Map<String, String> {
    memScoped {
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
            val keyStr = keyVar.value!!.toKotlinString(env)
            
            val valueVar = alloc<napi_valueVar>()
            napi_get_named_property(env, this@toKotlinStringStringMap, keyStr, valueVar.ptr)
            val valueStr = valueVar.value!!.toKotlinString(env)
            
            map[keyStr] = valueStr
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

// napi_value -> Boolean
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinBoolean(env: napi_env): Boolean {
    memScoped {
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

// napi_value -> Int
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinInt(env: napi_env): Int {
    memScoped {
        val result = alloc<IntVar>()
        napi_get_value_int32(env, this@toKotlinInt, result.ptr)
        return result.value
    }
}

// Any? -> napi_value
@OptIn(ExperimentalForeignApi::class)
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
        else -> throw IllegalArgumentException("Unsupported type for Any?.toNapiValue: ${this::class}")
    }
}

// napi_value -> Any?
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinAny(env: napi_env): Any? {
    memScoped {
        val typeVar = alloc<napi_valuetype.Var>()
        napi_typeof(env, this@toKotlinAny, typeVar.ptr)
        return when (typeVar.value) {
            napi_valuetype.napi_undefined, napi_valuetype.napi_null -> null
            napi_valuetype.napi_boolean -> this@toKotlinAny.toKotlinBoolean(env)
            napi_valuetype.napi_number -> this@toKotlinAny.toKotlinDouble(env) // JS numbers are double
            napi_valuetype.napi_string -> this@toKotlinAny.toKotlinString(env)
            napi_valuetype.napi_object -> {
                val isArray = alloc<BooleanVar>()
                napi_is_array(env, this@toKotlinAny, isArray.ptr)
                if (isArray.value) {
                    this@toKotlinAny.toKotlinAnyList(env)
                } else {
                    this@toKotlinAny.toKotlinStringAnyMap(env)
                }
            }
            else -> throw IllegalArgumentException("Unsupported napi_valuetype: ${typeVar.value}")
        }
    }
}

// List<Any?> -> napi_value
@OptIn(ExperimentalForeignApi::class)
fun List<Any?>.toNapiValueAnyList(env: napi_env): napi_value {
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

// napi_value -> List<Any?>
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinAnyList(env: napi_env): List<Any?> {
    memScoped {
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

// Map<String, Any?> -> napi_value
@OptIn(ExperimentalForeignApi::class)
fun Map<String, Any?>.toNapiValueStringAnyMap(env: napi_env): napi_value {
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

// napi_value -> Map<String, Any?>
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinStringAnyMap(env: napi_env): Map<String, Any?> {
    memScoped {
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
            val keyStr = keyVar.value!!.toKotlinString(env)
            
            val valueVar = alloc<napi_valueVar>()
            napi_get_named_property(env, this@toKotlinStringAnyMap, keyStr, valueVar.ptr)
            val valueAny = valueVar.value!!.toKotlinAny(env)
            
            map[keyStr] = valueAny
        }
        return map
    }
}

// ---- Primitive Specific Lists and Maps ----

// List<Int> -> napi_value
@OptIn(ExperimentalForeignApi::class)
fun List<Int>.toNapiValueIntList(env: napi_env): napi_value {
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

// napi_value -> List<Int>
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinIntList(env: napi_env): List<Int> {
    memScoped {
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
            list.add(elementVar.value!!.toKotlinInt(env))
        }
        return list
    }
}

// List<Double> -> napi_value
@OptIn(ExperimentalForeignApi::class)
fun List<Double>.toNapiValueDoubleList(env: napi_env): napi_value {
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

// napi_value -> List<Double>
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinDoubleList(env: napi_env): List<Double> {
    memScoped {
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
            list.add(elementVar.value!!.toKotlinDouble(env))
        }
        return list
    }
}

// List<Boolean> -> napi_value
@OptIn(ExperimentalForeignApi::class)
fun List<Boolean>.toNapiValueBooleanList(env: napi_env): napi_value {
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

// napi_value -> List<Boolean>
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinBooleanList(env: napi_env): List<Boolean> {
    memScoped {
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
            list.add(elementVar.value!!.toKotlinBoolean(env))
        }
        return list
    }
}

// Map<String, Int> -> napi_value
@OptIn(ExperimentalForeignApi::class)
fun Map<String, Int>.toNapiValueStringIntMap(env: napi_env): napi_value {
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

// napi_value -> Map<String, Int>
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinStringIntMap(env: napi_env): Map<String, Int> {
    memScoped {
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
            val keyStr = keyVar.value!!.toKotlinString(env)
            
            val valueVar = alloc<napi_valueVar>()
            napi_get_named_property(env, this@toKotlinStringIntMap, keyStr, valueVar.ptr)
            val valueInt = valueVar.value!!.toKotlinInt(env)
            
            map[keyStr] = valueInt
        }
        return map
    }
}

// Map<String, Double> -> napi_value
@OptIn(ExperimentalForeignApi::class)
fun Map<String, Double>.toNapiValueStringDoubleMap(env: napi_env): napi_value {
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

// napi_value -> Map<String, Double>
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinStringDoubleMap(env: napi_env): Map<String, Double> {
    memScoped {
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
            val keyStr = keyVar.value!!.toKotlinString(env)
            
            val valueVar = alloc<napi_valueVar>()
            napi_get_named_property(env, this@toKotlinStringDoubleMap, keyStr, valueVar.ptr)
            val valueDouble = valueVar.value!!.toKotlinDouble(env)
            
            map[keyStr] = valueDouble
        }
        return map
    }
}

// Map<String, Boolean> -> napi_value
@OptIn(ExperimentalForeignApi::class)
fun Map<String, Boolean>.toNapiValueStringBooleanMap(env: napi_env): napi_value {
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

// napi_value -> Map<String, Boolean>
@OptIn(ExperimentalForeignApi::class)
fun napi_value.toKotlinStringBooleanMap(env: napi_env): Map<String, Boolean> {
    memScoped {
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
            val keyStr = keyVar.value!!.toKotlinString(env)
            
            val valueVar = alloc<napi_valueVar>()
            napi_get_named_property(env, this@toKotlinStringBooleanMap, keyStr, valueVar.ptr)
            val valueBool = valueVar.value!!.toKotlinBoolean(env)
            
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
        
        return resultVar.value!!.toKotlinString(env)
    }
}

@OptIn(ExperimentalForeignApi::class)
inline fun <reified T> napi_value.toKotlinObject(env: napi_env): T {
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
        val jsonStr = Json { ignoreUnknownKeys = true }.encodeToString(this)
        return jsonStr.toNapiObject(env)
    } catch (e: Throwable) {
        val errorMsg = "toNapiObject failed for object: $this, Error: ${e.message}"
        println(errorMsg)
        throw e
    }
}

@OptIn(ExperimentalForeignApi::class)
inline fun <reified T : Enum<T>> napi_value.toKotlinEnum(env: napi_env): T = enumValueOf<T>(this.toKotlinString(env))

@OptIn(ExperimentalForeignApi::class)
inline fun <reified T : Enum<T>> T.toNapiString(env: napi_env): napi_value = this.name.toNapiValue(env)

