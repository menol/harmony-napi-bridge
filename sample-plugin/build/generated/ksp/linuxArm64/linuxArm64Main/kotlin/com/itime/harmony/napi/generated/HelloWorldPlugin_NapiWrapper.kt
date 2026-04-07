@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.itime.harmony.napi.generated

import com.itime.harmony.napi.runtime.utils.launchNapiCoroutine
import com.itime.harmony.napi.runtime.utils.toKotlinAny
import com.itime.harmony.napi.runtime.utils.toKotlinAnyList
import com.itime.harmony.napi.runtime.utils.toKotlinBoolean
import com.itime.harmony.napi.runtime.utils.toKotlinBooleanList
import com.itime.harmony.napi.runtime.utils.toKotlinDouble
import com.itime.harmony.napi.runtime.utils.toKotlinDoubleList
import com.itime.harmony.napi.runtime.utils.toKotlinEnum
import com.itime.harmony.napi.runtime.utils.toKotlinEnumList
import com.itime.harmony.napi.runtime.utils.toKotlinInt
import com.itime.harmony.napi.runtime.utils.toKotlinIntList
import com.itime.harmony.napi.runtime.utils.toKotlinObject
import com.itime.harmony.napi.runtime.utils.toKotlinObjectList
import com.itime.harmony.napi.runtime.utils.toKotlinString
import com.itime.harmony.napi.runtime.utils.toKotlinStringAnyMap
import com.itime.harmony.napi.runtime.utils.toKotlinStringBooleanMap
import com.itime.harmony.napi.runtime.utils.toKotlinStringDoubleMap
import com.itime.harmony.napi.runtime.utils.toKotlinStringEnumMap
import com.itime.harmony.napi.runtime.utils.toKotlinStringIntMap
import com.itime.harmony.napi.runtime.utils.toKotlinStringList
import com.itime.harmony.napi.runtime.utils.toKotlinStringObjectMap
import com.itime.harmony.napi.runtime.utils.toKotlinStringStringMap
import com.itime.harmony.napi.runtime.utils.toNapiObject
import com.itime.harmony.napi.runtime.utils.toNapiString
import com.itime.harmony.napi.runtime.utils.toNapiValue
import com.itime.harmony.napi.runtime.utils.toNapiValueAnyList
import com.itime.harmony.napi.runtime.utils.toNapiValueBooleanList
import com.itime.harmony.napi.runtime.utils.toNapiValueDoubleList
import com.itime.harmony.napi.runtime.utils.toNapiValueEnumList
import com.itime.harmony.napi.runtime.utils.toNapiValueIntList
import com.itime.harmony.napi.runtime.utils.toNapiValueObjectList
import com.itime.harmony.napi.runtime.utils.toNapiValueStringAnyMap
import com.itime.harmony.napi.runtime.utils.toNapiValueStringBooleanMap
import com.itime.harmony.napi.runtime.utils.toNapiValueStringDoubleMap
import com.itime.harmony.napi.runtime.utils.toNapiValueStringEnumMap
import com.itime.harmony.napi.runtime.utils.toNapiValueStringIntMap
import com.itime.harmony.napi.runtime.utils.toNapiValueStringObjectMap
import com.itime.harmony.napi.runtime.utils.toNapiWrappedObject
import com.itime.harmony.napi.runtime.utils.unwrapKotlinObject
import com.itime.harmony.sample.HelloWorldPlugin
import kotlin.OptIn
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.LongVar
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.`get`
import kotlinx.cinterop.`value`
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.asStableRef
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.refTo
import kotlinx.cinterop.staticCFunction
import napi.napi_adjust_external_memory
import napi.napi_callback_info
import napi.napi_env
import napi.napi_get_cb_info
import napi.napi_get_null
import napi.napi_get_value_external
import napi.napi_typeof
import napi.napi_value
import napi.napi_valueVar
import napi.napi_valuetype
import napi.napi_wrap
import platform.posix.size_tVar

public fun HelloWorldPlugin_add_wrapper(env: napi_env?, info: napi_callback_info?): napi_value? =
    try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 2u
        val argv = allocArray<napi_valueVar>(2)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinDouble(env!!)!!
        val arg1 = argv[1]!!.toKotlinDouble(env!!)!!
        val result = HelloWorldPlugin.add(arg0, arg1)
        result.toNapiValue(env!!)
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_add_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_greet_wrapper(env: napi_env?, info: napi_callback_info?): napi_value? =
    try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinString(env!!)!!
        val result = HelloWorldPlugin.greet(arg0)
        result.toNapiValue(env!!)
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_greet_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_processList_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinStringList(env!!)!!
        val result = HelloWorldPlugin.processList(arg0)
        result.toNapiValue(env!!)
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_processList_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_processMap_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinStringStringMap(env!!)!!
        val result = HelloWorldPlugin.processMap(arg0)
        result.toNapiValue(env!!)
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_processMap_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_processAny_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinAny(env!!)
        val result = HelloWorldPlugin.processAny(arg0)
        result.toNapiValue(env!!)
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_processAny_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_processAnyMap_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinStringAnyMap(env!!)!!
        val result = HelloWorldPlugin.processAnyMap(arg0)
        result.toNapiValueStringAnyMap(env!!)
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_processAnyMap_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_processIntList_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinIntList(env!!)!!
        val result = HelloWorldPlugin.processIntList(arg0)
        result.toNapiValueIntList(env!!)
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_processIntList_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_processDoubleList_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinDoubleList(env!!)!!
        val result = HelloWorldPlugin.processDoubleList(arg0)
        result.toNapiValueDoubleList(env!!)
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_processDoubleList_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_processBooleanList_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinBooleanList(env!!)!!
        val result = HelloWorldPlugin.processBooleanList(arg0)
        result.toNapiValueBooleanList(env!!)
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_processBooleanList_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_processStringIntMap_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinStringIntMap(env!!)!!
        val result = HelloWorldPlugin.processStringIntMap(arg0)
        result.toNapiValueStringIntMap(env!!)
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_processStringIntMap_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_processStringDoubleMap_wrapper(env: napi_env?,
    info: napi_callback_info?): napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinStringDoubleMap(env!!)!!
        val result = HelloWorldPlugin.processStringDoubleMap(arg0)
        result.toNapiValueStringDoubleMap(env!!)
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_processStringDoubleMap_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_processStringBooleanMap_wrapper(env: napi_env?,
    info: napi_callback_info?): napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinStringBooleanMap(env!!)!!
        val result = HelloWorldPlugin.processStringBooleanMap(arg0)
        result.toNapiValueStringBooleanMap(env!!)
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_processStringBooleanMap_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_processUser_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 2u
        val argv = allocArray<napi_valueVar>(2)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinObject<com.itime.harmony.sample.User>(env!!)!!
        val arg1 = argv[1]!!.toKotlinEnum<com.itime.harmony.sample.Role>(env!!)!!
        val result = HelloWorldPlugin.processUser(arg0, arg1)
        result.toNapiObject(env!!)
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_processUser_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_processResult_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinObject<com.itime.harmony.sample.NetworkResult>(env!!)!!
        val result = HelloWorldPlugin.processResult(arg0)
        result.toNapiObject(env!!)
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_processResult_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_fetchDataAsync_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinString(env!!)!!
        launchNapiCoroutine(env!!) {
            val result = HelloWorldPlugin.fetchDataAsync(arg0)
            return@launchNapiCoroutine { cbEnv -> result.toNapiValue(cbEnv) }
        }
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_fetchDataAsync_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_executeMultipleTasksAsync_wrapper(env: napi_env?,
    info: napi_callback_info?): napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 2u
        val argv = allocArray<napi_valueVar>(2)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinInt(env!!)!!
        val arg1 = argv[1]!!.toKotlinInt(env!!)!!
        launchNapiCoroutine(env!!) {
            val result = HelloWorldPlugin.executeMultipleTasksAsync(arg0, arg1)
            return@launchNapiCoroutine { cbEnv -> result.toNapiValue(cbEnv) }
        }
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_executeMultipleTasksAsync_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_getTestClass_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 0u
        val argv = allocArray<napi_valueVar>(0)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val result = HelloWorldPlugin.getTestClass()
        result.toNapiWrappedObject(env!!, "TestClass")
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_getTestClass_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_processNullableString_wrapper(env: napi_env?,
    info: napi_callback_info?): napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinString(env!!)
        val result = HelloWorldPlugin.processNullableString(arg0)
        result.toNapiValue(env!!)
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_processNullableString_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_getNullableList_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinBoolean(env!!)!!
        val result = HelloWorldPlugin.getNullableList(arg0)
        result.toNapiValue(env!!)
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_getNullableList_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_processMutableList_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinStringList(env!!)?.toMutableList()!!
        val result = HelloWorldPlugin.processMutableList(arg0)
        result.toNapiValue(env!!)
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_processMutableList_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_processMutableMap_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinStringIntMap(env!!)?.toMutableMap()!!
        val result = HelloWorldPlugin.processMutableMap(arg0)
        result.toNapiValueStringIntMap(env!!)
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_processMutableMap_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_modifyMutableData_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinObject<com.itime.harmony.sample.MutableData>(env!!)!!
        val result = HelloWorldPlugin.modifyMutableData(arg0)
        result.toNapiObject(env!!)
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_modifyMutableData_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_callTestInterface_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.let { com.itime.harmony.napi.generated.TestInterface_NapiProxy(env!!,
    it) }!!
        val result = HelloWorldPlugin.callTestInterface(arg0)
        result.toNapiValue(env!!)
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_callTestInterface_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_processDataSealed_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinObject<com.itime.harmony.sample.DataSealed>(env!!)!!
        val result = HelloWorldPlugin.processDataSealed(arg0)
        result.toNapiObject(env!!)
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_processDataSealed_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_processTree_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinObject<com.itime.harmony.sample.TreeNode>(env!!)!!
        val result = HelloWorldPlugin.processTree(arg0)
        result.toNapiObject(env!!)
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_processTree_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_processTripleBox_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinObject<com.itime.harmony.sample.TripleBox<kotlin.String,
    kotlin.Int, kotlin.Boolean>>(env!!)!!
        val result = HelloWorldPlugin.processTripleBox(arg0)
        result.toNapiObject(env!!)
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_processTripleBox_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_throwKotlinException_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinString(env!!)!!
        val result = HelloWorldPlugin.throwKotlinException(arg0)
        result.toNapiValue(env!!)
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_throwKotlinException_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_processExtremeAny_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinAny(env!!)
        val result = HelloWorldPlugin.processExtremeAny(arg0)
        result.toNapiValue(env!!)
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_processExtremeAny_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun HelloWorldPlugin_extremeSuspendThrow_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinString(env!!)!!
        launchNapiCoroutine(env!!) {
            val result = HelloWorldPlugin.extremeSuspendThrow(arg0)
            return@launchNapiCoroutine { cbEnv -> result.toNapiValue(cbEnv) }
        }
    }
} catch (e: Throwable) {
    println("Error in HelloWorldPlugin_extremeSuspendThrow_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}
