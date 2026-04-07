package com.realtech.harmony.napi.runtime.utils

import kotlinx.cinterop.*
import napi.*
import platform.posix.pthread_self
import platform.posix.pthread_t
import kotlin.concurrent.AtomicReference

private val mainThreadIdRef = AtomicReference<pthread_t?>(null)
@OptIn(ExperimentalForeignApi::class)
private var globalTsfn: napi_threadsafe_function? = null

fun initMainThreadId() {
    mainThreadIdRef.compareAndSet(null, pthread_self())
}

@OptIn(ExperimentalForeignApi::class)
fun initMainThreadRunner(env: napi_env) {
    if (globalTsfn != null) return
    memScoped {
        val resourceName = alloc<napi_valueVar>()
        napi_create_string_utf8(env, "GlobalMainThreadRunner", NAPI_AUTO_LENGTH.convert(), resourceName.ptr)
        
        val tsfnVar = alloc<napi_threadsafe_functionVar>()
        val status = napi_create_threadsafe_function(
            env,
            null,
            null,
            resourceName.value!!,
            0u.convert(),
            1u.convert(),
            null,
            null,
            null,
            staticCFunction { callEnv, _, _, data ->
                if (data != null && callEnv != null) {
                    val taskRef = data.asStableRef<() -> Unit>()
                    val task = taskRef.get()
                    task.invoke()
                    taskRef.dispose()
                }
            },
            tsfnVar.ptr
        )
        if (status == 0u) {
            globalTsfn = tsfnVar.value
        } else {
            println("Failed to create global threadsafe function, status: $status")
        }
    }
}

fun isMainThread(): Boolean {
    return pthread_self() == mainThreadIdRef.value
}

@OptIn(ExperimentalForeignApi::class)
fun <T> runOnMainThread(env: napi_env, jsObjRef: napi_ref, methodName: String, block: (napi_value, napi_value) -> T): T {
    // We accurately know if we are on the JS main thread
    val isOnMainThread = isMainThread()

    if (isOnMainThread) {
        var result: T? = null
        try {
            memScoped {
                val objVar = alloc<napi_valueVar>()
                napi_get_reference_value(env, jsObjRef, objVar.ptr)
                val jsObjValue = objVar.value!!
                val funcVar = alloc<napi_valueVar>()
                napi_get_named_property(env, jsObjValue, methodName, funcVar.ptr)
                result = block(jsObjValue, funcVar.value!!)
            }
        } catch (e: Throwable) {
            throw e
        }
        @Suppress("UNCHECKED_CAST")
        return result as T
    }

    // We are on a background thread. Use pre-created global threadsafe function.
    val tsfn = globalTsfn ?: throw IllegalStateException("globalTsfn is null. Ensure initMainThreadRunner is called on main thread.")
    
    val resultRef = kotlin.concurrent.AtomicReference<T?>(null)
    val isReadyRef = kotlin.concurrent.AtomicReference<Boolean>(false)
    val errorRef = kotlin.concurrent.AtomicReference<Throwable?>(null)
    
    println("[MainThreadRunner] Posting task to JS Main Thread for $methodName")
    val task = {
        println("[MainThreadRunner] Task executing on JS Main Thread for $methodName")
        try {
            memScoped {
                val objVar = alloc<napi_valueVar>()
                napi_get_reference_value(env, jsObjRef, objVar.ptr)
                val jsObjValue = objVar.value!!
                val funcVar = alloc<napi_valueVar>()
                napi_get_named_property(env, jsObjValue, methodName, funcVar.ptr)
                resultRef.value = block(jsObjValue, funcVar.value!!)
            }
        } catch (e: Throwable) {
            println("[MainThreadRunner] Error executing task: ${e.message}")
            errorRef.value = e
        } finally {
            println("[MainThreadRunner] Task completed for $methodName")
            isReadyRef.value = true
        }
    }
    
    val taskRef = StableRef.create(task)
    
    val status = napi_call_threadsafe_function(tsfn, taskRef.asCPointer(), napi_threadsafe_function_call_mode.napi_tsfn_blocking)
    println("[MainThreadRunner] napi_call_threadsafe_function status: $status")
    
    // Block wait on the background thread
    while (!isReadyRef.value) {
        platform.posix.usleep(1000u)
    }
    println("[MainThreadRunner] Background thread resumed for $methodName")
    
    val error = errorRef.value
    if (error != null) throw error
    @Suppress("UNCHECKED_CAST")
    return resultRef.value as T
}