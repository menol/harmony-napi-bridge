# Phase 6: Support any type conversion for ArkTS map and list - Research

## Context
Phase 6 aims to support dynamic `Any` type conversion for ArkTS `Map` and `List`. Currently, `TypeMapper.kt` in `harmony-napi-ksp-processor` maps `List<Any>` to `toKotlinAnyList(env!!)` and `Map<String, Any>` to `toKotlinStringAnyMap(env!!)` (or `toKotlinAnyAnyMap` if the key is also `Any`).
However, the corresponding runtime utilities in `NapiUtils.kt` only support `List<String>` and `Map<String, String>`.

## Missing Runtime Conversions in `NapiUtils.kt`

To implement this phase well, we need to add several missing functions in `NapiUtils.kt`:

1. **`Any` to `napi_value`**
   - Need an `Any?.toNapiValue(env: napi_env): napi_value` extension.
   - It should inspect the dynamic type of the Kotlin object using `when (this)`:
     - `null` -> `napi_get_null()`
     - `is String` -> `this.toNapiValue(env)`
     - `is Double` -> `this.toNapiValue(env)`
     - `is Int` -> `this.toNapiValue(env)` (needs `Int` support)
     - `is Boolean` -> `this.toNapiValue(env)` (needs `Boolean` support)
     - `is List<*>` -> `this.toNapiValue(env)`
     - `is Map<*, *>` -> `this.toNapiValue(env)`

2. **`napi_value` to `Any`**
   - Need a `napi_value.toKotlinAny(env: napi_env): Any?` extension.
   - It should query the JS type using `napi_typeof(env, this, typeVar.ptr)` and switch on `typeVar.value`:
     - `napi_valuetype.napi_undefined`, `napi_valuetype.napi_null` -> `null`
     - `napi_valuetype.napi_boolean` -> `toKotlinBoolean(env)`
     - `napi_valuetype.napi_number` -> `toKotlinDouble(env)` (JS numbers are double-precision)
     - `napi_valuetype.napi_string` -> `toKotlinString(env)`
     - `napi_valuetype.napi_object` -> Check `napi_is_array`. If true, call `toKotlinAnyList(env)`, else call `toKotlinStringAnyMap(env)`.

3. **Collections with `Any` types**
   - `List<Any?>.toNapiValue(env)`
   - `napi_value.toKotlinAnyList(env): List<Any?>`
   - `Map<String, Any?>.toNapiValue(env)`
   - `napi_value.toKotlinStringAnyMap(env): Map<String, Any?>`

4. **Primitive gaps**
   - `TypeMapper.kt` generates `toKotlinBoolean(env!!)` but it's not implemented in `NapiUtils.kt`. We need to implement `Boolean.toNapiValue` and `napi_value.toKotlinBoolean`.
   - `TypeMapper.kt` maps Kotlin `Int` to `toKotlinDouble(env!!)`, which causes a Kotlin type mismatch (assigning `Double` to `Int`). We need to implement `Int.toNapiValue` and `napi_value.toKotlinInt` and fix `TypeMapper.kt`.

## KSP Processor Updates

1. **`TypeMapper.kt`**
   - Must add explicit `"Any" -> "toKotlinAny(env!!)"` and `"Any" -> "toNapiValue(env!!)"` to support `Any` as a top-level parameter or return type.
   - Map `"Int" -> "toKotlinInt(env!!)"` to fix the type assignment issue.
   - Verify `List` maps to `toKotlinAnyList(env!!)` and `Map` maps to `toKotlinStringAnyMap(env!!)` correctly when `Any` types are used.

2. **`KotlinWrapperGenerator.kt`**
   - Currently, it has hardcoded imports for `toKotlinStringList`, `toKotlinStringStringMap`, etc.
   - We need to add the newly created methods (`toKotlinAnyList`, `toKotlinStringAnyMap`, `toKotlinBoolean`, `toKotlinInt`, `toKotlinAny`) to the `addImport` block so the generated Kotlin N-API wrapper compiles successfully.

## Verification

After making these changes, we can verify by compiling the NAPI module (`./hvigorw assembleHar` in `ohos` module) and checking if `List<Any>` and `Map<String, Any>` successfully cross the ArkTS/Kotlin boundary in our NAPI bridge.