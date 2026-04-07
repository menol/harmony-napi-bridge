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
import com.itime.harmony.sample.TestClass
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

public fun TestClass_fetchValue_wrapper(env: napi_env?, info: napi_callback_info?): napi_value? =
    try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 0u
        val argv = allocArray<napi_valueVar>(0)
        val thisVar = alloc<napi_valueVar>()
        napi_get_cb_info(env, info, argc.ptr, argv, thisVar.ptr, null)
        val instance = thisVar.value!!.unwrapKotlinObject<TestClass>(env!!)!!

        val result = instance.fetchValue()
        result.toNapiValue(env!!)
    }
} catch (e: Throwable) {
    println("Error in TestClass_fetchValue_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun TestClass_increment_wrapper(env: napi_env?, info: napi_callback_info?): napi_value? = try
    {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 0u
        val argv = allocArray<napi_valueVar>(0)
        val thisVar = alloc<napi_valueVar>()
        napi_get_cb_info(env, info, argc.ptr, argv, thisVar.ptr, null)
        val instance = thisVar.value!!.unwrapKotlinObject<TestClass>(env!!)!!

        val result = instance.increment()
        null
    }
} catch (e: Throwable) {
    println("Error in TestClass_increment_wrapper: ${e.message}")
    e.printStackTrace()
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}

public fun TestClass_finalize(
  env: napi_env?,
  `data`: COpaquePointer?,
  hint: COpaquePointer?,
) {
  data?.asStableRef<Any>()?.dispose()
  if (env != null) {
      memScoped {
          val adjustedValue = alloc<LongVar>()
          napi_adjust_external_memory(env, -8192L, adjustedValue.ptr)
      }
  }
}

public fun TestClass_constructor(env: napi_env?, info: napi_callback_info?): napi_value? = try {
    memScoped {
        val argc = alloc<size_tVar>()
        argc.value = 1u
        val argv = allocArray<napi_valueVar>(1)
        val thisVar = alloc<napi_valueVar>()
        napi_get_cb_info(env, info, argc.ptr, argv, thisVar.ptr, null)

        val typeVar = alloc<napi_valuetype.Var>()
        if (argc.value > 0u) {
            napi_typeof(env, argv[0], typeVar.ptr)
        }

        if (argc.value > 0u && typeVar.value == napi.napi_valuetype.napi_external) {
            // Called from Kotlin toNapiWrappedObject
            val externalPtr = alloc<kotlinx.cinterop.COpaquePointerVar>()
            napi_get_value_external(env, argv[0], externalPtr.ptr)
            napi_wrap(env, thisVar.value, externalPtr.value, staticCFunction(::TestClass_finalize),
    null, null)
            val adjustedValue = alloc<LongVar>()
            napi_adjust_external_memory(env, 8192L, adjustedValue.ptr)
            return@memScoped thisVar.value
        }

        // Called from JS 'new'
        val arg0 = argv[0]!!.toKotlinInt(env!!)!!
        val instance = TestClass(arg0)
        val stableRef = StableRef.create(instance)
        napi_wrap(env, thisVar.value, stableRef.asCPointer(), staticCFunction(::TestClass_finalize),
    null, null)
        val adjustedValue = alloc<LongVar>()
        napi_adjust_external_memory(env, 8192L, adjustedValue.ptr)
        return@memScoped thisVar.value
    }
} catch (e: Throwable) {
    napi.napi_throw_error(env, null, e.message ?: "Unknown Kotlin exception")
    null
}
