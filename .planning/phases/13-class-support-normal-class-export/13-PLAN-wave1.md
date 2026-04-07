---
wave: 1
depends_on: []
files_modified:
  - "harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/models/Models.kt"
  - "harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/HarmonyNapiProcessor.kt"
  - "harmony-napi-runtime/src/linuxArm64Main/kotlin/com.realtech/harmony/napi/runtime/utils/NapiUtils.kt"
autonomous: true
---

# Phase 13 Plan: Support Normal Class Export

## Goal
Support exporting Kotlin normal classes (not object, not abstract) to TypeScript and NAPI, allowing JS to instantiate them and call their instance methods.

## Requirements
- TBD

## Must Haves
- Domain models successfully identify whether a Kotlin declaration is an `object` or a normal class.
- The runtime handles wrapping instances transparently when constructor callbacks are invoked.

## Verification Criteria
- `Models.kt` and `HarmonyNapiProcessor.kt` update correctly and capture `isObject`.
- `NapiUtils.kt` correctly passes `napi_external` rather than performing duplicate wrapping.

## Tasks

### Wave 1: Domain Models and Runtime Updates

<task>
  <id>13-01</id>
  <description>Update Domain Models to distinguish singletons and capture constructor parameters</description>
  <read_first>
    - harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/models/Models.kt
    - harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/HarmonyNapiProcessor.kt
  </read_first>
  <action>
    Modify `HarmonyModuleModel` in `Models.kt` to include `val isObject: Boolean = false` and `val primaryConstructorParams: List<HarmonyParameterModel> = emptyList()`.
    Update `HarmonyNapiProcessor.kt` to extract these values: `isObject = classDecl.classKind == ClassKind.OBJECT` and map `classDecl.primaryConstructor?.parameters` to `HarmonyParameterModel` instances (similar to how method parameters are mapped).
  </action>
  <acceptance_criteria>
    - `grep -q "val isObject: Boolean" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/models/Models.kt`
    - `grep -q "val primaryConstructorParams" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/models/Models.kt`
    - `grep -q "isObject =" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/HarmonyNapiProcessor.kt`
    - `grep -q "primaryConstructorParams =" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/HarmonyNapiProcessor.kt`
  </acceptance_criteria>
</task>

<task>
  <id>13-02</id>
  <description>Refactor NapiUtils for object wrapping and external constructors</description>
  <read_first>
    - harmony-napi-runtime/src/linuxArm64Main/kotlin/com.realtech/harmony/napi/runtime/utils/NapiUtils.kt
  </read_first>
  <action>
    Update `toNapiWrappedObject` to pass the `StableRef` pointer as a `napi_external` argument to `napi_new_instance`.
    Remove the manual `napi_wrap` from `toNapiWrappedObject`, as the constructor callback will handle wrapping.
    Specifically:
    ```kotlin
    val stableRef = StableRef.create(this@toNapiWrappedObject)
    val externalPtr = alloc<napi_valueVar>()
    napi_create_external(env, stableRef.asCPointer(), null, null, externalPtr.ptr)
    val args = allocArray<napi_valueVar>(1)
    args[0] = externalPtr.value!!
    napi_new_instance(env, constructorVar.value!!, 1u.convert(), args, jsInstanceVar.ptr)
    ```
  </action>
  <acceptance_criteria>
    - `grep -q "napi_create_external" harmony-napi-runtime/src/linuxArm64Main/kotlin/com.realtech/harmony/napi/runtime/utils/NapiUtils.kt`
    - `grep -v -q "napi_wrap" harmony-napi-runtime/src/linuxArm64Main/kotlin/com.realtech/harmony/napi/runtime/utils/NapiUtils.kt` (inside `toNapiWrappedObject`)
  </acceptance_criteria>
</task>
<threat_model>
  - Block on: high
  - Threats:
    - **Memory Leaks**: `StableRef.dispose()` is crucial in the constructor `napi_wrap` finalize callback to prevent memory leaks.
    - **Wrapper Conflicts**: Double wrapping could crash the JS runtime if `napi_external` logic fails.
</threat_model>
