@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.itime.harmony.napi.generated

import com.itime.harmony.napi.runtime.utils.toKotlinAny
import com.itime.harmony.napi.runtime.utils.toKotlinAnyList
import com.itime.harmony.napi.runtime.utils.toKotlinBoolean
import com.itime.harmony.napi.runtime.utils.toKotlinBooleanList
import com.itime.harmony.napi.runtime.utils.toKotlinDouble
import com.itime.harmony.napi.runtime.utils.toKotlinDoubleList
import com.itime.harmony.napi.runtime.utils.toKotlinEnum
import com.itime.harmony.napi.runtime.utils.toKotlinInt
import com.itime.harmony.napi.runtime.utils.toKotlinIntList
import com.itime.harmony.napi.runtime.utils.toKotlinObject
import com.itime.harmony.napi.runtime.utils.toKotlinString
import com.itime.harmony.napi.runtime.utils.toKotlinStringAnyMap
import com.itime.harmony.napi.runtime.utils.toKotlinStringBooleanMap
import com.itime.harmony.napi.runtime.utils.toKotlinStringDoubleMap
import com.itime.harmony.napi.runtime.utils.toKotlinStringIntMap
import com.itime.harmony.napi.runtime.utils.toKotlinStringList
import com.itime.harmony.napi.runtime.utils.toKotlinStringStringMap
import com.itime.harmony.napi.runtime.utils.toNapiObject
import com.itime.harmony.napi.runtime.utils.toNapiString
import com.itime.harmony.napi.runtime.utils.toNapiValue
import com.itime.harmony.napi.runtime.utils.toNapiValueAnyList
import com.itime.harmony.napi.runtime.utils.toNapiValueBooleanList
import com.itime.harmony.napi.runtime.utils.toNapiValueDoubleList
import com.itime.harmony.napi.runtime.utils.toNapiValueIntList
import com.itime.harmony.napi.runtime.utils.toNapiValueStringAnyMap
import com.itime.harmony.napi.runtime.utils.toNapiValueStringBooleanMap
import com.itime.harmony.napi.runtime.utils.toNapiValueStringDoubleMap
import com.itime.harmony.napi.runtime.utils.toNapiValueStringIntMap
import com.itime.harmony.sample.HelloWorldPlugin
import kotlin.OptIn
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.`get`
import kotlinx.cinterop.`value`
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.refTo
import napi.napi_callback_info
import napi.napi_env
import napi.napi_get_cb_info
import napi.napi_value
import napi.napi_valueVar
import platform.posix.size_tVar

public fun HelloWorldPlugin_add_wrapper(env: napi_env?, info: napi_callback_info?): napi_value? =
    memScoped {
    val argc = alloc<size_tVar>()
    argc.value = 2u
    val argv = allocArray<napi_valueVar>(2)
    napi_get_cb_info(env, info, argc.ptr, argv, null, null)

    val arg0 = argv[0]!!.toKotlinDouble(env!!)
    val arg1 = argv[1]!!.toKotlinDouble(env!!)
    val result = HelloWorldPlugin.add(arg0, arg1)
    result.toNapiValue(env!!)
}

public fun HelloWorldPlugin_greet_wrapper(env: napi_env?, info: napi_callback_info?): napi_value? =
    memScoped {
    val argc = alloc<size_tVar>()
    argc.value = 1u
    val argv = allocArray<napi_valueVar>(1)
    napi_get_cb_info(env, info, argc.ptr, argv, null, null)

    val arg0 = argv[0]!!.toKotlinString(env!!)
    val result = HelloWorldPlugin.greet(arg0)
    result.toNapiValue(env!!)
}

public fun HelloWorldPlugin_processList_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = memScoped {
    val argc = alloc<size_tVar>()
    argc.value = 1u
    val argv = allocArray<napi_valueVar>(1)
    napi_get_cb_info(env, info, argc.ptr, argv, null, null)

    val arg0 = argv[0]!!.toKotlinStringList(env!!)
    val result = HelloWorldPlugin.processList(arg0)
    result.toNapiValue(env!!)
}

public fun HelloWorldPlugin_processMap_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = memScoped {
    val argc = alloc<size_tVar>()
    argc.value = 1u
    val argv = allocArray<napi_valueVar>(1)
    napi_get_cb_info(env, info, argc.ptr, argv, null, null)

    val arg0 = argv[0]!!.toKotlinStringStringMap(env!!)
    val result = HelloWorldPlugin.processMap(arg0)
    result.toNapiValue(env!!)
}

public fun HelloWorldPlugin_processAny_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = memScoped {
    val argc = alloc<size_tVar>()
    argc.value = 1u
    val argv = allocArray<napi_valueVar>(1)
    napi_get_cb_info(env, info, argc.ptr, argv, null, null)

    val arg0 = argv[0]!!.toKotlinAny(env!!)
    val result = HelloWorldPlugin.processAny(arg0)
    result.toNapiValue(env!!)
}

public fun HelloWorldPlugin_processAnyMap_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = memScoped {
    val argc = alloc<size_tVar>()
    argc.value = 1u
    val argv = allocArray<napi_valueVar>(1)
    napi_get_cb_info(env, info, argc.ptr, argv, null, null)

    val arg0 = argv[0]!!.toKotlinStringAnyMap(env!!)
    val result = HelloWorldPlugin.processAnyMap(arg0)
    result.toNapiValueStringAnyMap(env!!)
}

public fun HelloWorldPlugin_processIntList_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = memScoped {
    val argc = alloc<size_tVar>()
    argc.value = 1u
    val argv = allocArray<napi_valueVar>(1)
    napi_get_cb_info(env, info, argc.ptr, argv, null, null)

    val arg0 = argv[0]!!.toKotlinIntList(env!!)
    val result = HelloWorldPlugin.processIntList(arg0)
    result.toNapiValueIntList(env!!)
}

public fun HelloWorldPlugin_processDoubleList_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = memScoped {
    val argc = alloc<size_tVar>()
    argc.value = 1u
    val argv = allocArray<napi_valueVar>(1)
    napi_get_cb_info(env, info, argc.ptr, argv, null, null)

    val arg0 = argv[0]!!.toKotlinDoubleList(env!!)
    val result = HelloWorldPlugin.processDoubleList(arg0)
    result.toNapiValueDoubleList(env!!)
}

public fun HelloWorldPlugin_processBooleanList_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = memScoped {
    val argc = alloc<size_tVar>()
    argc.value = 1u
    val argv = allocArray<napi_valueVar>(1)
    napi_get_cb_info(env, info, argc.ptr, argv, null, null)

    val arg0 = argv[0]!!.toKotlinBooleanList(env!!)
    val result = HelloWorldPlugin.processBooleanList(arg0)
    result.toNapiValueBooleanList(env!!)
}

public fun HelloWorldPlugin_processStringIntMap_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = memScoped {
    val argc = alloc<size_tVar>()
    argc.value = 1u
    val argv = allocArray<napi_valueVar>(1)
    napi_get_cb_info(env, info, argc.ptr, argv, null, null)

    val arg0 = argv[0]!!.toKotlinStringIntMap(env!!)
    val result = HelloWorldPlugin.processStringIntMap(arg0)
    result.toNapiValueStringIntMap(env!!)
}

public fun HelloWorldPlugin_processStringDoubleMap_wrapper(env: napi_env?,
    info: napi_callback_info?): napi_value? = memScoped {
    val argc = alloc<size_tVar>()
    argc.value = 1u
    val argv = allocArray<napi_valueVar>(1)
    napi_get_cb_info(env, info, argc.ptr, argv, null, null)

    val arg0 = argv[0]!!.toKotlinStringDoubleMap(env!!)
    val result = HelloWorldPlugin.processStringDoubleMap(arg0)
    result.toNapiValueStringDoubleMap(env!!)
}

public fun HelloWorldPlugin_processStringBooleanMap_wrapper(env: napi_env?,
    info: napi_callback_info?): napi_value? = memScoped {
    val argc = alloc<size_tVar>()
    argc.value = 1u
    val argv = allocArray<napi_valueVar>(1)
    napi_get_cb_info(env, info, argc.ptr, argv, null, null)

    val arg0 = argv[0]!!.toKotlinStringBooleanMap(env!!)
    val result = HelloWorldPlugin.processStringBooleanMap(arg0)
    result.toNapiValueStringBooleanMap(env!!)
}

public fun HelloWorldPlugin_processUser_wrapper(env: napi_env?, info: napi_callback_info?):
    napi_value? = memScoped {
    val argc = alloc<size_tVar>()
    argc.value = 2u
    val argv = allocArray<napi_valueVar>(2)
    napi_get_cb_info(env, info, argc.ptr, argv, null, null)

    val arg0 = argv[0]!!.toKotlinObject<com.itime.harmony.sample.User>(env!!)
    val arg1 = argv[1]!!.toKotlinEnum<com.itime.harmony.sample.Role>(env!!)
    val result = HelloWorldPlugin.processUser(arg0, arg1)
    result.toNapiObject(env!!)
}
