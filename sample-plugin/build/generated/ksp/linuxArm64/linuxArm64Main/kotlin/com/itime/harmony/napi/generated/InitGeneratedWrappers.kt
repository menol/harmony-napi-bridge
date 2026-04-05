@file:OptIn(
  kotlinx.cinterop.ExperimentalForeignApi::class,
  kotlin.experimental.ExperimentalNativeApi::class,
)

package com.itime.harmony.napi.generated

import kotlin.OptIn
import kotlin.native.CName
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.`value`
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.cstr
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.staticCFunction
import napi.napi_create_object
import napi.napi_create_string_utf8
import napi.napi_define_properties
import napi.napi_env
import napi.napi_property_descriptor
import napi.napi_set_named_property
import napi.napi_value
import napi.napi_valueVar

@CName("InitGeneratedWrappers")
public fun InitGeneratedWrappers(env: napi_env?, exports: napi_value?): napi_value? {
  memScoped {
      // Register TestInterface (TestInterface)
      val TestInterface_obj = alloc<napi_valueVar>()
      napi_create_object(env, TestInterface_obj.ptr)

      val desc_sayHello = alloc<napi_property_descriptor>()
      desc_sayHello.utf8name = "sayHello".cstr.ptr
      desc_sayHello.method = staticCFunction(::TestInterface_sayHello_wrapper)
      desc_sayHello.attributes = 0u.convert() // napi_default
      napi_define_properties(env, TestInterface_obj.value, 1u, desc_sayHello.ptr)

      napi_set_named_property(env, exports, "TestInterface", TestInterface_obj.value)
      // Register HelloWorldPlugin (hello_world_plugin)
      val HelloWorldPlugin_obj = alloc<napi_valueVar>()
      napi_create_object(env, HelloWorldPlugin_obj.ptr)

      val desc_add = alloc<napi_property_descriptor>()
      desc_add.utf8name = "add".cstr.ptr
      desc_add.method = staticCFunction(::HelloWorldPlugin_add_wrapper)
      desc_add.attributes = 0u.convert() // napi_default
      napi_define_properties(env, HelloWorldPlugin_obj.value, 1u, desc_add.ptr)

      val desc_greet = alloc<napi_property_descriptor>()
      desc_greet.utf8name = "greet".cstr.ptr
      desc_greet.method = staticCFunction(::HelloWorldPlugin_greet_wrapper)
      desc_greet.attributes = 0u.convert() // napi_default
      napi_define_properties(env, HelloWorldPlugin_obj.value, 1u, desc_greet.ptr)

      val desc_processList = alloc<napi_property_descriptor>()
      desc_processList.utf8name = "processList".cstr.ptr
      desc_processList.method = staticCFunction(::HelloWorldPlugin_processList_wrapper)
      desc_processList.attributes = 0u.convert() // napi_default
      napi_define_properties(env, HelloWorldPlugin_obj.value, 1u, desc_processList.ptr)

      val desc_processMap = alloc<napi_property_descriptor>()
      desc_processMap.utf8name = "processMap".cstr.ptr
      desc_processMap.method = staticCFunction(::HelloWorldPlugin_processMap_wrapper)
      desc_processMap.attributes = 0u.convert() // napi_default
      napi_define_properties(env, HelloWorldPlugin_obj.value, 1u, desc_processMap.ptr)

      val desc_processAny = alloc<napi_property_descriptor>()
      desc_processAny.utf8name = "processAny".cstr.ptr
      desc_processAny.method = staticCFunction(::HelloWorldPlugin_processAny_wrapper)
      desc_processAny.attributes = 0u.convert() // napi_default
      napi_define_properties(env, HelloWorldPlugin_obj.value, 1u, desc_processAny.ptr)

      val desc_processAnyMap = alloc<napi_property_descriptor>()
      desc_processAnyMap.utf8name = "processAnyMap".cstr.ptr
      desc_processAnyMap.method = staticCFunction(::HelloWorldPlugin_processAnyMap_wrapper)
      desc_processAnyMap.attributes = 0u.convert() // napi_default
      napi_define_properties(env, HelloWorldPlugin_obj.value, 1u, desc_processAnyMap.ptr)

      val desc_processIntList = alloc<napi_property_descriptor>()
      desc_processIntList.utf8name = "processIntList".cstr.ptr
      desc_processIntList.method = staticCFunction(::HelloWorldPlugin_processIntList_wrapper)
      desc_processIntList.attributes = 0u.convert() // napi_default
      napi_define_properties(env, HelloWorldPlugin_obj.value, 1u, desc_processIntList.ptr)

      val desc_processDoubleList = alloc<napi_property_descriptor>()
      desc_processDoubleList.utf8name = "processDoubleList".cstr.ptr
      desc_processDoubleList.method = staticCFunction(::HelloWorldPlugin_processDoubleList_wrapper)
      desc_processDoubleList.attributes = 0u.convert() // napi_default
      napi_define_properties(env, HelloWorldPlugin_obj.value, 1u, desc_processDoubleList.ptr)

      val desc_processBooleanList = alloc<napi_property_descriptor>()
      desc_processBooleanList.utf8name = "processBooleanList".cstr.ptr
      desc_processBooleanList.method =
      staticCFunction(::HelloWorldPlugin_processBooleanList_wrapper)
      desc_processBooleanList.attributes = 0u.convert() // napi_default
      napi_define_properties(env, HelloWorldPlugin_obj.value, 1u, desc_processBooleanList.ptr)

      val desc_processStringIntMap = alloc<napi_property_descriptor>()
      desc_processStringIntMap.utf8name = "processStringIntMap".cstr.ptr
      desc_processStringIntMap.method =
      staticCFunction(::HelloWorldPlugin_processStringIntMap_wrapper)
      desc_processStringIntMap.attributes = 0u.convert() // napi_default
      napi_define_properties(env, HelloWorldPlugin_obj.value, 1u, desc_processStringIntMap.ptr)

      val desc_processStringDoubleMap = alloc<napi_property_descriptor>()
      desc_processStringDoubleMap.utf8name = "processStringDoubleMap".cstr.ptr
      desc_processStringDoubleMap.method =
      staticCFunction(::HelloWorldPlugin_processStringDoubleMap_wrapper)
      desc_processStringDoubleMap.attributes = 0u.convert() // napi_default
      napi_define_properties(env, HelloWorldPlugin_obj.value, 1u, desc_processStringDoubleMap.ptr)

      val desc_processStringBooleanMap = alloc<napi_property_descriptor>()
      desc_processStringBooleanMap.utf8name = "processStringBooleanMap".cstr.ptr
      desc_processStringBooleanMap.method =
      staticCFunction(::HelloWorldPlugin_processStringBooleanMap_wrapper)
      desc_processStringBooleanMap.attributes = 0u.convert() // napi_default
      napi_define_properties(env, HelloWorldPlugin_obj.value, 1u, desc_processStringBooleanMap.ptr)

      val desc_processUser = alloc<napi_property_descriptor>()
      desc_processUser.utf8name = "processUser".cstr.ptr
      desc_processUser.method = staticCFunction(::HelloWorldPlugin_processUser_wrapper)
      desc_processUser.attributes = 0u.convert() // napi_default
      napi_define_properties(env, HelloWorldPlugin_obj.value, 1u, desc_processUser.ptr)

      napi_set_named_property(env, exports, "hello_world_plugin", HelloWorldPlugin_obj.value)
  }
  return exports
}
