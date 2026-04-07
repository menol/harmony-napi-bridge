package com.realtech.harmony.napi.runtime.utils

import kotlinx.cinterop.*
import kotlinx.coroutines.*
import napi.*
import platform.posix.size_t

@OptIn(ExperimentalForeignApi::class)
class NapiPromiseContext(
    val env: napi_env,
    val deferred: napi_deferred
)

@OptIn(ExperimentalForeignApi::class)
class NapiPromiseResult(
    val contextRef: StableRef<NapiPromiseContext>,
    val isSuccess: Boolean,
    val resultCreator: ((napi_env) -> napi_value)?,
    val errorMessage: String?
)

@OptIn(ExperimentalForeignApi::class)
val napiCallJsCallback = staticCFunction<napi_env?, napi_value?, COpaquePointer?, COpaquePointer?, Unit> { env, js_callback, context, data ->
    if (env == null || data == null) return@staticCFunction
    
    val resultRef = data.asStableRef<NapiPromiseResult>()
    val resultObj = resultRef.get()
    val ctxRef = resultObj.contextRef
    val ctx = ctxRef.get()
    
    if (resultObj.isSuccess) {
        val jsResult = resultObj.resultCreator?.invoke(env)
        val successValue = jsResult ?: memScoped {
            val nullVal = alloc<napi_valueVar>()
            napi_get_null(env, nullVal.ptr)
            nullVal.value!!
        }
        napi_resolve_deferred(env, ctx.deferred, successValue)
    } else {
        memScoped {
            val errCode = alloc<napi_valueVar>()
            napi_create_string_utf8(env, "Error", NAPI_AUTO_LENGTH.convert(), errCode.ptr)
            val errMsg = alloc<napi_valueVar>()
            napi_create_string_utf8(env, resultObj.errorMessage ?: "Unknown error", NAPI_AUTO_LENGTH.convert(), errMsg.ptr)
            val jsError = alloc<napi_valueVar>()
            napi_create_error(env, errCode.value, errMsg.value, jsError.ptr)
            napi_reject_deferred(env, ctx.deferred, jsError.value)
        }
    }
    
    // Cleanup
    resultRef.dispose()
    ctxRef.dispose()
}

@OptIn(ExperimentalForeignApi::class)
fun launchNapiCoroutine(
    env: napi_env,
    block: suspend () -> ((napi_env) -> napi_value)
): napi_value {
    memScoped {
        val deferredVar = alloc<napi_deferredVar>()
        val promiseVar = alloc<napi_valueVar>()
        napi_create_promise(env, deferredVar.ptr, promiseVar.ptr)
        
        val ctx = NapiPromiseContext(env, deferredVar.value!!)
        val ctxRef = StableRef.create(ctx)
        
        val tsfnVar = alloc<napi_threadsafe_functionVar>()
        val resourceName = alloc<napi_valueVar>()
        napi_create_string_utf8(env, "NapiCoroutine", NAPI_AUTO_LENGTH.convert(), resourceName.ptr)
        
        napi_create_threadsafe_function(
            env,
            null, // func
            null, // async_resource
            resourceName.value!!,
            0u.convert(), // max_queue_size
            1u.convert(), // initial_thread_count
            null, // thread_finalize_data
            null, // thread_finalize_cb
            null, // context
            napiCallJsCallback,
            tsfnVar.ptr
        )
        
        val tsfn = tsfnVar.value!!
        val dispatcher = NapiDispatcher(env)
        
        // Launch coroutine
        @OptIn(DelicateCoroutinesApi::class)
        GlobalScope.launch(dispatcher) {
            try {
                val resultCreator = block()
                val resultObj = NapiPromiseResult(ctxRef, true, resultCreator, null)
                val resultRef = StableRef.create(resultObj)
                napi_call_threadsafe_function(tsfn, resultRef.asCPointer(), napi_threadsafe_function_call_mode.napi_tsfn_nonblocking)
            } catch (e: Throwable) {
                val resultObj = NapiPromiseResult(ctxRef, false, null, e.message)
                val resultRef = StableRef.create(resultObj)
                napi_call_threadsafe_function(tsfn, resultRef.asCPointer(), napi_threadsafe_function_call_mode.napi_tsfn_nonblocking)
            } finally {
                napi_release_threadsafe_function(tsfn, napi_threadsafe_function_release_mode.napi_tsfn_release)
                dispatcher.close()
            }
        }
        
        return promiseVar.value!!
    }
}
