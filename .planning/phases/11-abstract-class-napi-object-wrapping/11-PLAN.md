---
wave: 1
depends_on: []
files_modified:
  - harmony-napi-runtime/src/linuxArm64Main/kotlin/com/itime/harmony/napi/runtime/utils/NapiUtils.kt
  - harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt
  - harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/InitEntryGenerator.kt
  - harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/mapper/TypeMapper.kt
  - harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/TypeScriptGenerator.kt
autonomous: true
---

# Phase 11: Abstract Class NAPI Object Wrapping

## Requirements
- TBD

## Goal
Support exporting Kotlin abstract classes with generics to TypeScript abstract classes, and implement NAPI Object Wrapping to pass instances by reference between Kotlin and JS.

## Verification Criteria
- [ ] `NapiUtils.kt` contains `wrapKotlinObject` and `unwrapKotlinObject` extensions.
- [ ] `KotlinWrapperGenerator` generates class constructor wrappers and instance method wrappers for `isAbstract` modules.
- [ ] `InitEntryGenerator` uses `napi_define_class` for abstract classes and stores their `napi_ref`s.
- [ ] `TypeMapper` returns `toNapiWrappedObject` and `unwrapKotlinObject` for abstract classes.
- [ ] `TypeScriptGenerator` exports abstract classes correctly.

## Must Haves
- Correct StableRef memory management (finalizer).
- Global registry for `napi_ref` to constructors.

<threat_model>
- Memory Leaks: Finalizers MUST properly dispose of `StableRef`s to prevent memory leaks in Kotlin ARC. Block on: high.
</threat_model>

## Tasks

<task>
<read_first>
- harmony-napi-runtime/src/linuxArm64Main/kotlin/com/itime/harmony/napi/runtime/utils/NapiUtils.kt
</read_first>
<action>
Add `ConstructorRegistry` object to `NapiUtils.kt` to store `napi_ref` values:
```kotlin
object ConstructorRegistry {
    val refs = mutableMapOf<String, napi_ref>()
}
```
Add `wrapKotlinObject` extension to `Any` that uses `ConstructorRegistry`, `napi_new_instance`, and `napi_wrap` to bind a Kotlin object to a JS instance using `StableRef`. Ensure finalizer calls `StableRef.fromCPointer<Any>(it).dispose()`.
Add `unwrapKotlinObject` extension to `napi_value` that uses `napi_unwrap` to retrieve the Kotlin object.
</action>
<acceptance_criteria>
- `NapiUtils.kt` contains `object ConstructorRegistry` with `refs` map.
- `NapiUtils.kt` contains `fun Any.toNapiWrappedObject(env: napi_env, className: String): napi_value`
- `NapiUtils.kt` contains `inline fun <reified T : Any> napi_value.unwrapKotlinObject(env: napi_env): T`
</acceptance_criteria>
</task>

<task>
<read_first>
- harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt
</read_first>
<action>
Update `KotlinWrapperGenerator.kt`:
1. Change the filter condition `if (module.isInterface || module.isAbstract || module.isSealed) return` to `if (module.isInterface || module.isSealed) return` (allow `isAbstract`).
2. If `module.isAbstract`, generate a constructor wrapper function `${module.className}_constructor` that simply returns the `this` argument (`argv[0]` equivalent or `this_arg`), or handles `napi_get_cb_info` to get `thisVar` and returns it. (For abstract classes, direct JS instantiation should probably throw or just return `thisVar`).
3. For method wrappers of abstract classes, the first step must be unwrapping `this`:
   - `val thisVar = alloc<napi_valueVar>()`
   - `napi_get_cb_info(env, info, argc.ptr, argv, thisVar.ptr, null)`
   - `val instance = thisVar.value!!.unwrapKotlinObject<${module.className}>(env!!)`
   - Then call `instance.${func.functionName}(...)` instead of `module.className.${func.functionName}(...)`.
</action>
<acceptance_criteria>
- `KotlinWrapperGenerator.kt` no longer skips `isAbstract` modules.
- Generated method wrapper for abstract class unwraps `thisVar.value` to the instance using `unwrapKotlinObject`.
- Generates a constructor wrapper function for abstract classes.
</acceptance_criteria>
</task>

<task>
<read_first>
- harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/InitEntryGenerator.kt
</read_first>
<action>
Update `InitEntryGenerator.kt`:
1. Change filter to allow `isAbstract` modules.
2. For `isAbstract` modules, instead of `napi_create_object` and `napi_define_properties` on the export object:
   - Create property descriptors for each method.
   - Use `napi_define_class` to create the JS class constructor, passing the generated constructor wrapper.
   - Use `napi_create_reference` to store the class constructor in a `napi_ref` with ref count 1.
   - Store the ref in `com.itime.harmony.napi.runtime.utils.ConstructorRegistry.refs["${module.className}"] = refVar.value!!`
   - Set the constructor on the `exports` object using `napi_set_named_property` so it is exported.
</action>
<acceptance_criteria>
- `InitEntryGenerator.kt` processes `isAbstract` modules.
- Generates `napi_define_class` for abstract classes.
- Generates code to store `napi_ref` into `ConstructorRegistry.refs`.
</acceptance_criteria>
</task>

<task>
<read_first>
- harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/mapper/TypeMapper.kt
</read_first>
<action>
Update `TypeMapper.kt`:
1. In `getNapiToKotlinMethod`, if the `typeModel.isAbstract` is true (or if we can determine it's an abstract class from `isSerializable` check), return `unwrapKotlinObject<${getKotlinTypeString(typeModel)}>(env!!)`. Wait, `HarmonyTypeModel` doesn't have `isAbstract`. Add `isAbstract` to `HarmonyTypeModel` or determine it via modules.
2. In `getKotlinToNapiMethod`, if it's an abstract class, return `toNapiWrappedObject(env!!, "${typeModel.simpleName}")`.
</action>
<acceptance_criteria>
- `TypeMapper.kt` handles abstract class conversions to/from NAPI using `unwrapKotlinObject` and `toNapiWrappedObject`.
</acceptance_criteria>
</task>

<task>
<read_first>
- harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/TypeScriptGenerator.kt
</read_first>
<action>
Update `TypeScriptGenerator.kt`:
1. When generating modules, if `module.isAbstract` is true, generate `export declare abstract class ${module.moduleName}` instead of `export interface`.
2. Generate methods inside the class without the `function` keyword (e.g. `doSomething(): void;`).
</action>
<acceptance_criteria>
- `TypeScriptGenerator.kt` generates `export declare abstract class` for abstract modules.
</acceptance_criteria>
</task>
