package com.realtech.logic

import kotlinx.cinterop.DoubleVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.cstr
import kotlinx.cinterop.get
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readValue
import kotlinx.cinterop.set
import kotlinx.cinterop.staticCFunction
import kotlinx.cinterop.value
import napi.napi_callback_info
import napi.napi_create_double
import napi.napi_default
import napi.napi_define_properties
import napi.napi_env
import napi.napi_get_cb_info
import napi.napi_get_value_double
import napi.napi_module
import napi.napi_module_register
import napi.napi_property_descriptor
import napi.napi_typeof
import napi.napi_value
import napi.napi_valueVar
import napi.napi_valuetype
import napi.size_tVar
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalForeignApi::class)
val demoModule: napi_module = memScoped {
    val module = alloc<napi_module>()
    module.nm_version = 1 // nm版本号，默认值为1
    module.nm_flags = 0u // nm标识符
    module.nm_filename = null // 文件名，暂不关注，使用默认值即可
    val func: napi.napi_addon_register_func = staticCFunction { p1: napi_env?, p2: napi_value? ->
        Init(p1!!, p2!!)
    }
    module.nm_register_func = func  // 指定nm的入口函数，这里是 Init
    module.nm_modname = "khn".cstr.getPointer(this) // 指定ArkTS页面导入的模块名，这里是 hn
    module.nm_priv = null // 暂不关注，使用默认即可
    for (i in 0 until 4) {
        module.reserved[i] = null // 暂不关注，使用默认值即可
    }
    module
}

@CName("KNInit")
@OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
fun register() {
    //napi native模块注册
    napi_module_register(demoModule.readValue())
}

@OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)
@CName("Init")
fun Init(env: napi_env, exports: napi_value): napi_value = memScoped {
    // 定义属性描述符
    val desc = allocArray<napi_property_descriptor>(1).apply {
//        this[0].utf8name = "add".cstr.getPointer(memScope)
        this[0].utf8name = "add".cstr.getPointer(memScope)
        val func: napi.napi_callback = staticCFunction { p1: napi_env?, p2: napi_callback_info? ->
            Add(p1!!, p2!!)
            InitOpenMiniEffect(p1!!, p2!!)
        }
        this[0].method = func
        this[0].attributes = napi_default
        this[0].data = null
    }
    // 注册属性到 exports
    napi_define_properties(env, exports, 1u, desc)
    return exports
}


@OptIn(ExperimentalForeignApi::class)
fun Add(env: napi_env, info: napi_callback_info): napi_value = memScoped {
    val argc = alloc<size_tVar>().apply { value = 2uL }
    val args = allocArray<napi_valueVar>(2).apply {
        this[0] = null
        this[1] = null
    }
    // 获取参数
    napi_get_cb_info(env, info, argc.ptr, args, null, null)
    // 检查类型
    val valuetype0 = alloc<napi_valuetype.Var>()
    val valuetype1 = alloc<napi_valuetype.Var>()
    napi_typeof(env, args[0], valuetype0.ptr)
    napi_typeof(env, args[1], valuetype1.ptr)
    // 转 double
    val value0 = alloc<DoubleVar>()
    val value1 = alloc<DoubleVar>()
    napi_get_value_double(env, args[0], value0.ptr)
    napi_get_value_double(env, args[1], value1.ptr)
    // 计算和
    val sum = alloc<napi_valueVar>()
    napi_create_double(env, value0.value + value1.value, sum.ptr)
    return sum.value!!
}

fun InitOpenMiniEffect(env: napi_env, info: napi_callback_info): napi_value? = memScoped {
    return@memScoped null
}