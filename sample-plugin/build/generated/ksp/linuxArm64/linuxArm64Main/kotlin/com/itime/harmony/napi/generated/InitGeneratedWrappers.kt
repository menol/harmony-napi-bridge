@file:OptIn(
  kotlinx.cinterop.ExperimentalForeignApi::class,
  kotlin.experimental.ExperimentalNativeApi::class,
)

package com.itime.harmony.napi.generated

import kotlin.OptIn
import kotlin.native.CName
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.`get`
import kotlinx.cinterop.`value`
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.convert
import kotlinx.cinterop.cstr
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.staticCFunction
import napi.NAPI_AUTO_LENGTH
import napi.napi_create_function
import napi.napi_create_object
import napi.napi_create_reference
import napi.napi_create_string_utf8
import napi.napi_define_class
import napi.napi_define_properties
import napi.napi_env
import napi.napi_property_descriptor
import napi.napi_refVar
import napi.napi_set_named_property
import napi.napi_set_property
import napi.napi_value
import napi.napi_valueVar

@CName("InitGeneratedWrappers")
public fun InitGeneratedWrappers(env: napi_env?, exports: napi_value?): napi_value? {
  memScoped {
      // Register Class IndexPresenter (IndexPresenter)
      val IndexPresenter_constructorVar = alloc<napi_valueVar>()
      val IndexPresenter_descArray = allocArray<napi_property_descriptor>(3)
      IndexPresenter_descArray[0].utf8name = "attach".cstr.ptr
      IndexPresenter_descArray[0].method = staticCFunction(::IndexPresenter_attach_wrapper)
      IndexPresenter_descArray[0].attributes = 0u.convert() // napi_default
      IndexPresenter_descArray[1].utf8name = "showUser".cstr.ptr
      IndexPresenter_descArray[1].method = staticCFunction(::IndexPresenter_showUser_wrapper)
      IndexPresenter_descArray[1].attributes = 0u.convert() // napi_default
      IndexPresenter_descArray[2].utf8name = "detach".cstr.ptr
      IndexPresenter_descArray[2].method = staticCFunction(::IndexPresenter_detach_wrapper)
      IndexPresenter_descArray[2].attributes = 0u.convert() // napi_default
      napi_define_class(env, "IndexPresenter", napi.NAPI_AUTO_LENGTH.convert(),
      staticCFunction(::IndexPresenter_constructor), null, 3u.convert(), IndexPresenter_descArray,
      IndexPresenter_constructorVar.ptr)
      val IndexPresenter_refVar = alloc<napi_refVar>()
      napi_create_reference(env, IndexPresenter_constructorVar.value, 1u.convert(),
      IndexPresenter_refVar.ptr)
      com.itime.harmony.napi.runtime.utils.ConstructorRegistry.refs["IndexPresenter"] =
      IndexPresenter_refVar.value!!
      napi_set_named_property(env, exports, "IndexPresenter", IndexPresenter_constructorVar.value)
      // Register Class DemoAbstract (DemoAbstract)
      val DemoAbstract_constructorVar = alloc<napi_valueVar>()
      val DemoAbstract_descArray = allocArray<napi_property_descriptor>(2)
      DemoAbstract_descArray[0].utf8name = "process".cstr.ptr
      DemoAbstract_descArray[0].method = staticCFunction(::DemoAbstract_process_wrapper)
      DemoAbstract_descArray[0].attributes = 0u.convert() // napi_default
      DemoAbstract_descArray[1].utf8name = "sayHello".cstr.ptr
      DemoAbstract_descArray[1].method = staticCFunction(::DemoAbstract_sayHello_wrapper)
      DemoAbstract_descArray[1].attributes = 0u.convert() // napi_default
      napi_define_class(env, "DemoAbstract", napi.NAPI_AUTO_LENGTH.convert(),
      staticCFunction(::DemoAbstract_constructor), null, 2u.convert(), DemoAbstract_descArray,
      DemoAbstract_constructorVar.ptr)
      val DemoAbstract_refVar = alloc<napi_refVar>()
      napi_create_reference(env, DemoAbstract_constructorVar.value, 1u.convert(),
      DemoAbstract_refVar.ptr)
      com.itime.harmony.napi.runtime.utils.ConstructorRegistry.refs["DemoAbstract"] =
      DemoAbstract_refVar.value!!
      napi_set_named_property(env, exports, "DemoAbstract", DemoAbstract_constructorVar.value)
      // Register Class TestAbstract (TestAbstract)
      val TestAbstract_constructorVar = alloc<napi_valueVar>()
      val TestAbstract_descArray = allocArray<napi_property_descriptor>(2)
      TestAbstract_descArray[0].utf8name = "process".cstr.ptr
      TestAbstract_descArray[0].method = staticCFunction(::TestAbstract_process_wrapper)
      TestAbstract_descArray[0].attributes = 0u.convert() // napi_default
      TestAbstract_descArray[1].utf8name = "sayHello".cstr.ptr
      TestAbstract_descArray[1].method = staticCFunction(::TestAbstract_sayHello_wrapper)
      TestAbstract_descArray[1].attributes = 0u.convert() // napi_default
      napi_define_class(env, "TestAbstract", napi.NAPI_AUTO_LENGTH.convert(),
      staticCFunction(::TestAbstract_constructor), null, 2u.convert(), TestAbstract_descArray,
      TestAbstract_constructorVar.ptr)
      val TestAbstract_refVar = alloc<napi_refVar>()
      napi_create_reference(env, TestAbstract_constructorVar.value, 1u.convert(),
      TestAbstract_refVar.ptr)
      com.itime.harmony.napi.runtime.utils.ConstructorRegistry.refs["TestAbstract"] =
      TestAbstract_refVar.value!!
      napi_set_named_property(env, exports, "TestAbstract", TestAbstract_constructorVar.value)
      // Register Class TestClass (TestClass)
      val TestClass_constructorVar = alloc<napi_valueVar>()
      val TestClass_descArray = allocArray<napi_property_descriptor>(2)
      TestClass_descArray[0].utf8name = "fetchValue".cstr.ptr
      TestClass_descArray[0].method = staticCFunction(::TestClass_fetchValue_wrapper)
      TestClass_descArray[0].attributes = 0u.convert() // napi_default
      TestClass_descArray[1].utf8name = "increment".cstr.ptr
      TestClass_descArray[1].method = staticCFunction(::TestClass_increment_wrapper)
      TestClass_descArray[1].attributes = 0u.convert() // napi_default
      napi_define_class(env, "TestClass", napi.NAPI_AUTO_LENGTH.convert(),
      staticCFunction(::TestClass_constructor), null, 2u.convert(), TestClass_descArray,
      TestClass_constructorVar.ptr)
      val TestClass_refVar = alloc<napi_refVar>()
      napi_create_reference(env, TestClass_constructorVar.value, 1u.convert(), TestClass_refVar.ptr)
      com.itime.harmony.napi.runtime.utils.ConstructorRegistry.refs["TestClass"] =
      TestClass_refVar.value!!
      napi_set_named_property(env, exports, "TestClass", TestClass_constructorVar.value)
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

      val desc_processResult = alloc<napi_property_descriptor>()
      desc_processResult.utf8name = "processResult".cstr.ptr
      desc_processResult.method = staticCFunction(::HelloWorldPlugin_processResult_wrapper)
      desc_processResult.attributes = 0u.convert() // napi_default
      napi_define_properties(env, HelloWorldPlugin_obj.value, 1u, desc_processResult.ptr)

      val desc_getTestClass = alloc<napi_property_descriptor>()
      desc_getTestClass.utf8name = "getTestClass".cstr.ptr
      desc_getTestClass.method = staticCFunction(::HelloWorldPlugin_getTestClass_wrapper)
      desc_getTestClass.attributes = 0u.convert() // napi_default
      napi_define_properties(env, HelloWorldPlugin_obj.value, 1u, desc_getTestClass.ptr)

      napi_set_named_property(env, exports, "hello_world_plugin", HelloWorldPlugin_obj.value)
      val obj_UserUtilsV2 = alloc<napi_valueVar>()
      napi_create_object(env, obj_UserUtilsV2.ptr)
      val str_UserUtilsV2_getFullName = alloc<napi_valueVar>()
      napi_create_string_utf8(env, "getFullName", NAPI_AUTO_LENGTH.convert(),
      str_UserUtilsV2_getFullName.ptr)
      val fn_UserUtilsV2_getFullName = alloc<napi_valueVar>()
      napi_create_function(env, "getFullName", NAPI_AUTO_LENGTH.convert(),
      staticCFunction(::UserUtilsV2_getFullName_wrapper), null, fn_UserUtilsV2_getFullName.ptr)
      napi_set_property(env, obj_UserUtilsV2.value, str_UserUtilsV2_getFullName.value,
      fn_UserUtilsV2_getFullName.value)
      val str_export_UserUtilsV2 = alloc<napi_valueVar>()
      napi_create_string_utf8(env, "UserUtilsV2", NAPI_AUTO_LENGTH.convert(),
      str_export_UserUtilsV2.ptr)
      napi_set_property(env, exports, str_export_UserUtilsV2.value, obj_UserUtilsV2.value)

  }
  return exports
}
