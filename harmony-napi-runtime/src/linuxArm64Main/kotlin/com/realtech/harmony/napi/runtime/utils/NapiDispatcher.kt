package com.realtech.harmony.napi.runtime.utils

import kotlinx.cinterop.*
import kotlinx.coroutines.*
import napi.*
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalForeignApi::class)
class NapiDispatcher(private val env: napi_env) : CoroutineDispatcher() {

    private val tsfn: napi_threadsafe_function = memScoped {
        val resourceName = alloc<napi_valueVar>()
        napi_create_string_utf8(env, "NapiDispatcher", NAPI_AUTO_LENGTH.convert(), resourceName.ptr)
        
        val tsfnVar = alloc<napi_threadsafe_functionVar>()
        napi_create_threadsafe_function(
            env,
            null,
            null,
            resourceName.value!!,
            0u.convert(),
            1u.convert(),
            null,
            null,
            null,
            staticCFunction { envPtr, _, _, data ->
                if (data != null) {
                    val runnableRef = data.asStableRef<Runnable>()
                    val runnable = runnableRef.get()
                    runnable.run()
                    runnableRef.dispose()
                }
            },
            tsfnVar.ptr
        )
        tsfnVar.value!!
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        val runnableRef = StableRef.create(block)
        napi_call_threadsafe_function(tsfn, runnableRef.asCPointer(), napi_threadsafe_function_call_mode.napi_tsfn_nonblocking)
    }
    
    fun close() {
        napi_release_threadsafe_function(tsfn, napi_threadsafe_function_release_mode.napi_tsfn_release)
    }
}