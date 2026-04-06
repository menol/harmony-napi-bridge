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
      if (env == null || exports == null) return null
      com.itime.harmony.napi.runtime.utils.initMainThreadId()
      com.itime.harmony.napi.runtime.utils.initMainThreadRunner(env!!)
      memScoped {
      // Register Class IndexPresenter (IndexPresenter)
      val IndexPresenter_constructorVar = alloc<napi_valueVar>()
      val IndexPresenter_descArray = allocArray<napi_property_descriptor>(4)
      IndexPresenter_descArray[0].utf8name = "attach".cstr.ptr
      IndexPresenter_descArray[0].method = staticCFunction(::IndexPresenter_attach_wrapper)
      IndexPresenter_descArray[0].attributes = 0u.convert() // napi_default
      IndexPresenter_descArray[1].utf8name = "showUser".cstr.ptr
      IndexPresenter_descArray[1].method = staticCFunction(::IndexPresenter_showUser_wrapper)
      IndexPresenter_descArray[1].attributes = 0u.convert() // napi_default
      IndexPresenter_descArray[2].utf8name = "showStudent".cstr.ptr
      IndexPresenter_descArray[2].method = staticCFunction(::IndexPresenter_showStudent_wrapper)
      IndexPresenter_descArray[2].attributes = 0u.convert() // napi_default
      IndexPresenter_descArray[3].utf8name = "detach".cstr.ptr
      IndexPresenter_descArray[3].method = staticCFunction(::IndexPresenter_detach_wrapper)
      IndexPresenter_descArray[3].attributes = 0u.convert() // napi_default
      napi_define_class(env!!, "IndexPresenter", napi.NAPI_AUTO_LENGTH.convert(),
      staticCFunction(::IndexPresenter_constructor), null, 4u.convert(), IndexPresenter_descArray,
      IndexPresenter_constructorVar.ptr)
      val IndexPresenter_refVar = alloc<napi_refVar>()
      napi_create_reference(env!!, IndexPresenter_constructorVar.value, 1u.convert(),
      IndexPresenter_refVar.ptr)
      com.itime.harmony.napi.runtime.utils.ConstructorRegistry.refs["IndexPresenter"] =
      IndexPresenter_refVar.value!!
      napi_set_named_property(env!!, exports!!, "IndexPresenter",
      IndexPresenter_constructorVar.value)
      // Register Class DemoAbstract (DemoAbstract)
      val DemoAbstract_constructorVar = alloc<napi_valueVar>()
      val DemoAbstract_descArray = allocArray<napi_property_descriptor>(2)
      DemoAbstract_descArray[0].utf8name = "process".cstr.ptr
      DemoAbstract_descArray[0].method = staticCFunction(::DemoAbstract_process_wrapper)
      DemoAbstract_descArray[0].attributes = 0u.convert() // napi_default
      DemoAbstract_descArray[1].utf8name = "sayHello".cstr.ptr
      DemoAbstract_descArray[1].method = staticCFunction(::DemoAbstract_sayHello_wrapper)
      DemoAbstract_descArray[1].attributes = 0u.convert() // napi_default
      napi_define_class(env!!, "DemoAbstract", napi.NAPI_AUTO_LENGTH.convert(),
      staticCFunction(::DemoAbstract_constructor), null, 2u.convert(), DemoAbstract_descArray,
      DemoAbstract_constructorVar.ptr)
      val DemoAbstract_refVar = alloc<napi_refVar>()
      napi_create_reference(env!!, DemoAbstract_constructorVar.value, 1u.convert(),
      DemoAbstract_refVar.ptr)
      com.itime.harmony.napi.runtime.utils.ConstructorRegistry.refs["DemoAbstract"] =
      DemoAbstract_refVar.value!!
      napi_set_named_property(env!!, exports!!, "DemoAbstract", DemoAbstract_constructorVar.value)
      // Register Class TestAbstract (TestAbstract)
      val TestAbstract_constructorVar = alloc<napi_valueVar>()
      val TestAbstract_descArray = allocArray<napi_property_descriptor>(2)
      TestAbstract_descArray[0].utf8name = "process".cstr.ptr
      TestAbstract_descArray[0].method = staticCFunction(::TestAbstract_process_wrapper)
      TestAbstract_descArray[0].attributes = 0u.convert() // napi_default
      TestAbstract_descArray[1].utf8name = "sayHello".cstr.ptr
      TestAbstract_descArray[1].method = staticCFunction(::TestAbstract_sayHello_wrapper)
      TestAbstract_descArray[1].attributes = 0u.convert() // napi_default
      napi_define_class(env!!, "TestAbstract", napi.NAPI_AUTO_LENGTH.convert(),
      staticCFunction(::TestAbstract_constructor), null, 2u.convert(), TestAbstract_descArray,
      TestAbstract_constructorVar.ptr)
      val TestAbstract_refVar = alloc<napi_refVar>()
      napi_create_reference(env!!, TestAbstract_constructorVar.value, 1u.convert(),
      TestAbstract_refVar.ptr)
      com.itime.harmony.napi.runtime.utils.ConstructorRegistry.refs["TestAbstract"] =
      TestAbstract_refVar.value!!
      napi_set_named_property(env!!, exports!!, "TestAbstract", TestAbstract_constructorVar.value)
      // Register Class TestClass (TestClass)
      val TestClass_constructorVar = alloc<napi_valueVar>()
      val TestClass_descArray = allocArray<napi_property_descriptor>(2)
      TestClass_descArray[0].utf8name = "fetchValue".cstr.ptr
      TestClass_descArray[0].method = staticCFunction(::TestClass_fetchValue_wrapper)
      TestClass_descArray[0].attributes = 0u.convert() // napi_default
      TestClass_descArray[1].utf8name = "increment".cstr.ptr
      TestClass_descArray[1].method = staticCFunction(::TestClass_increment_wrapper)
      TestClass_descArray[1].attributes = 0u.convert() // napi_default
      napi_define_class(env!!, "TestClass", napi.NAPI_AUTO_LENGTH.convert(),
      staticCFunction(::TestClass_constructor), null, 2u.convert(), TestClass_descArray,
      TestClass_constructorVar.ptr)
      val TestClass_refVar = alloc<napi_refVar>()
      napi_create_reference(env!!, TestClass_constructorVar.value, 1u.convert(),
      TestClass_refVar.ptr)
      com.itime.harmony.napi.runtime.utils.ConstructorRegistry.refs["TestClass"] =
      TestClass_refVar.value!!
      napi_set_named_property(env!!, exports!!, "TestClass", TestClass_constructorVar.value)
      // Register HelloWorldPlugin (hello_world_plugin)
      val HelloWorldPlugin_obj = alloc<napi_valueVar>()
      napi_create_object(env!!, HelloWorldPlugin_obj.ptr)

      val desc_HelloWorldPlugin_add = alloc<napi_property_descriptor>()
      desc_HelloWorldPlugin_add.utf8name = "add".cstr.ptr
      desc_HelloWorldPlugin_add.method = staticCFunction(::HelloWorldPlugin_add_wrapper)
      desc_HelloWorldPlugin_add.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, HelloWorldPlugin_obj.value, 1u, desc_HelloWorldPlugin_add.ptr)

      val desc_HelloWorldPlugin_greet = alloc<napi_property_descriptor>()
      desc_HelloWorldPlugin_greet.utf8name = "greet".cstr.ptr
      desc_HelloWorldPlugin_greet.method = staticCFunction(::HelloWorldPlugin_greet_wrapper)
      desc_HelloWorldPlugin_greet.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, HelloWorldPlugin_obj.value, 1u, desc_HelloWorldPlugin_greet.ptr)

      val desc_HelloWorldPlugin_processList = alloc<napi_property_descriptor>()
      desc_HelloWorldPlugin_processList.utf8name = "processList".cstr.ptr
      desc_HelloWorldPlugin_processList.method =
      staticCFunction(::HelloWorldPlugin_processList_wrapper)
      desc_HelloWorldPlugin_processList.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, HelloWorldPlugin_obj.value, 1u,
      desc_HelloWorldPlugin_processList.ptr)

      val desc_HelloWorldPlugin_processMap = alloc<napi_property_descriptor>()
      desc_HelloWorldPlugin_processMap.utf8name = "processMap".cstr.ptr
      desc_HelloWorldPlugin_processMap.method =
      staticCFunction(::HelloWorldPlugin_processMap_wrapper)
      desc_HelloWorldPlugin_processMap.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, HelloWorldPlugin_obj.value, 1u,
      desc_HelloWorldPlugin_processMap.ptr)

      val desc_HelloWorldPlugin_processAny = alloc<napi_property_descriptor>()
      desc_HelloWorldPlugin_processAny.utf8name = "processAny".cstr.ptr
      desc_HelloWorldPlugin_processAny.method =
      staticCFunction(::HelloWorldPlugin_processAny_wrapper)
      desc_HelloWorldPlugin_processAny.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, HelloWorldPlugin_obj.value, 1u,
      desc_HelloWorldPlugin_processAny.ptr)

      val desc_HelloWorldPlugin_processAnyMap = alloc<napi_property_descriptor>()
      desc_HelloWorldPlugin_processAnyMap.utf8name = "processAnyMap".cstr.ptr
      desc_HelloWorldPlugin_processAnyMap.method =
      staticCFunction(::HelloWorldPlugin_processAnyMap_wrapper)
      desc_HelloWorldPlugin_processAnyMap.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, HelloWorldPlugin_obj.value, 1u,
      desc_HelloWorldPlugin_processAnyMap.ptr)

      val desc_HelloWorldPlugin_processIntList = alloc<napi_property_descriptor>()
      desc_HelloWorldPlugin_processIntList.utf8name = "processIntList".cstr.ptr
      desc_HelloWorldPlugin_processIntList.method =
      staticCFunction(::HelloWorldPlugin_processIntList_wrapper)
      desc_HelloWorldPlugin_processIntList.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, HelloWorldPlugin_obj.value, 1u,
      desc_HelloWorldPlugin_processIntList.ptr)

      val desc_HelloWorldPlugin_processDoubleList = alloc<napi_property_descriptor>()
      desc_HelloWorldPlugin_processDoubleList.utf8name = "processDoubleList".cstr.ptr
      desc_HelloWorldPlugin_processDoubleList.method =
      staticCFunction(::HelloWorldPlugin_processDoubleList_wrapper)
      desc_HelloWorldPlugin_processDoubleList.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, HelloWorldPlugin_obj.value, 1u,
      desc_HelloWorldPlugin_processDoubleList.ptr)

      val desc_HelloWorldPlugin_processBooleanList = alloc<napi_property_descriptor>()
      desc_HelloWorldPlugin_processBooleanList.utf8name = "processBooleanList".cstr.ptr
      desc_HelloWorldPlugin_processBooleanList.method =
      staticCFunction(::HelloWorldPlugin_processBooleanList_wrapper)
      desc_HelloWorldPlugin_processBooleanList.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, HelloWorldPlugin_obj.value, 1u,
      desc_HelloWorldPlugin_processBooleanList.ptr)

      val desc_HelloWorldPlugin_processStringIntMap = alloc<napi_property_descriptor>()
      desc_HelloWorldPlugin_processStringIntMap.utf8name = "processStringIntMap".cstr.ptr
      desc_HelloWorldPlugin_processStringIntMap.method =
      staticCFunction(::HelloWorldPlugin_processStringIntMap_wrapper)
      desc_HelloWorldPlugin_processStringIntMap.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, HelloWorldPlugin_obj.value, 1u,
      desc_HelloWorldPlugin_processStringIntMap.ptr)

      val desc_HelloWorldPlugin_processStringDoubleMap = alloc<napi_property_descriptor>()
      desc_HelloWorldPlugin_processStringDoubleMap.utf8name = "processStringDoubleMap".cstr.ptr
      desc_HelloWorldPlugin_processStringDoubleMap.method =
      staticCFunction(::HelloWorldPlugin_processStringDoubleMap_wrapper)
      desc_HelloWorldPlugin_processStringDoubleMap.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, HelloWorldPlugin_obj.value, 1u,
      desc_HelloWorldPlugin_processStringDoubleMap.ptr)

      val desc_HelloWorldPlugin_processStringBooleanMap = alloc<napi_property_descriptor>()
      desc_HelloWorldPlugin_processStringBooleanMap.utf8name = "processStringBooleanMap".cstr.ptr
      desc_HelloWorldPlugin_processStringBooleanMap.method =
      staticCFunction(::HelloWorldPlugin_processStringBooleanMap_wrapper)
      desc_HelloWorldPlugin_processStringBooleanMap.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, HelloWorldPlugin_obj.value, 1u,
      desc_HelloWorldPlugin_processStringBooleanMap.ptr)

      val desc_HelloWorldPlugin_processUser = alloc<napi_property_descriptor>()
      desc_HelloWorldPlugin_processUser.utf8name = "processUser".cstr.ptr
      desc_HelloWorldPlugin_processUser.method =
      staticCFunction(::HelloWorldPlugin_processUser_wrapper)
      desc_HelloWorldPlugin_processUser.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, HelloWorldPlugin_obj.value, 1u,
      desc_HelloWorldPlugin_processUser.ptr)

      val desc_HelloWorldPlugin_processResult = alloc<napi_property_descriptor>()
      desc_HelloWorldPlugin_processResult.utf8name = "processResult".cstr.ptr
      desc_HelloWorldPlugin_processResult.method =
      staticCFunction(::HelloWorldPlugin_processResult_wrapper)
      desc_HelloWorldPlugin_processResult.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, HelloWorldPlugin_obj.value, 1u,
      desc_HelloWorldPlugin_processResult.ptr)

      val desc_HelloWorldPlugin_fetchDataAsync = alloc<napi_property_descriptor>()
      desc_HelloWorldPlugin_fetchDataAsync.utf8name = "fetchDataAsync".cstr.ptr
      desc_HelloWorldPlugin_fetchDataAsync.method =
      staticCFunction(::HelloWorldPlugin_fetchDataAsync_wrapper)
      desc_HelloWorldPlugin_fetchDataAsync.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, HelloWorldPlugin_obj.value, 1u,
      desc_HelloWorldPlugin_fetchDataAsync.ptr)

      val desc_HelloWorldPlugin_executeMultipleTasksAsync = alloc<napi_property_descriptor>()
      desc_HelloWorldPlugin_executeMultipleTasksAsync.utf8name =
      "executeMultipleTasksAsync".cstr.ptr
      desc_HelloWorldPlugin_executeMultipleTasksAsync.method =
      staticCFunction(::HelloWorldPlugin_executeMultipleTasksAsync_wrapper)
      desc_HelloWorldPlugin_executeMultipleTasksAsync.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, HelloWorldPlugin_obj.value, 1u,
      desc_HelloWorldPlugin_executeMultipleTasksAsync.ptr)

      val desc_HelloWorldPlugin_getTestClass = alloc<napi_property_descriptor>()
      desc_HelloWorldPlugin_getTestClass.utf8name = "getTestClass".cstr.ptr
      desc_HelloWorldPlugin_getTestClass.method =
      staticCFunction(::HelloWorldPlugin_getTestClass_wrapper)
      desc_HelloWorldPlugin_getTestClass.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, HelloWorldPlugin_obj.value, 1u,
      desc_HelloWorldPlugin_getTestClass.ptr)

      napi_set_named_property(env!!, exports!!, "hello_world_plugin", HelloWorldPlugin_obj.value)
      // Register KoinPlugin (koin_plugin)
      val KoinPlugin_obj = alloc<napi_valueVar>()
      napi_create_object(env!!, KoinPlugin_obj.ptr)

      val desc_KoinPlugin_initKoin = alloc<napi_property_descriptor>()
      desc_KoinPlugin_initKoin.utf8name = "initKoin".cstr.ptr
      desc_KoinPlugin_initKoin.method = staticCFunction(::KoinPlugin_initKoin_wrapper)
      desc_KoinPlugin_initKoin.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, KoinPlugin_obj.value, 1u, desc_KoinPlugin_initKoin.ptr)

      val desc_KoinPlugin_getGreeting = alloc<napi_property_descriptor>()
      desc_KoinPlugin_getGreeting.utf8name = "getGreeting".cstr.ptr
      desc_KoinPlugin_getGreeting.method = staticCFunction(::KoinPlugin_getGreeting_wrapper)
      desc_KoinPlugin_getGreeting.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, KoinPlugin_obj.value, 1u, desc_KoinPlugin_getGreeting.ptr)

      val desc_KoinPlugin_getKoin = alloc<napi_property_descriptor>()
      desc_KoinPlugin_getKoin.utf8name = "getKoin".cstr.ptr
      desc_KoinPlugin_getKoin.method = staticCFunction(::KoinPlugin_getKoin_wrapper)
      desc_KoinPlugin_getKoin.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, KoinPlugin_obj.value, 1u, desc_KoinPlugin_getKoin.ptr)

      napi_set_named_property(env!!, exports!!, "koin_plugin", KoinPlugin_obj.value)
      // Register KtorPlugin (ktor_plugin)
      val KtorPlugin_obj = alloc<napi_valueVar>()
      napi_create_object(env!!, KtorPlugin_obj.ptr)

      val desc_KtorPlugin_initKtor = alloc<napi_property_descriptor>()
      desc_KtorPlugin_initKtor.utf8name = "initKtor".cstr.ptr
      desc_KtorPlugin_initKtor.method = staticCFunction(::KtorPlugin_initKtor_wrapper)
      desc_KtorPlugin_initKtor.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, KtorPlugin_obj.value, 1u, desc_KtorPlugin_initKtor.ptr)

      val desc_KtorPlugin_onFetchSuccess = alloc<napi_property_descriptor>()
      desc_KtorPlugin_onFetchSuccess.utf8name = "onFetchSuccess".cstr.ptr
      desc_KtorPlugin_onFetchSuccess.method = staticCFunction(::KtorPlugin_onFetchSuccess_wrapper)
      desc_KtorPlugin_onFetchSuccess.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, KtorPlugin_obj.value, 1u, desc_KtorPlugin_onFetchSuccess.ptr)

      val desc_KtorPlugin_onFetchError = alloc<napi_property_descriptor>()
      desc_KtorPlugin_onFetchError.utf8name = "onFetchError".cstr.ptr
      desc_KtorPlugin_onFetchError.method = staticCFunction(::KtorPlugin_onFetchError_wrapper)
      desc_KtorPlugin_onFetchError.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, KtorPlugin_obj.value, 1u, desc_KtorPlugin_onFetchError.ptr)

      val desc_KtorPlugin_fetchFromUrl = alloc<napi_property_descriptor>()
      desc_KtorPlugin_fetchFromUrl.utf8name = "fetchFromUrl".cstr.ptr
      desc_KtorPlugin_fetchFromUrl.method = staticCFunction(::KtorPlugin_fetchFromUrl_wrapper)
      desc_KtorPlugin_fetchFromUrl.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, KtorPlugin_obj.value, 1u, desc_KtorPlugin_fetchFromUrl.ptr)

      val desc_KtorPlugin_fetchUserFromApi = alloc<napi_property_descriptor>()
      desc_KtorPlugin_fetchUserFromApi.utf8name = "fetchUserFromApi".cstr.ptr
      desc_KtorPlugin_fetchUserFromApi.method =
      staticCFunction(::KtorPlugin_fetchUserFromApi_wrapper)
      desc_KtorPlugin_fetchUserFromApi.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, KtorPlugin_obj.value, 1u, desc_KtorPlugin_fetchUserFromApi.ptr)

      napi_set_named_property(env!!, exports!!, "ktor_plugin", KtorPlugin_obj.value)
      // Register StoragePlugin (storage_plugin)
      val StoragePlugin_obj = alloc<napi_valueVar>()
      napi_create_object(env!!, StoragePlugin_obj.ptr)

      val desc_StoragePlugin_initArkTsStorage = alloc<napi_property_descriptor>()
      desc_StoragePlugin_initArkTsStorage.utf8name = "initArkTsStorage".cstr.ptr
      desc_StoragePlugin_initArkTsStorage.method =
      staticCFunction(::StoragePlugin_initArkTsStorage_wrapper)
      desc_StoragePlugin_initArkTsStorage.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, StoragePlugin_obj.value, 1u,
      desc_StoragePlugin_initArkTsStorage.ptr)

      val desc_StoragePlugin_testStorageRoundTrip = alloc<napi_property_descriptor>()
      desc_StoragePlugin_testStorageRoundTrip.utf8name = "testStorageRoundTrip".cstr.ptr
      desc_StoragePlugin_testStorageRoundTrip.method =
      staticCFunction(::StoragePlugin_testStorageRoundTrip_wrapper)
      desc_StoragePlugin_testStorageRoundTrip.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, StoragePlugin_obj.value, 1u,
      desc_StoragePlugin_testStorageRoundTrip.ptr)

      val desc_StoragePlugin_getKoin = alloc<napi_property_descriptor>()
      desc_StoragePlugin_getKoin.utf8name = "getKoin".cstr.ptr
      desc_StoragePlugin_getKoin.method = staticCFunction(::StoragePlugin_getKoin_wrapper)
      desc_StoragePlugin_getKoin.attributes = 0u.convert() // napi_default
      napi_define_properties(env!!, StoragePlugin_obj.value, 1u, desc_StoragePlugin_getKoin.ptr)

      napi_set_named_property(env!!, exports!!, "storage_plugin", StoragePlugin_obj.value)
      val obj_UserUtilsV2 = alloc<napi_valueVar>()
      napi_create_object(env!!, obj_UserUtilsV2.ptr)
      val str_UserUtilsV2_getFullName = alloc<napi_valueVar>()
      napi_create_string_utf8(env!!, "getFullName", NAPI_AUTO_LENGTH.convert(),
      str_UserUtilsV2_getFullName.ptr)
      val fn_UserUtilsV2_getFullName = alloc<napi_valueVar>()
      napi_create_function(env!!, "getFullName", NAPI_AUTO_LENGTH.convert(),
      staticCFunction(::UserUtilsV2_getFullName_wrapper), null, fn_UserUtilsV2_getFullName.ptr)
      napi_set_property(env!!, obj_UserUtilsV2.value, str_UserUtilsV2_getFullName.value,
      fn_UserUtilsV2_getFullName.value)
      val str_export_UserUtilsV2 = alloc<napi_valueVar>()
      napi_create_string_utf8(env!!, "UserUtilsV2", NAPI_AUTO_LENGTH.convert(),
      str_export_UserUtilsV2.ptr)
      napi_set_property(env!!, exports!!, str_export_UserUtilsV2.value, obj_UserUtilsV2.value)

      }
      return exports
}
