#ifndef KONAN_LIBSAMPLE_PLUGIN_H
#define KONAN_LIBSAMPLE_PLUGIN_H
#ifdef __cplusplus
extern "C" {
#endif
#ifdef __cplusplus
typedef bool            libsample_plugin_KBoolean;
#else
typedef _Bool           libsample_plugin_KBoolean;
#endif
typedef unsigned short     libsample_plugin_KChar;
typedef signed char        libsample_plugin_KByte;
typedef short              libsample_plugin_KShort;
typedef int                libsample_plugin_KInt;
typedef long long          libsample_plugin_KLong;
typedef unsigned char      libsample_plugin_KUByte;
typedef unsigned short     libsample_plugin_KUShort;
typedef unsigned int       libsample_plugin_KUInt;
typedef unsigned long long libsample_plugin_KULong;
typedef float              libsample_plugin_KFloat;
typedef double             libsample_plugin_KDouble;
typedef float __attribute__ ((__vector_size__ (16))) libsample_plugin_KVector128;
typedef void*              libsample_plugin_KNativePtr;
struct libsample_plugin_KType;
typedef struct libsample_plugin_KType libsample_plugin_KType;

typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlin_Byte;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlin_Short;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlin_Int;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlin_Long;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlin_Float;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlin_Double;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlin_Char;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlin_Boolean;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlin_Unit;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlin_UByte;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlin_UShort;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlin_UInt;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlin_ULong;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_org_koin_core_module_Module;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_napi_generated_ArkTsHttpFetcher_NapiProxy;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_napi_generated_BasePageState_NapiProxy;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_napi_generated_BaseView_NapiProxy;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_napi_generated_IndexView_NapiProxy;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_Student;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_napi_generated_KeyValueStorage_NapiProxy;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_napi_generated_TestInterface_NapiProxy;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_User;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_ArkTsEngine;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpFetcher;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_ArkTsEngineConfig;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlinx_coroutines_CoroutineDispatcher;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlin_collections_Set;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlin_Any;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest_$serializer;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlinx_serialization_descriptors_SerialDescriptor;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlin_Array;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlinx_serialization_encoding_Decoder;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlinx_serialization_encoding_Encoder;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest_Companion;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlinx_serialization_KSerializer;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpResponse;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpResponse_$serializer;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpResponse_Companion;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_BasePageState;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_BaseView;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_DataSealed;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlinx_serialization_internal_SerializationConstructorMarker;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_DataSealed_Companion;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemA;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemA_$serializer;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemA_Companion;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemB;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemB_$serializer;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemB_Companion;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_DemoAbstract;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlin_collections_List;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_Error;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_Error_$serializer;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_Error_Companion;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_GreetingService;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_GreetingServiceImpl;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_TestInterface;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_TestClass;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_MutableData;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlin_collections_Map;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlin_collections_MutableList;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlin_collections_MutableMap;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_NetworkResult;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_TreeNode;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_TripleBox;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_Role;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_IndexPresenter;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_IndexView;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_KeyValueStorage;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_KoinPlugin;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_KtorPlugin;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_MutableData_$serializer;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_MutableData_Companion;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_NetworkResult_Companion;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_PageState;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_PageState_Companion;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_PageState_Error;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_PageState_Error_$serializer;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_PageState_Error_Companion;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_PageState_Loading;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_PageState_Loading_$serializer;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_PageState_Loading_Companion;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_Role_ADMIN;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_Role_USER;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_SettingsRepository;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_StoragePlugin;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_Success;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_Success_$serializer;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_Success_Companion;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_TreeNode_$serializer;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_TreeNode_Companion;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_User_$serializer;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_User_Companion;

extern void* InitGeneratedWrappers(void* env, void* exports);

typedef struct {
  /* Service functions. */
  void (*DisposeStablePointer)(libsample_plugin_KNativePtr ptr);
  void (*DisposeString)(const char* string);
  libsample_plugin_KBoolean (*IsInstance)(libsample_plugin_KNativePtr ref, const libsample_plugin_KType* type);
  libsample_plugin_kref_kotlin_Byte (*createNullableByte)(libsample_plugin_KByte);
  libsample_plugin_KByte (*getNonNullValueOfByte)(libsample_plugin_kref_kotlin_Byte);
  libsample_plugin_kref_kotlin_Short (*createNullableShort)(libsample_plugin_KShort);
  libsample_plugin_KShort (*getNonNullValueOfShort)(libsample_plugin_kref_kotlin_Short);
  libsample_plugin_kref_kotlin_Int (*createNullableInt)(libsample_plugin_KInt);
  libsample_plugin_KInt (*getNonNullValueOfInt)(libsample_plugin_kref_kotlin_Int);
  libsample_plugin_kref_kotlin_Long (*createNullableLong)(libsample_plugin_KLong);
  libsample_plugin_KLong (*getNonNullValueOfLong)(libsample_plugin_kref_kotlin_Long);
  libsample_plugin_kref_kotlin_Float (*createNullableFloat)(libsample_plugin_KFloat);
  libsample_plugin_KFloat (*getNonNullValueOfFloat)(libsample_plugin_kref_kotlin_Float);
  libsample_plugin_kref_kotlin_Double (*createNullableDouble)(libsample_plugin_KDouble);
  libsample_plugin_KDouble (*getNonNullValueOfDouble)(libsample_plugin_kref_kotlin_Double);
  libsample_plugin_kref_kotlin_Char (*createNullableChar)(libsample_plugin_KChar);
  libsample_plugin_KChar (*getNonNullValueOfChar)(libsample_plugin_kref_kotlin_Char);
  libsample_plugin_kref_kotlin_Boolean (*createNullableBoolean)(libsample_plugin_KBoolean);
  libsample_plugin_KBoolean (*getNonNullValueOfBoolean)(libsample_plugin_kref_kotlin_Boolean);
  libsample_plugin_kref_kotlin_Unit (*createNullableUnit)(void);
  libsample_plugin_kref_kotlin_UByte (*createNullableUByte)(libsample_plugin_KUByte);
  libsample_plugin_KUByte (*getNonNullValueOfUByte)(libsample_plugin_kref_kotlin_UByte);
  libsample_plugin_kref_kotlin_UShort (*createNullableUShort)(libsample_plugin_KUShort);
  libsample_plugin_KUShort (*getNonNullValueOfUShort)(libsample_plugin_kref_kotlin_UShort);
  libsample_plugin_kref_kotlin_UInt (*createNullableUInt)(libsample_plugin_KUInt);
  libsample_plugin_KUInt (*getNonNullValueOfUInt)(libsample_plugin_kref_kotlin_UInt);
  libsample_plugin_kref_kotlin_ULong (*createNullableULong)(libsample_plugin_KULong);
  libsample_plugin_KULong (*getNonNullValueOfULong)(libsample_plugin_kref_kotlin_ULong);

  /* User functions. */
  struct {
    struct {
      struct {
        struct {
          struct {
            struct {
              struct {
                struct {
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_napi_generated_ArkTsHttpFetcher_NapiProxy (*ArkTsHttpFetcher_NapiProxy)(void* env, void* jsObj);
                  void (*fetch)(libsample_plugin_kref_com_itime_harmony_napi_generated_ArkTsHttpFetcher_NapiProxy thiz, const char* requestId, libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest request);
                } ArkTsHttpFetcher_NapiProxy;
                struct {
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_napi_generated_BasePageState_NapiProxy (*BasePageState_NapiProxy)(void* env, void* jsObj);
                } BasePageState_NapiProxy;
                struct {
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_napi_generated_BaseView_NapiProxy (*BaseView_NapiProxy)(void* env, void* jsObj);
                } BaseView_NapiProxy;
                struct {
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_napi_generated_IndexView_NapiProxy (*IndexView_NapiProxy)(void* env, void* jsObj);
                  void (*showStudent)(libsample_plugin_kref_com_itime_harmony_napi_generated_IndexView_NapiProxy thiz, libsample_plugin_kref_com_itime_harmony_sample_Student student);
                  void (*showUser)(libsample_plugin_kref_com_itime_harmony_napi_generated_IndexView_NapiProxy thiz, const char* name);
                } IndexView_NapiProxy;
                struct {
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_napi_generated_KeyValueStorage_NapiProxy (*KeyValueStorage_NapiProxy)(void* env, void* jsObj);
                  const char* (*getString)(libsample_plugin_kref_com_itime_harmony_napi_generated_KeyValueStorage_NapiProxy thiz, const char* key);
                  void (*saveString)(libsample_plugin_kref_com_itime_harmony_napi_generated_KeyValueStorage_NapiProxy thiz, const char* key, const char* value);
                } KeyValueStorage_NapiProxy;
                struct {
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_napi_generated_TestInterface_NapiProxy (*TestInterface_NapiProxy)(void* env, void* jsObj);
                  const char* (*sayHello)(libsample_plugin_kref_com_itime_harmony_napi_generated_TestInterface_NapiProxy thiz, const char* name);
                } TestInterface_NapiProxy;
                void* (*DemoAbstract_constructor)(void* env, void* info);
                void (*DemoAbstract_finalize)(void* env, void* data, void* hint);
                void* (*DemoAbstract_process_wrapper)(void* env, void* info);
                void* (*DemoAbstract_sayHello_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_add_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_callTestInterface_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_executeMultipleTasksAsync_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_extremeSuspendThrow_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_fetchDataAsync_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_getNullableList_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_getTestClass_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_greet_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_modifyMutableData_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processAnyMap_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processAny_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processBooleanList_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processDataSealed_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processDoubleList_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processExtremeAny_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processIntList_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processList_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processMap_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processMutableList_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processMutableMap_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processNullableString_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processResult_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processStringBooleanMap_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processStringDoubleMap_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processStringIntMap_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processTree_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processTripleBox_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processUser_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_throwKotlinException_wrapper)(void* env, void* info);
                void* (*IndexPresenter_attach_wrapper)(void* env, void* info);
                void* (*IndexPresenter_constructor)(void* env, void* info);
                void* (*IndexPresenter_detach_wrapper)(void* env, void* info);
                void (*IndexPresenter_finalize)(void* env, void* data, void* hint);
                void* (*IndexPresenter_showStudent_wrapper)(void* env, void* info);
                void* (*IndexPresenter_showUser_wrapper)(void* env, void* info);
                void* (*InitGeneratedWrappers_)(void* env, void* exports);
                void* (*KoinPlugin_getGreeting_wrapper)(void* env, void* info);
                void* (*KoinPlugin_getKoin_wrapper)(void* env, void* info);
                void* (*KoinPlugin_initKoin_wrapper)(void* env, void* info);
                void* (*KtorPlugin_fetchFromUrl_wrapper)(void* env, void* info);
                void* (*KtorPlugin_fetchUserFromApi_wrapper)(void* env, void* info);
                void* (*KtorPlugin_initKtor_wrapper)(void* env, void* info);
                void* (*KtorPlugin_onFetchError_wrapper)(void* env, void* info);
                void* (*KtorPlugin_onFetchSuccess_wrapper)(void* env, void* info);
                void* (*StoragePlugin_getKoin_wrapper)(void* env, void* info);
                void* (*StoragePlugin_initArkTsStorage_wrapper)(void* env, void* info);
                void* (*StoragePlugin_testStorageRoundTrip_wrapper)(void* env, void* info);
                void* (*TestAbstract_constructor)(void* env, void* info);
                void (*TestAbstract_finalize)(void* env, void* data, void* hint);
                void* (*TestAbstract_process_wrapper)(void* env, void* info);
                void* (*TestAbstract_sayHello_wrapper)(void* env, void* info);
                void* (*TestClass_constructor)(void* env, void* info);
                void* (*TestClass_fetchValue_wrapper)(void* env, void* info);
                void (*TestClass_finalize)(void* env, void* data, void* hint);
                void* (*TestClass_increment_wrapper)(void* env, void* info);
                void* (*UserUtilsV2_getFullName_wrapper)(void* env, void* info);
              } generated;
            } napi;
            struct {
              struct {
                libsample_plugin_KType* (*_type)(void);
                libsample_plugin_kref_com_itime_harmony_sample_ArkTsEngine (*ArkTsEngine)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpFetcher fetcher, libsample_plugin_kref_com_itime_harmony_sample_ArkTsEngineConfig config);
                libsample_plugin_kref_com_itime_harmony_sample_ArkTsEngineConfig (*get_config)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsEngine thiz);
                libsample_plugin_kref_kotlinx_coroutines_CoroutineDispatcher (*get_dispatcher)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsEngine thiz);
                libsample_plugin_kref_kotlin_collections_Set (*get_supportedCapabilities)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsEngine thiz);
              } ArkTsEngine;
              struct {
                libsample_plugin_KType* (*_type)(void);
                libsample_plugin_kref_com_itime_harmony_sample_ArkTsEngineConfig (*ArkTsEngineConfig)();
              } ArkTsEngineConfig;
              struct {
                libsample_plugin_KType* (*_type)(void);
                void (*fetch)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpFetcher thiz, const char* requestId, libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest request);
              } ArkTsHttpFetcher;
              struct {
                struct {
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest_$serializer (*_instance)();
                  libsample_plugin_kref_kotlinx_serialization_descriptors_SerialDescriptor (*get_descriptor)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest_$serializer thiz);
                  libsample_plugin_kref_kotlin_Array (*childSerializers)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest_$serializer thiz);
                  libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest (*deserialize)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest_$serializer thiz, libsample_plugin_kref_kotlinx_serialization_encoding_Decoder decoder);
                  void (*serialize)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest_$serializer thiz, libsample_plugin_kref_kotlinx_serialization_encoding_Encoder encoder, libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest value);
                } $serializer;
                struct {
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest_Companion (*_instance)();
                  libsample_plugin_kref_kotlinx_serialization_KSerializer (*serializer)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest_Companion thiz);
                } Companion;
                libsample_plugin_KType* (*_type)(void);
                libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest (*ArkTsHttpRequest)(const char* url, const char* method, const char* body);
                const char* (*get_body)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest thiz);
                const char* (*get_method)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest thiz);
                const char* (*get_url)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest thiz);
                const char* (*component1)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest thiz);
                const char* (*component2)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest thiz);
                const char* (*component3)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest thiz);
                libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest (*copy)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest thiz, const char* url, const char* method, const char* body);
                libsample_plugin_KBoolean (*equals)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest thiz, libsample_plugin_kref_kotlin_Any other);
                libsample_plugin_KInt (*hashCode)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest thiz);
                const char* (*toString)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpRequest thiz);
              } ArkTsHttpRequest;
              struct {
                struct {
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpResponse_$serializer (*_instance)();
                  libsample_plugin_kref_kotlinx_serialization_descriptors_SerialDescriptor (*get_descriptor)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpResponse_$serializer thiz);
                  libsample_plugin_kref_kotlin_Array (*childSerializers)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpResponse_$serializer thiz);
                  libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpResponse (*deserialize)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpResponse_$serializer thiz, libsample_plugin_kref_kotlinx_serialization_encoding_Decoder decoder);
                  void (*serialize)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpResponse_$serializer thiz, libsample_plugin_kref_kotlinx_serialization_encoding_Encoder encoder, libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpResponse value);
                } $serializer;
                struct {
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpResponse_Companion (*_instance)();
                  libsample_plugin_kref_kotlinx_serialization_KSerializer (*serializer)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpResponse_Companion thiz);
                } Companion;
                libsample_plugin_KType* (*_type)(void);
                libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpResponse (*ArkTsHttpResponse)(libsample_plugin_KInt status, const char* body);
                const char* (*get_body)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpResponse thiz);
                libsample_plugin_KInt (*get_status)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpResponse thiz);
                libsample_plugin_KInt (*component1)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpResponse thiz);
                const char* (*component2)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpResponse thiz);
                libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpResponse (*copy)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpResponse thiz, libsample_plugin_KInt status, const char* body);
                libsample_plugin_KBoolean (*equals)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpResponse thiz, libsample_plugin_kref_kotlin_Any other);
                libsample_plugin_KInt (*hashCode)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpResponse thiz);
                const char* (*toString)(libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpResponse thiz);
              } ArkTsHttpResponse;
              struct {
                libsample_plugin_KType* (*_type)(void);
              } BasePageState;
              struct {
                libsample_plugin_KType* (*_type)(void);
              } BaseView;
              struct {
                struct {
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_sample_DataSealed_Companion (*_instance)();
                  libsample_plugin_kref_kotlinx_serialization_KSerializer (*serializer)(libsample_plugin_kref_com_itime_harmony_sample_DataSealed_Companion thiz);
                  libsample_plugin_kref_kotlinx_serialization_KSerializer (*serializer_)(libsample_plugin_kref_com_itime_harmony_sample_DataSealed_Companion thiz, libsample_plugin_kref_kotlin_Array typeParamsSerializers);
                } Companion;
                struct {
                  struct {
                    libsample_plugin_KType* (*_type)(void);
                    libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemA_$serializer (*_instance)();
                    libsample_plugin_kref_kotlinx_serialization_descriptors_SerialDescriptor (*get_descriptor)(libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemA_$serializer thiz);
                    libsample_plugin_kref_kotlin_Array (*childSerializers)(libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemA_$serializer thiz);
                    libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemA (*deserialize)(libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemA_$serializer thiz, libsample_plugin_kref_kotlinx_serialization_encoding_Decoder decoder);
                    void (*serialize)(libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemA_$serializer thiz, libsample_plugin_kref_kotlinx_serialization_encoding_Encoder encoder, libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemA value);
                  } $serializer;
                  struct {
                    libsample_plugin_KType* (*_type)(void);
                    libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemA_Companion (*_instance)();
                    libsample_plugin_kref_kotlinx_serialization_KSerializer (*serializer)(libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemA_Companion thiz);
                  } Companion;
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemA (*ItemA)(libsample_plugin_KInt id);
                  libsample_plugin_KInt (*get_id)(libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemA thiz);
                  libsample_plugin_KInt (*component1)(libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemA thiz);
                  libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemA (*copy)(libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemA thiz, libsample_plugin_KInt id);
                  libsample_plugin_KBoolean (*equals)(libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemA thiz, libsample_plugin_kref_kotlin_Any other);
                  libsample_plugin_KInt (*hashCode)(libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemA thiz);
                  const char* (*toString)(libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemA thiz);
                } ItemA;
                struct {
                  struct {
                    libsample_plugin_KType* (*_type)(void);
                    libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemB_$serializer (*_instance)();
                    libsample_plugin_kref_kotlinx_serialization_descriptors_SerialDescriptor (*get_descriptor)(libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemB_$serializer thiz);
                    libsample_plugin_kref_kotlin_Array (*childSerializers)(libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemB_$serializer thiz);
                    libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemB (*deserialize)(libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemB_$serializer thiz, libsample_plugin_kref_kotlinx_serialization_encoding_Decoder decoder);
                    void (*serialize)(libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemB_$serializer thiz, libsample_plugin_kref_kotlinx_serialization_encoding_Encoder encoder, libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemB value);
                  } $serializer;
                  struct {
                    libsample_plugin_KType* (*_type)(void);
                    libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemB_Companion (*_instance)();
                    libsample_plugin_kref_kotlinx_serialization_KSerializer (*serializer)(libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemB_Companion thiz);
                  } Companion;
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemB (*ItemB)(const char* name);
                  const char* (*get_name)(libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemB thiz);
                  const char* (*component1)(libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemB thiz);
                  libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemB (*copy)(libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemB thiz, const char* name);
                  libsample_plugin_KBoolean (*equals)(libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemB thiz, libsample_plugin_kref_kotlin_Any other);
                  libsample_plugin_KInt (*hashCode)(libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemB thiz);
                  const char* (*toString)(libsample_plugin_kref_com_itime_harmony_sample_DataSealed_ItemB thiz);
                } ItemB;
                libsample_plugin_KType* (*_type)(void);
                libsample_plugin_kref_com_itime_harmony_sample_DataSealed (*DataSealed)(libsample_plugin_KInt seen1, libsample_plugin_kref_kotlinx_serialization_internal_SerializationConstructorMarker serializationConstructorMarker);
                libsample_plugin_kref_com_itime_harmony_sample_DataSealed (*DataSealed_)();
              } DataSealed;
              struct {
                libsample_plugin_KType* (*_type)(void);
                libsample_plugin_kref_com_itime_harmony_sample_DemoAbstract (*DemoAbstract)();
                libsample_plugin_kref_kotlin_collections_List (*process)(libsample_plugin_kref_com_itime_harmony_sample_DemoAbstract thiz, libsample_plugin_kref_kotlin_collections_List item);
                const char* (*sayHello)(libsample_plugin_kref_com_itime_harmony_sample_DemoAbstract thiz);
              } DemoAbstract;
              struct {
                struct {
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_sample_Error_$serializer (*_instance)();
                  libsample_plugin_kref_kotlinx_serialization_descriptors_SerialDescriptor (*get_descriptor)(libsample_plugin_kref_com_itime_harmony_sample_Error_$serializer thiz);
                  libsample_plugin_kref_kotlin_Array (*childSerializers)(libsample_plugin_kref_com_itime_harmony_sample_Error_$serializer thiz);
                  libsample_plugin_kref_com_itime_harmony_sample_Error (*deserialize)(libsample_plugin_kref_com_itime_harmony_sample_Error_$serializer thiz, libsample_plugin_kref_kotlinx_serialization_encoding_Decoder decoder);
                  void (*serialize)(libsample_plugin_kref_com_itime_harmony_sample_Error_$serializer thiz, libsample_plugin_kref_kotlinx_serialization_encoding_Encoder encoder, libsample_plugin_kref_com_itime_harmony_sample_Error value);
                } $serializer;
                struct {
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_sample_Error_Companion (*_instance)();
                  libsample_plugin_kref_kotlinx_serialization_KSerializer (*serializer)(libsample_plugin_kref_com_itime_harmony_sample_Error_Companion thiz);
                } Companion;
                libsample_plugin_KType* (*_type)(void);
                libsample_plugin_kref_com_itime_harmony_sample_Error (*Error)(const char* message);
                const char* (*get_message)(libsample_plugin_kref_com_itime_harmony_sample_Error thiz);
                const char* (*component1)(libsample_plugin_kref_com_itime_harmony_sample_Error thiz);
                libsample_plugin_kref_com_itime_harmony_sample_Error (*copy)(libsample_plugin_kref_com_itime_harmony_sample_Error thiz, const char* message);
                libsample_plugin_KBoolean (*equals)(libsample_plugin_kref_com_itime_harmony_sample_Error thiz, libsample_plugin_kref_kotlin_Any other);
                libsample_plugin_KInt (*hashCode)(libsample_plugin_kref_com_itime_harmony_sample_Error thiz);
                const char* (*toString)(libsample_plugin_kref_com_itime_harmony_sample_Error thiz);
              } Error;
              struct {
                libsample_plugin_KType* (*_type)(void);
                const char* (*greet)(libsample_plugin_kref_com_itime_harmony_sample_GreetingService thiz, const char* name);
              } GreetingService;
              struct {
                libsample_plugin_KType* (*_type)(void);
                libsample_plugin_kref_com_itime_harmony_sample_GreetingServiceImpl (*GreetingServiceImpl)();
                const char* (*greet)(libsample_plugin_kref_com_itime_harmony_sample_GreetingServiceImpl thiz, const char* name);
              } GreetingServiceImpl;
              struct {
                libsample_plugin_KType* (*_type)(void);
                libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin (*_instance)();
                libsample_plugin_KDouble (*add)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_KDouble a, libsample_plugin_KDouble b);
                const char* (*callTestInterface)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_com_itime_harmony_sample_TestInterface callback);
                libsample_plugin_kref_kotlin_collections_List (*getNullableList)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_KBoolean returnNull);
                libsample_plugin_kref_com_itime_harmony_sample_TestClass (*getTestClass)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz);
                const char* (*greet)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, const char* name);
                libsample_plugin_kref_com_itime_harmony_sample_MutableData (*modifyMutableData)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_com_itime_harmony_sample_MutableData data);
                libsample_plugin_kref_kotlin_Any (*processAny)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_kotlin_Any value);
                libsample_plugin_kref_kotlin_collections_Map (*processAnyMap)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_kotlin_collections_Map data);
                libsample_plugin_kref_kotlin_collections_List (*processBooleanList)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_kotlin_collections_List items);
                libsample_plugin_kref_com_itime_harmony_sample_DataSealed (*processDataSealed)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_com_itime_harmony_sample_DataSealed data);
                libsample_plugin_kref_kotlin_collections_List (*processDoubleList)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_kotlin_collections_List items);
                libsample_plugin_kref_kotlin_Any (*processExtremeAny)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_kotlin_Any data);
                libsample_plugin_kref_kotlin_collections_List (*processIntList)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_kotlin_collections_List items);
                libsample_plugin_kref_kotlin_collections_List (*processList)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_kotlin_collections_List items);
                libsample_plugin_kref_kotlin_collections_Map (*processMap)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_kotlin_collections_Map data);
                libsample_plugin_kref_kotlin_collections_MutableList (*processMutableList)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_kotlin_collections_MutableList items);
                libsample_plugin_kref_kotlin_collections_MutableMap (*processMutableMap)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_kotlin_collections_MutableMap data);
                const char* (*processNullableString)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, const char* name);
                libsample_plugin_kref_com_itime_harmony_sample_NetworkResult (*processResult)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_com_itime_harmony_sample_NetworkResult result);
                libsample_plugin_kref_kotlin_collections_Map (*processStringBooleanMap)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_kotlin_collections_Map data);
                libsample_plugin_kref_kotlin_collections_Map (*processStringDoubleMap)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_kotlin_collections_Map data);
                libsample_plugin_kref_kotlin_collections_Map (*processStringIntMap)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_kotlin_collections_Map data);
                libsample_plugin_kref_com_itime_harmony_sample_TreeNode (*processTree)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_com_itime_harmony_sample_TreeNode node);
                libsample_plugin_kref_com_itime_harmony_sample_TripleBox (*processTripleBox)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_com_itime_harmony_sample_TripleBox box);
                libsample_plugin_kref_com_itime_harmony_sample_User (*processUser)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_com_itime_harmony_sample_User user, libsample_plugin_kref_com_itime_harmony_sample_Role role);
                const char* (*throwKotlinException)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, const char* message);
              } HelloWorldPlugin;
              struct {
                libsample_plugin_KType* (*_type)(void);
                libsample_plugin_kref_com_itime_harmony_sample_IndexPresenter (*IndexPresenter)();
                void (*attach)(libsample_plugin_kref_com_itime_harmony_sample_IndexPresenter thiz, libsample_plugin_kref_com_itime_harmony_sample_IndexView view);
                void (*detach)(libsample_plugin_kref_com_itime_harmony_sample_IndexPresenter thiz);
                void (*showUser)(libsample_plugin_kref_com_itime_harmony_sample_IndexPresenter thiz, libsample_plugin_kref_com_itime_harmony_sample_User user);
              } IndexPresenter;
              struct {
                libsample_plugin_KType* (*_type)(void);
                void (*showStudent)(libsample_plugin_kref_com_itime_harmony_sample_IndexView thiz, libsample_plugin_kref_com_itime_harmony_sample_Student student);
                void (*showUser)(libsample_plugin_kref_com_itime_harmony_sample_IndexView thiz, const char* name);
              } IndexView;
              struct {
                libsample_plugin_KType* (*_type)(void);
                const char* (*getString)(libsample_plugin_kref_com_itime_harmony_sample_KeyValueStorage thiz, const char* key);
                void (*saveString)(libsample_plugin_kref_com_itime_harmony_sample_KeyValueStorage thiz, const char* key, const char* value);
              } KeyValueStorage;
              struct {
                libsample_plugin_KType* (*_type)(void);
                libsample_plugin_kref_com_itime_harmony_sample_KoinPlugin (*_instance)();
                const char* (*getGreeting)(libsample_plugin_kref_com_itime_harmony_sample_KoinPlugin thiz, const char* name);
                void (*initKoin)(libsample_plugin_kref_com_itime_harmony_sample_KoinPlugin thiz);
              } KoinPlugin;
              struct {
                libsample_plugin_KType* (*_type)(void);
                libsample_plugin_kref_com_itime_harmony_sample_KtorPlugin (*_instance)();
                void (*initKtor)(libsample_plugin_kref_com_itime_harmony_sample_KtorPlugin thiz, libsample_plugin_kref_com_itime_harmony_sample_ArkTsHttpFetcher fetcher);
                void (*onFetchError)(libsample_plugin_kref_com_itime_harmony_sample_KtorPlugin thiz, const char* requestId, const char* error);
                void (*onFetchSuccess)(libsample_plugin_kref_com_itime_harmony_sample_KtorPlugin thiz, const char* requestId, libsample_plugin_KInt status, const char* body);
              } KtorPlugin;
              struct {
                struct {
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_sample_MutableData_$serializer (*_instance)();
                  libsample_plugin_kref_kotlinx_serialization_descriptors_SerialDescriptor (*get_descriptor)(libsample_plugin_kref_com_itime_harmony_sample_MutableData_$serializer thiz);
                  libsample_plugin_kref_kotlin_Array (*childSerializers)(libsample_plugin_kref_com_itime_harmony_sample_MutableData_$serializer thiz);
                  libsample_plugin_kref_com_itime_harmony_sample_MutableData (*deserialize)(libsample_plugin_kref_com_itime_harmony_sample_MutableData_$serializer thiz, libsample_plugin_kref_kotlinx_serialization_encoding_Decoder decoder);
                  void (*serialize)(libsample_plugin_kref_com_itime_harmony_sample_MutableData_$serializer thiz, libsample_plugin_kref_kotlinx_serialization_encoding_Encoder encoder, libsample_plugin_kref_com_itime_harmony_sample_MutableData value);
                } $serializer;
                struct {
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_sample_MutableData_Companion (*_instance)();
                  libsample_plugin_kref_kotlinx_serialization_KSerializer (*serializer)(libsample_plugin_kref_com_itime_harmony_sample_MutableData_Companion thiz);
                } Companion;
                libsample_plugin_KType* (*_type)(void);
                libsample_plugin_kref_com_itime_harmony_sample_MutableData (*MutableData)(libsample_plugin_KInt id, const char* name, const char* readOnlyField);
                libsample_plugin_KInt (*get_id)(libsample_plugin_kref_com_itime_harmony_sample_MutableData thiz);
                void (*set_id)(libsample_plugin_kref_com_itime_harmony_sample_MutableData thiz, libsample_plugin_KInt set);
                const char* (*get_name)(libsample_plugin_kref_com_itime_harmony_sample_MutableData thiz);
                void (*set_name)(libsample_plugin_kref_com_itime_harmony_sample_MutableData thiz, const char* set);
                const char* (*get_readOnlyField)(libsample_plugin_kref_com_itime_harmony_sample_MutableData thiz);
                libsample_plugin_KInt (*component1)(libsample_plugin_kref_com_itime_harmony_sample_MutableData thiz);
                const char* (*component2)(libsample_plugin_kref_com_itime_harmony_sample_MutableData thiz);
                const char* (*component3)(libsample_plugin_kref_com_itime_harmony_sample_MutableData thiz);
                libsample_plugin_kref_com_itime_harmony_sample_MutableData (*copy)(libsample_plugin_kref_com_itime_harmony_sample_MutableData thiz, libsample_plugin_KInt id, const char* name, const char* readOnlyField);
                libsample_plugin_KBoolean (*equals)(libsample_plugin_kref_com_itime_harmony_sample_MutableData thiz, libsample_plugin_kref_kotlin_Any other);
                libsample_plugin_KInt (*hashCode)(libsample_plugin_kref_com_itime_harmony_sample_MutableData thiz);
                const char* (*toString)(libsample_plugin_kref_com_itime_harmony_sample_MutableData thiz);
              } MutableData;
              struct {
                struct {
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_sample_NetworkResult_Companion (*_instance)();
                  libsample_plugin_kref_kotlinx_serialization_KSerializer (*serializer)(libsample_plugin_kref_com_itime_harmony_sample_NetworkResult_Companion thiz);
                  libsample_plugin_kref_kotlinx_serialization_KSerializer (*serializer_)(libsample_plugin_kref_com_itime_harmony_sample_NetworkResult_Companion thiz, libsample_plugin_kref_kotlin_Array typeParamsSerializers);
                } Companion;
                libsample_plugin_KType* (*_type)(void);
                libsample_plugin_kref_com_itime_harmony_sample_NetworkResult (*NetworkResult)(libsample_plugin_KInt seen1, libsample_plugin_kref_kotlinx_serialization_internal_SerializationConstructorMarker serializationConstructorMarker);
                libsample_plugin_kref_com_itime_harmony_sample_NetworkResult (*NetworkResult_)();
              } NetworkResult;
              struct {
                struct {
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_sample_PageState_Companion (*_instance)();
                  libsample_plugin_kref_kotlinx_serialization_KSerializer (*serializer)(libsample_plugin_kref_com_itime_harmony_sample_PageState_Companion thiz);
                  libsample_plugin_kref_kotlinx_serialization_KSerializer (*serializer_)(libsample_plugin_kref_com_itime_harmony_sample_PageState_Companion thiz, libsample_plugin_kref_kotlin_Array typeParamsSerializers);
                } Companion;
                struct {
                  struct {
                    libsample_plugin_KType* (*_type)(void);
                    libsample_plugin_kref_com_itime_harmony_sample_PageState_Error_$serializer (*_instance)();
                    libsample_plugin_kref_kotlinx_serialization_descriptors_SerialDescriptor (*get_descriptor)(libsample_plugin_kref_com_itime_harmony_sample_PageState_Error_$serializer thiz);
                    libsample_plugin_kref_kotlin_Array (*childSerializers)(libsample_plugin_kref_com_itime_harmony_sample_PageState_Error_$serializer thiz);
                    libsample_plugin_kref_com_itime_harmony_sample_PageState_Error (*deserialize)(libsample_plugin_kref_com_itime_harmony_sample_PageState_Error_$serializer thiz, libsample_plugin_kref_kotlinx_serialization_encoding_Decoder decoder);
                    void (*serialize)(libsample_plugin_kref_com_itime_harmony_sample_PageState_Error_$serializer thiz, libsample_plugin_kref_kotlinx_serialization_encoding_Encoder encoder, libsample_plugin_kref_com_itime_harmony_sample_PageState_Error value);
                  } $serializer;
                  struct {
                    libsample_plugin_KType* (*_type)(void);
                    libsample_plugin_kref_com_itime_harmony_sample_PageState_Error_Companion (*_instance)();
                    libsample_plugin_kref_kotlinx_serialization_KSerializer (*serializer)(libsample_plugin_kref_com_itime_harmony_sample_PageState_Error_Companion thiz);
                  } Companion;
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_sample_PageState_Error (*Error)(const char* message);
                  const char* (*get_message)(libsample_plugin_kref_com_itime_harmony_sample_PageState_Error thiz);
                  const char* (*component1)(libsample_plugin_kref_com_itime_harmony_sample_PageState_Error thiz);
                  libsample_plugin_kref_com_itime_harmony_sample_PageState_Error (*copy)(libsample_plugin_kref_com_itime_harmony_sample_PageState_Error thiz, const char* message);
                  libsample_plugin_KBoolean (*equals)(libsample_plugin_kref_com_itime_harmony_sample_PageState_Error thiz, libsample_plugin_kref_kotlin_Any other);
                  libsample_plugin_KInt (*hashCode)(libsample_plugin_kref_com_itime_harmony_sample_PageState_Error thiz);
                  const char* (*toString)(libsample_plugin_kref_com_itime_harmony_sample_PageState_Error thiz);
                } Error;
                struct {
                  struct {
                    libsample_plugin_KType* (*_type)(void);
                    libsample_plugin_kref_com_itime_harmony_sample_PageState_Loading_$serializer (*_instance)();
                    libsample_plugin_kref_kotlinx_serialization_descriptors_SerialDescriptor (*get_descriptor)(libsample_plugin_kref_com_itime_harmony_sample_PageState_Loading_$serializer thiz);
                    libsample_plugin_kref_kotlin_Array (*childSerializers)(libsample_plugin_kref_com_itime_harmony_sample_PageState_Loading_$serializer thiz);
                    libsample_plugin_kref_com_itime_harmony_sample_PageState_Loading (*deserialize)(libsample_plugin_kref_com_itime_harmony_sample_PageState_Loading_$serializer thiz, libsample_plugin_kref_kotlinx_serialization_encoding_Decoder decoder);
                    void (*serialize)(libsample_plugin_kref_com_itime_harmony_sample_PageState_Loading_$serializer thiz, libsample_plugin_kref_kotlinx_serialization_encoding_Encoder encoder, libsample_plugin_kref_com_itime_harmony_sample_PageState_Loading value);
                  } $serializer;
                  struct {
                    libsample_plugin_KType* (*_type)(void);
                    libsample_plugin_kref_com_itime_harmony_sample_PageState_Loading_Companion (*_instance)();
                    libsample_plugin_kref_kotlinx_serialization_KSerializer (*serializer)(libsample_plugin_kref_com_itime_harmony_sample_PageState_Loading_Companion thiz);
                  } Companion;
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_sample_PageState_Loading (*Loading)(libsample_plugin_KBoolean isRefreshing);
                  libsample_plugin_KBoolean (*get_isRefreshing)(libsample_plugin_kref_com_itime_harmony_sample_PageState_Loading thiz);
                  libsample_plugin_KBoolean (*component1)(libsample_plugin_kref_com_itime_harmony_sample_PageState_Loading thiz);
                  libsample_plugin_kref_com_itime_harmony_sample_PageState_Loading (*copy)(libsample_plugin_kref_com_itime_harmony_sample_PageState_Loading thiz, libsample_plugin_KBoolean isRefreshing);
                  libsample_plugin_KBoolean (*equals)(libsample_plugin_kref_com_itime_harmony_sample_PageState_Loading thiz, libsample_plugin_kref_kotlin_Any other);
                  libsample_plugin_KInt (*hashCode)(libsample_plugin_kref_com_itime_harmony_sample_PageState_Loading thiz);
                  const char* (*toString)(libsample_plugin_kref_com_itime_harmony_sample_PageState_Loading thiz);
                } Loading;
                libsample_plugin_KType* (*_type)(void);
                libsample_plugin_kref_com_itime_harmony_sample_PageState (*PageState)(libsample_plugin_KInt seen1, libsample_plugin_kref_kotlinx_serialization_internal_SerializationConstructorMarker serializationConstructorMarker);
                libsample_plugin_kref_com_itime_harmony_sample_PageState (*PageState_)();
              } PageState;
              struct {
                struct {
                  libsample_plugin_kref_com_itime_harmony_sample_Role (*get)(); /* enum entry for ADMIN. */
                } ADMIN;
                struct {
                  libsample_plugin_kref_com_itime_harmony_sample_Role (*get)(); /* enum entry for USER. */
                } USER;
                libsample_plugin_KType* (*_type)(void);
              } Role;
              struct {
                libsample_plugin_KType* (*_type)(void);
                libsample_plugin_kref_com_itime_harmony_sample_SettingsRepository (*SettingsRepository)(libsample_plugin_kref_com_itime_harmony_sample_KeyValueStorage storage);
                const char* (*getTheme)(libsample_plugin_kref_com_itime_harmony_sample_SettingsRepository thiz);
                void (*saveTheme)(libsample_plugin_kref_com_itime_harmony_sample_SettingsRepository thiz, const char* theme);
              } SettingsRepository;
              struct {
                libsample_plugin_KType* (*_type)(void);
                libsample_plugin_kref_com_itime_harmony_sample_StoragePlugin (*_instance)();
                void (*initArkTsStorage)(libsample_plugin_kref_com_itime_harmony_sample_StoragePlugin thiz, libsample_plugin_kref_com_itime_harmony_sample_KeyValueStorage storageImpl);
                const char* (*testStorageRoundTrip)(libsample_plugin_kref_com_itime_harmony_sample_StoragePlugin thiz);
              } StoragePlugin;
              struct {
                struct {
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_sample_Success_$serializer (*_instance)();
                  libsample_plugin_kref_kotlinx_serialization_descriptors_SerialDescriptor (*get_descriptor)(libsample_plugin_kref_com_itime_harmony_sample_Success_$serializer thiz);
                  libsample_plugin_kref_kotlin_Array (*childSerializers)(libsample_plugin_kref_com_itime_harmony_sample_Success_$serializer thiz);
                  libsample_plugin_kref_com_itime_harmony_sample_Success (*deserialize)(libsample_plugin_kref_com_itime_harmony_sample_Success_$serializer thiz, libsample_plugin_kref_kotlinx_serialization_encoding_Decoder decoder);
                  void (*serialize)(libsample_plugin_kref_com_itime_harmony_sample_Success_$serializer thiz, libsample_plugin_kref_kotlinx_serialization_encoding_Encoder encoder, libsample_plugin_kref_com_itime_harmony_sample_Success value);
                } $serializer;
                struct {
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_sample_Success_Companion (*_instance)();
                  libsample_plugin_kref_kotlinx_serialization_KSerializer (*serializer)(libsample_plugin_kref_com_itime_harmony_sample_Success_Companion thiz);
                } Companion;
                libsample_plugin_KType* (*_type)(void);
                libsample_plugin_kref_com_itime_harmony_sample_Success (*Success)(const char* data);
                const char* (*get_data)(libsample_plugin_kref_com_itime_harmony_sample_Success thiz);
                const char* (*component1)(libsample_plugin_kref_com_itime_harmony_sample_Success thiz);
                libsample_plugin_kref_com_itime_harmony_sample_Success (*copy)(libsample_plugin_kref_com_itime_harmony_sample_Success thiz, const char* data);
                libsample_plugin_KBoolean (*equals)(libsample_plugin_kref_com_itime_harmony_sample_Success thiz, libsample_plugin_kref_kotlin_Any other);
                libsample_plugin_KInt (*hashCode)(libsample_plugin_kref_com_itime_harmony_sample_Success thiz);
                const char* (*toString)(libsample_plugin_kref_com_itime_harmony_sample_Success thiz);
              } Success;
              struct {
                libsample_plugin_KType* (*_type)(void);
                libsample_plugin_kref_com_itime_harmony_sample_TestClass (*TestClass)(libsample_plugin_KInt value);
                libsample_plugin_KInt (*fetchValue)(libsample_plugin_kref_com_itime_harmony_sample_TestClass thiz);
                void (*increment)(libsample_plugin_kref_com_itime_harmony_sample_TestClass thiz);
              } TestClass;
              struct {
                libsample_plugin_KType* (*_type)(void);
                const char* (*sayHello)(libsample_plugin_kref_com_itime_harmony_sample_TestInterface thiz, const char* name);
              } TestInterface;
              struct {
                struct {
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_sample_TreeNode_$serializer (*_instance)();
                  libsample_plugin_kref_kotlinx_serialization_descriptors_SerialDescriptor (*get_descriptor)(libsample_plugin_kref_com_itime_harmony_sample_TreeNode_$serializer thiz);
                  libsample_plugin_kref_kotlin_Array (*childSerializers)(libsample_plugin_kref_com_itime_harmony_sample_TreeNode_$serializer thiz);
                  libsample_plugin_kref_com_itime_harmony_sample_TreeNode (*deserialize)(libsample_plugin_kref_com_itime_harmony_sample_TreeNode_$serializer thiz, libsample_plugin_kref_kotlinx_serialization_encoding_Decoder decoder);
                  void (*serialize)(libsample_plugin_kref_com_itime_harmony_sample_TreeNode_$serializer thiz, libsample_plugin_kref_kotlinx_serialization_encoding_Encoder encoder, libsample_plugin_kref_com_itime_harmony_sample_TreeNode value);
                } $serializer;
                struct {
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_sample_TreeNode_Companion (*_instance)();
                  libsample_plugin_kref_kotlinx_serialization_KSerializer (*serializer)(libsample_plugin_kref_com_itime_harmony_sample_TreeNode_Companion thiz);
                } Companion;
                libsample_plugin_KType* (*_type)(void);
                libsample_plugin_kref_com_itime_harmony_sample_TreeNode (*TreeNode)(const char* value, libsample_plugin_kref_kotlin_collections_List children);
                libsample_plugin_kref_kotlin_collections_List (*get_children)(libsample_plugin_kref_com_itime_harmony_sample_TreeNode thiz);
                const char* (*get_value)(libsample_plugin_kref_com_itime_harmony_sample_TreeNode thiz);
                const char* (*component1)(libsample_plugin_kref_com_itime_harmony_sample_TreeNode thiz);
                libsample_plugin_kref_kotlin_collections_List (*component2)(libsample_plugin_kref_com_itime_harmony_sample_TreeNode thiz);
                libsample_plugin_kref_com_itime_harmony_sample_TreeNode (*copy)(libsample_plugin_kref_com_itime_harmony_sample_TreeNode thiz, const char* value, libsample_plugin_kref_kotlin_collections_List children);
                libsample_plugin_KBoolean (*equals)(libsample_plugin_kref_com_itime_harmony_sample_TreeNode thiz, libsample_plugin_kref_kotlin_Any other);
                libsample_plugin_KInt (*hashCode)(libsample_plugin_kref_com_itime_harmony_sample_TreeNode thiz);
                const char* (*toString)(libsample_plugin_kref_com_itime_harmony_sample_TreeNode thiz);
              } TreeNode;
              struct {
                struct {
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_sample_User_$serializer (*_instance)();
                  libsample_plugin_kref_kotlinx_serialization_descriptors_SerialDescriptor (*get_descriptor)(libsample_plugin_kref_com_itime_harmony_sample_User_$serializer thiz);
                  libsample_plugin_kref_kotlin_Array (*childSerializers)(libsample_plugin_kref_com_itime_harmony_sample_User_$serializer thiz);
                  libsample_plugin_kref_com_itime_harmony_sample_User (*deserialize)(libsample_plugin_kref_com_itime_harmony_sample_User_$serializer thiz, libsample_plugin_kref_kotlinx_serialization_encoding_Decoder decoder);
                  void (*serialize)(libsample_plugin_kref_com_itime_harmony_sample_User_$serializer thiz, libsample_plugin_kref_kotlinx_serialization_encoding_Encoder encoder, libsample_plugin_kref_com_itime_harmony_sample_User value);
                } $serializer;
                struct {
                  libsample_plugin_KType* (*_type)(void);
                  libsample_plugin_kref_com_itime_harmony_sample_User_Companion (*_instance)();
                  libsample_plugin_kref_kotlinx_serialization_KSerializer (*serializer)(libsample_plugin_kref_com_itime_harmony_sample_User_Companion thiz);
                } Companion;
                libsample_plugin_KType* (*_type)(void);
                libsample_plugin_kref_com_itime_harmony_sample_User (*User)(const char* name, libsample_plugin_KInt age);
                libsample_plugin_KInt (*get_age)(libsample_plugin_kref_com_itime_harmony_sample_User thiz);
                const char* (*get_name)(libsample_plugin_kref_com_itime_harmony_sample_User thiz);
                const char* (*component1)(libsample_plugin_kref_com_itime_harmony_sample_User thiz);
                libsample_plugin_KInt (*component2)(libsample_plugin_kref_com_itime_harmony_sample_User thiz);
                libsample_plugin_kref_com_itime_harmony_sample_User (*copy)(libsample_plugin_kref_com_itime_harmony_sample_User thiz, const char* name, libsample_plugin_KInt age);
                libsample_plugin_KBoolean (*equals)(libsample_plugin_kref_com_itime_harmony_sample_User thiz, libsample_plugin_kref_kotlin_Any other);
                const char* (*getGreeting)(libsample_plugin_kref_com_itime_harmony_sample_User thiz);
                libsample_plugin_KInt (*hashCode)(libsample_plugin_kref_com_itime_harmony_sample_User thiz);
                const char* (*sayHi)(libsample_plugin_kref_com_itime_harmony_sample_User thiz, const char* to);
                const char* (*toString)(libsample_plugin_kref_com_itime_harmony_sample_User thiz);
              } User;
              libsample_plugin_kref_org_koin_core_module_Module (*get_appModule)();
              const char* (*getFullName)(libsample_plugin_kref_com_itime_harmony_sample_User thiz);
            } sample;
          } harmony;
        } itime;
      } com;
      libsample_plugin_kref_org_koin_core_module_Module (*get_m1)();
      void (*test)();
      void (*test2)();
      void (*test3)();
    } root;
  } kotlin;
} libsample_plugin_ExportedSymbols;
extern libsample_plugin_ExportedSymbols* libsample_plugin_symbols(void);
#ifdef __cplusplus
}  /* extern "C" */
#endif
#endif  /* KONAN_LIBSAMPLE_PLUGIN_H */
