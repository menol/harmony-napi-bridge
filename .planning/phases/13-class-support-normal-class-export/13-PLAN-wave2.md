---
wave: 2
depends_on: ["13-01", "13-02"]
files_modified:
  - "harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt"
  - "harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/InitEntryGenerator.kt"
  - "harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/TypeScriptGenerator.kt"
autonomous: true
---

# Phase 13 Plan: Generators Updates

## Goal
Update KSP generators to generate NAPI wrapper code, initialization code, and TypeScript definitions for normal classes.

## Requirements
- TBD

## Must Haves
- Constructor wrappers check for `napi_external` arguments to differentiate JS instantiation from Kotlin object wrapping.
- Generated code uses `napi_define_class` for normal classes and limits `napi_create_object` to objects.
- TypeScript outputs `export declare class` with constructors.

## Verification Criteria
- `KotlinWrapperGenerator.kt` outputs the correct branch for constructor callbacks and instance methods.
- `InitEntryGenerator.kt` correctly registers normal classes in the module.
- `TypeScriptGenerator.kt` emits valid TS definitions matching Kotlin classes.

## Tasks

### Wave 2: Generators Updates

<task>
  <id>13-03</id>
  <description>Refactor KotlinWrapperGenerator for normal classes</description>
  <read_first>
    - harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt
  </read_first>
  <action>
    Update `KotlinWrapperGenerator.kt` to generate constructor wrappers and instance methods for normal classes (`isObject == false`).
    Modify the constructor generation to check for `napi_external` (from Kotlin `toNapiWrappedObject`).
    If external, unwrap it via `napi_get_value_external` and wrap the instance with `napi_wrap`.
    If not external (called via JS `new`), unpack JS args, call the primary constructor to create the Kotlin instance, wrap it via `napi_wrap`, and return it.
    For instance methods, extract the `instance` from `thisVar` using `unwrapKotlinObject` (same as abstract classes) and invoke methods on `instance` instead of making static calls on `module.className`.
  </action>
  <acceptance_criteria>
    - `grep -q "napi_external" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt`
    - `grep -q "napi_wrap" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt`
    - `grep -q "unwrapKotlinObject" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt`
  </acceptance_criteria>
</task>

<task>
  <id>13-04</id>
  <description>Update InitEntryGenerator for normal classes</description>
  <read_first>
    - harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/InitEntryGenerator.kt
  </read_first>
  <action>
    Update `InitEntryGenerator.kt` to use `napi_define_class` and `ConstructorRegistry.refs.put` for normal classes (`isObject == false`), similar to what is currently done for abstract classes.
    Ensure `napi_create_object` and `napi_define_properties` are strictly limited to `isObject == true` (singletons).
  </action>
  <acceptance_criteria>
    - `grep -q "isObject" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/InitEntryGenerator.kt`
    - Code correctly differentiates between object and non-object for `napi_define_class` vs `napi_create_object`.
  </acceptance_criteria>
</task>

<task>
  <id>13-05</id>
  <description>Update TypeScriptGenerator for normal classes</description>
  <read_first>
    - harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/TypeScriptGenerator.kt
  </read_first>
  <action>
    Update `TypeScriptGenerator.kt` to output `export declare class` instead of `export declare namespace` for normal classes (`isObject == false`).
    Inside the class definition, output a `constructor` signature mapped from `primaryConstructorParams`.
    Output instance methods inside the class.
    Keep `export declare namespace` exclusively for `isObject == true` singletons.
  </action>
  <acceptance_criteria>
    - `grep -q "export declare class" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/TypeScriptGenerator.kt`
    - `grep -q "constructor" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/TypeScriptGenerator.kt`
  </acceptance_criteria>
</task>
<threat_model>
  - Block on: high
  - Threats:
    - **Memory Leaks**: `StableRef.dispose()` is crucial in the constructor `napi_wrap` finalize callback to prevent memory leaks.
    - **Wrapper Conflicts**: Double wrapping could crash the JS runtime if `napi_external` logic fails.
</threat_model>
