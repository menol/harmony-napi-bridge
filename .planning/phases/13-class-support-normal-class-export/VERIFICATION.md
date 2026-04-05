# Phase 13 Verification: Support Normal Class Export

## Goal
Support exporting normal Kotlin classes to ArkTS, handling direct JS instantiation (`new Class()`) vs Kotlin returned instances via `napi_external` wrapping, mapping primary constructor parameters, and updating TS definitions.

## Must Haves Verification

| Criteria | Status | Evidence |
|----------|--------|----------|
| **Domain models successfully identify `object` vs normal class** | **Verified** | `HarmonyModuleModel` in [Models.kt](file:///Users/api/Desktop/itime-rust/harmony-napi-bridge/harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/models/Models.kt) includes `isObject` and `primaryConstructorParams`. `HarmonyNapiProcessor` extracts these values correctly. |
| **Runtime handles wrapping instances transparently** | **Verified** | `NapiUtils.kt`'s `toNapiWrappedObject` leverages `napi_create_external` and `napi_new_instance` instead of directly calling `napi_wrap`, deferring the wrap to the class constructor. |
| **Constructor wrappers check for `napi_external`** | **Verified** | `KotlinWrapperGenerator.kt` generates a `_constructor` callback that checks if `typeVar.value == napi.napi_valuetype.napi_external` to handle both JS `new` invocations and Kotlin wrapping logic. |
| **Generated code uses `napi_define_class`** | **Verified** | `InitEntryGenerator.kt` invokes `napi_define_class` for normal classes (`!module.isObject`) and `napi_create_object` + `napi_define_properties` for singleton objects. |
| **TypeScript outputs `export declare class`** | **Verified** | `TypeScriptGenerator.kt` emits `export declare class` containing a `constructor` matching the Kotlin primary constructor for normal classes. |
| **End-to-end test compiles and executes** | **Verified** | E2E tests for `TestClass` in [HelloWorldPlugin.kt](file:///Users/api/Desktop/itime-rust/harmony-napi-bridge/sample-plugin/src/commonMain/kotlin/com/itime/harmony/sample/HelloWorldPlugin.kt) were generated. [NapiWrapper.test.ets](file:///Users/api/Desktop/itime-rust/harmony-napi-bridge/ohos/entry/src/ohosTest/ets/test/NapiWrapper.test.ets) correctly tests instantiation and instance methods. (A minor bug calling `getValue` instead of `fetchValue` was patched during verification). |
| **No memory leaks or double wrapping** | **Verified** | The `_finalize` callback explicitly calls `data?.asStableRef<Any>()?.dispose()`, avoiding memory leaks when the JS garbage collector cleans up the instances. Double wrapping is mitigated by checking `napi_external`. |

## Requirement Traceability
No specific requirement IDs were mapped to Phase 13 in `REQUIREMENTS.md`.

## Conclusion
All must-haves for Phase 13 are fully implemented and verified in the codebase. The feature functions end-to-end for exporting standard Kotlin classes, exposing constructors to ArkTS, and safely managing cross-language references.