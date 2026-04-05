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
import com.itime.harmony.napi.runtime.utils.toNapiWrappedObject
import com.itime.harmony.napi.runtime.utils.unwrapKotlinObject
import com.itime.harmony.sample.getFullName
import kotlin.OptIn
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ExperimentalForeignApi
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
import napi.napi_callback_info
import napi.napi_env
import napi.napi_get_cb_info
import napi.napi_get_value_external
import napi.napi_typeof
import napi.napi_value
import napi.napi_valueVar
import napi.napi_valuetype
import napi.napi_wrap
import platform.posix.size_tVar

public fun UserUtilsV2_getFullName_wrapper(env: napi_env?, info: napi_callback_info?): napi_value? =
    try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        napi_get_cb_info(env, info, argc.ptr, argv, null, null)

        val arg0 = argv[0]!!.toKotlinObject<com.itime.harmony.sample.User>(env!!)
        val result = arg0.getFullName()
        result.toNapiValue(env!!)
    }
} catch (e: Throwable) {
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}
