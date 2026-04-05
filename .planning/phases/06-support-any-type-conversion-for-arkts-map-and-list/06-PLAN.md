---
wave: 1
depends_on: []
files_modified:
  - harmony-napi-runtime/src/linuxArm64Main/kotlin/com/itime/harmony/napi/runtime/utils/NapiUtils.kt
  - harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/mapper/TypeMapper.kt
  - harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt
autonomous: true
---

# Phase 06: Support Any type conversion for ArkTS map and list

## Goal
Support dynamic `Any` type conversion for ArkTS `Map` and `List` by adding missing methods to `NapiUtils.kt` and updating the KSP processor mappings.

## Must Haves
- `NapiUtils.kt` must support `Any`, `Boolean`, `Int`, `List<Any?>`, and `Map<String, Any?>` to `napi_value` and vice versa.
- `TypeMapper.kt` must map `Any` to `toKotlinAny(env!!)` and `toNapiValue(env!!)`.
- `TypeMapper.kt` must correctly map `Int` to `toKotlinInt(env!!)`.
- `KotlinWrapperGenerator.kt` must import all required `toKotlin*` methods.

## Tasks

<tasks>
  <task id="napi-utils-extensions" wave="1">
    <description>Add missing NAPI type conversion methods to NapiUtils.kt</description>
    <read_first>
      <file>harmony-napi-runtime/src/linuxArm64Main/kotlin/com/itime/harmony/napi/runtime/utils/NapiUtils.kt</file>
    </read_first>
    <action>
      Implement the following extension functions in NapiUtils.kt:
      1. `fun Boolean.toNapiValue(env: napi_env): napi_value` using `napi_get_boolean`
      2. `fun napi_value.toKotlinBoolean(env: napi_env): Boolean` using `napi_get_value_bool`
      3. `fun Int.toNapiValue(env: napi_env): napi_value` using `napi_create_int32`
      4. `fun napi_value.toKotlinInt(env: napi_env): Int` using `napi_get_value_int32`
      5. `fun Any?.toNapiValue(env: napi_env): napi_value` that uses `when (this)` to route to specific `toNapiValue` functions (handles null, String, Double, Int, Boolean, List, Map)
      6. `fun napi_value.toKotlinAny(env: napi_env): Any?` that uses `napi_typeof(env, this, typeVar.ptr)` to route to `toKotlinBoolean`, `toKotlinDouble`, `toKotlinString`, or checks `napi_is_array` to call `toKotlinAnyList` or `toKotlinStringAnyMap`.
      7. `fun List<Any?>.toNapiValue(env: napi_env): napi_value`
      8. `fun napi_value.toKotlinAnyList(env: napi_env): List<Any?>`
      9. `fun Map<String, Any?>.toNapiValue(env: napi_env): napi_value`
      10. `fun napi_value.toKotlinStringAnyMap(env: napi_env): Map<String, Any?>`
    </action>
    <acceptance_criteria>
      - `grep -q "fun Boolean.toNapiValue" harmony-napi-runtime/src/linuxArm64Main/kotlin/com/itime/harmony/napi/runtime/utils/NapiUtils.kt` returns 0
      - `grep -q "fun napi_value.toKotlinBoolean" harmony-napi-runtime/src/linuxArm64Main/kotlin/com/itime/harmony/napi/runtime/utils/NapiUtils.kt` returns 0
      - `grep -q "fun Int.toNapiValue" harmony-napi-runtime/src/linuxArm64Main/kotlin/com/itime/harmony/napi/runtime/utils/NapiUtils.kt` returns 0
      - `grep -q "fun napi_value.toKotlinInt" harmony-napi-runtime/src/linuxArm64Main/kotlin/com/itime/harmony/napi/runtime/utils/NapiUtils.kt` returns 0
      - `grep -q "fun Any?.toNapiValue" harmony-napi-runtime/src/linuxArm64Main/kotlin/com/itime/harmony/napi/runtime/utils/NapiUtils.kt` returns 0
      - `grep -q "fun napi_value.toKotlinAny" harmony-napi-runtime/src/linuxArm64Main/kotlin/com/itime/harmony/napi/runtime/utils/NapiUtils.kt` returns 0
      - `grep -q "fun List<Any?>.toNapiValue" harmony-napi-runtime/src/linuxArm64Main/kotlin/com/itime/harmony/napi/runtime/utils/NapiUtils.kt` returns 0
      - `grep -q "fun napi_value.toKotlinAnyList" harmony-napi-runtime/src/linuxArm64Main/kotlin/com/itime/harmony/napi/runtime/utils/NapiUtils.kt` returns 0
      - `grep -q "fun Map<String, Any?>.toNapiValue" harmony-napi-runtime/src/linuxArm64Main/kotlin/com/itime/harmony/napi/runtime/utils/NapiUtils.kt` returns 0
      - `grep -q "fun napi_value.toKotlinStringAnyMap" harmony-napi-runtime/src/linuxArm64Main/kotlin/com/itime/harmony/napi/runtime/utils/NapiUtils.kt` returns 0
    </acceptance_criteria>
  </task>

  <task id="update-type-mapper" wave="2" depends_on="napi-utils-extensions">
    <description>Update TypeMapper.kt to handle Any and Int types correctly</description>
    <read_first>
      <file>harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/mapper/TypeMapper.kt</file>
    </read_first>
    <action>
      Update `getNapiToKotlinMethod` in `TypeMapper.kt` to:
      - Replace `"Double", "Int" -> "toKotlinDouble(env!!)"` with `"Double" -> "toKotlinDouble(env!!)"` and `"Int" -> "toKotlinInt(env!!)"`
      - Add `"Any" -> "toKotlinAny(env!!)"`
      
      Update `getKotlinToNapiMethod` in `TypeMapper.kt` to:
      - Add `"Any"` to the first case so it becomes `"Double", "Int", "String", "Boolean", "Any" -> "toNapiValue(env!!)"`
    </action>
    <acceptance_criteria>
      - `grep -q '"Int" -> "toKotlinInt(env!!)"' harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/mapper/TypeMapper.kt` returns 0
      - `grep -q '"Any" -> "toKotlinAny(env!!)"' harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/mapper/TypeMapper.kt` returns 0
      - `grep -q '"Any" -> "toNapiValue(env!!)"' harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/mapper/TypeMapper.kt` returns 0
    </acceptance_criteria>
  </task>

  <task id="update-wrapper-generator" wave="2" depends_on="napi-utils-extensions">
    <description>Update KotlinWrapperGenerator.kt imports</description>
    <read_first>
      <file>harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt</file>
    </read_first>
    <action>
      In `KotlinWrapperGenerator.kt`, update the `.addImport` call for `com.itime.harmony.napi.runtime.utils` to include:
      `"toKotlinInt", "toKotlinBoolean", "toKotlinAny", "toKotlinAnyList", "toKotlinStringAnyMap"`
      
      The full line should look like:
      `.addImport("com.itime.harmony.napi.runtime.utils", "toNapiValue", "toKotlinDouble", "toKotlinInt", "toKotlinBoolean", "toKotlinString", "toKotlinStringList", "toKotlinStringStringMap", "toKotlinAny", "toKotlinAnyList", "toKotlinStringAnyMap")`
    </action>
    <acceptance_criteria>
      - `grep -q 'toKotlinInt' harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt` returns 0
      - `grep -q 'toKotlinAny' harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt` returns 0
      - `grep -q 'toKotlinAnyList' harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt` returns 0
      - `grep -q 'toKotlinStringAnyMap' harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt` returns 0
    </acceptance_criteria>
  </task>
</tasks>
