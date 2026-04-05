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
import com.itime.harmony.napi.runtime.utils.unwrapKotlinObject
import com.itime.harmony.sample.DemoAbstract
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

public fun DemoAbstract_process_wrapper(env: napi_env?, info: napi_callback_info?): napi_value? =
    try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        val thisVar = alloc<napi_valueVar>()
        napi_get_cb_info(env, info, argc.ptr, argv, thisVar.ptr, null)
        val instance = thisVar.value!!.unwrapKotlinObject<DemoAbstract>(env!!)

        val arg0 = argv[0]!!.toKotlinStringList(env!!)
        val result = instance.process(arg0)
        result.toNapiValue(env!!)
    }
} catch (e: Throwable) {
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun DemoAbstract_constructor(env: napi_env?, info: napi_callback_info?): napi_value? = try {
    memScoped {
        val thisVar = alloc<napi_valueVar>()
        napi_get_cb_info(env, info, null, null, thisVar.ptr, null)
        thisVar.value
    }
} catch (e: Throwable) {
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}
