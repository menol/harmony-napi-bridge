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
} libsample_plugin_kref_com_itime_harmony_sample_User;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_BasePageState;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_BaseView;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_DemoAbstract;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlin_collections_List;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_TestClass;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlin_Any;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlin_collections_Map;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_NetworkResult;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_Role;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_IndexView;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_PageState;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlinx_serialization_internal_SerializationConstructorMarker;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_PageState_Companion;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlinx_serialization_KSerializer;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlin_Array;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_PageState_Error;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_com_itime_harmony_sample_PageState_Error_$serializer;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlinx_serialization_descriptors_SerialDescriptor;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlinx_serialization_encoding_Decoder;
typedef struct {
  libsample_plugin_KNativePtr pinned;
} libsample_plugin_kref_kotlinx_serialization_encoding_Encoder;
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
} libsample_plugin_kref_com_itime_harmony_sample_TestInterface;
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
                void* (*DemoAbstract_constructor)(void* env, void* info);
                void (*DemoAbstract_finalize)(void* env, void* data, void* hint);
                void* (*DemoAbstract_process_wrapper)(void* env, void* info);
                void* (*DemoAbstract_sayHello_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_add_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_getTestClass_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_greet_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processAnyMap_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processAny_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processBooleanList_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processDoubleList_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processIntList_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processList_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processMap_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processResult_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processStringBooleanMap_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processStringDoubleMap_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processStringIntMap_wrapper)(void* env, void* info);
                void* (*HelloWorldPlugin_processUser_wrapper)(void* env, void* info);
                void* (*InitGeneratedWrappers_)(void* env, void* exports);
                void* (*TestAbstract_constructor)(void* env, void* info);
                void (*TestAbstract_finalize)(void* env, void* data, void* hint);
                void* (*TestAbstract_process_wrapper)(void* env, void* info);
                void* (*TestAbstract_sayHello_wrapper)(void* env, void* info);
                void* (*TestClass_constructor)(void* env, void* info);
                void (*TestClass_finalize)(void* env, void* data, void* hint);
                void* (*TestClass_getValue_wrapper)(void* env, void* info);
                void* (*TestClass_increment_wrapper)(void* env, void* info);
                void* (*UserUtils_getFullName_wrapper)(void* env, void* info);
              } generated;
            } napi;
            struct {
              struct {
                libsample_plugin_KType* (*_type)(void);
              } BasePageState;
              struct {
                libsample_plugin_KType* (*_type)(void);
              } BaseView;
              struct {
                libsample_plugin_KType* (*_type)(void);
                libsample_plugin_kref_com_itime_harmony_sample_DemoAbstract (*DemoAbstract)();
                libsample_plugin_kref_kotlin_collections_List (*process)(libsample_plugin_kref_com_itime_harmony_sample_DemoAbstract thiz, libsample_plugin_kref_kotlin_collections_List item);
                const char* (*sayHello)(libsample_plugin_kref_com_itime_harmony_sample_DemoAbstract thiz);
              } DemoAbstract;
              struct {
                libsample_plugin_KType* (*_type)(void);
                libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin (*_instance)();
                libsample_plugin_KDouble (*add)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_KDouble a, libsample_plugin_KDouble b);
                libsample_plugin_kref_com_itime_harmony_sample_TestClass (*getTestClass)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz);
                const char* (*greet)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, const char* name);
                libsample_plugin_kref_kotlin_Any (*processAny)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_kotlin_Any value);
                libsample_plugin_kref_kotlin_collections_Map (*processAnyMap)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_kotlin_collections_Map data);
                libsample_plugin_kref_kotlin_collections_List (*processBooleanList)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_kotlin_collections_List items);
                libsample_plugin_kref_kotlin_collections_List (*processDoubleList)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_kotlin_collections_List items);
                libsample_plugin_kref_kotlin_collections_List (*processIntList)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_kotlin_collections_List items);
                libsample_plugin_kref_kotlin_collections_List (*processList)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_kotlin_collections_List items);
                libsample_plugin_kref_kotlin_collections_Map (*processMap)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_kotlin_collections_Map data);
                libsample_plugin_kref_com_itime_harmony_sample_NetworkResult (*processResult)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_com_itime_harmony_sample_NetworkResult result);
                libsample_plugin_kref_kotlin_collections_Map (*processStringBooleanMap)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_kotlin_collections_Map data);
                libsample_plugin_kref_kotlin_collections_Map (*processStringDoubleMap)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_kotlin_collections_Map data);
                libsample_plugin_kref_kotlin_collections_Map (*processStringIntMap)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_kotlin_collections_Map data);
                libsample_plugin_kref_com_itime_harmony_sample_User (*processUser)(libsample_plugin_kref_com_itime_harmony_sample_HelloWorldPlugin thiz, libsample_plugin_kref_com_itime_harmony_sample_User user, libsample_plugin_kref_com_itime_harmony_sample_Role role);
              } HelloWorldPlugin;
              struct {
                libsample_plugin_KType* (*_type)(void);
                void (*showUser)(libsample_plugin_kref_com_itime_harmony_sample_IndexView thiz, const char* name);
              } IndexView;
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
                libsample_plugin_kref_com_itime_harmony_sample_TestClass (*TestClass)(libsample_plugin_KInt value);
                libsample_plugin_KInt (*getValue)(libsample_plugin_kref_com_itime_harmony_sample_TestClass thiz);
                void (*increment)(libsample_plugin_kref_com_itime_harmony_sample_TestClass thiz);
              } TestClass;
              struct {
                libsample_plugin_KType* (*_type)(void);
                const char* (*sayHello)(libsample_plugin_kref_com_itime_harmony_sample_TestInterface thiz, const char* name);
              } TestInterface;
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
                const char* (*toString)(libsample_plugin_kref_com_itime_harmony_sample_User thiz);
              } User;
              const char* (*getFullName)(libsample_plugin_kref_com_itime_harmony_sample_User thiz);
            } sample;
          } harmony;
        } itime;
      } com;
    } root;
  } kotlin;
} libsample_plugin_ExportedSymbols;
extern libsample_plugin_ExportedSymbols* libsample_plugin_symbols(void);
#ifdef __cplusplus
}  /* extern "C" */
#endif
#endif  /* KONAN_LIBSAMPLE_PLUGIN_H */
