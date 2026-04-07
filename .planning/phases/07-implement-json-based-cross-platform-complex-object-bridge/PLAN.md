---
wave: 1
depends_on: []
files_modified:
  - gradle/libs.versions.toml
  - harmony-napi-runtime/build.gradle.kts
  - sample-plugin/build.gradle.kts
  - harmony-napi-runtime/src/linuxArm64Main/kotlin/com.realtech/harmony/napi/runtime/utils/NapiUtils.kt
  - harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/models/Models.kt
  - harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/HarmonyNapiProcessor.kt
  - harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/mapper/TypeMapper.kt
  - harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/TypeScriptGenerator.kt
  - harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt
  - sample-plugin/src/commonMain/kotlin/com.realtech/harmony/sample/HelloWorldPlugin.kt
  - ohos/entry/src/main/ets/test/KhnTest.ets
autonomous: true
requirements:
  - TEST-02
---

# Phase 07: Implement JSON-based cross-platform complex object bridge

## Goal
Support zero-overhead data class and enum passing using `kotlinx.serialization` + NAPI C bindings by utilizing JS engine's native `JSON.parse` and `JSON.stringify`.

## Must Haves
- `kotlinx-serialization-json` must be added to runtime and plugin build configurations.
- `NapiUtils` must provide inline extensions to wrap `JSON.parse` and `JSON.stringify` for NAPI values (`toKotlinObject`, `toNapiObject`, `toKotlinEnum`, `toNapiString`).
- KSP Models must capture `isSerializable`, `isEnum`, `properties`, and `enumValues`.
- TypeMapper and Code Generators must correctly emit JSON serialization wrapper calls and matching TypeScript interface/enum definitions.
- End-to-end data class and enum passing must be tested and verified in ArkTS.

## Tasks

<tasks>
  <task id="deps-config" wave="1">
    <description>Add kotlinx-serialization to build configuration</description>
    <read_first>
      <file>gradle/libs.versions.toml</file>
      <file>harmony-napi-runtime/build.gradle.kts</file>
      <file>sample-plugin/build.gradle.kts</file>
    </read_first>
    <action>
      1. In `gradle/libs.versions.toml`, under `[libraries]`, add: `kotlinx-serialization-json = { module = "org.jetbrains.kotlinx:kotlinx-serialization-json", version.ref = "kotlinx-serialization" }`
      2. In `harmony-napi-runtime/build.gradle.kts`, add `kotlin("plugin.serialization")` to the `plugins` block.
      3. In `harmony-napi-runtime/build.gradle.kts`, add `implementation(libs.kotlinx.serialization.json)` to the `commonMain` `dependencies` block.
      4. In `sample-plugin/build.gradle.kts`, add `kotlin("plugin.serialization")` to the `plugins` block.
      5. In `sample-plugin/build.gradle.kts`, add `implementation(libs.kotlinx.serialization.json)` to the `commonMain` `dependencies` block.
    </action>
    <acceptance_criteria>
      - `grep -q "kotlinx-serialization-json =" gradle/libs.versions.toml` returns 0
      - `grep -q "plugin.serialization" harmony-napi-runtime/build.gradle.kts` returns 0
      - `grep -q "libs.kotlinx.serialization.json" harmony-napi-runtime/build.gradle.kts` returns 0
      - `grep -q "plugin.serialization" sample-plugin/build.gradle.kts` returns 0
      - `grep -q "libs.kotlinx.serialization.json" sample-plugin/build.gradle.kts` returns 0
    </acceptance_criteria>
  </task>

  <task id="napi-utils-json" wave="2" depends_on="deps-config">
    <description>Add JSON serialization bridge extensions to NapiUtils</description>
    <read_first>
      <file>harmony-napi-runtime/src/linuxArm64Main/kotlin/com.realtech/harmony/napi/runtime/utils/NapiUtils.kt</file>
    </read_first>
    <action>
      In `harmony-napi-runtime/src/linuxArm64Main/kotlin/com.realtech/harmony/napi/runtime/utils/NapiUtils.kt`:
      1. Add imports: `import kotlinx.serialization.encodeToString`, `import kotlinx.serialization.decodeFromString`, `import kotlinx.serialization.json.Json`
      2. Add `fun String.toNapiObject(env: napi_env): napi_value`: Uses NAPI to get the global object, retrieves the `JSON` property, and calls `JSON.parse` with this string converted to `napi_value`.
      3. Add `fun napi_value.toJsonString(env: napi_env): String`: Uses NAPI to get the global object, retrieves the `JSON` property, and calls `JSON.stringify` on this `napi_value`, returning a Kotlin String.
      4. Add `inline fun &lt;reified T&gt; napi_value.toKotlinObject(env: napi_env): T = Json.decodeFromString&lt;T&gt;(this.toJsonString(env))`
      5. Add `inline fun &lt;reified T&gt; T.toNapiObject(env: napi_env): napi_value = Json.encodeToString(this).toNapiObject(env)`
      6. Add `inline fun &lt;reified T : Enum&lt;T&gt;&gt; napi_value.toKotlinEnum(env: napi_env): T = enumValueOf&lt;T&gt;(this.toKotlinString(env))`
      7. Add `inline fun &lt;reified T : Enum&lt;T&gt;&gt; T.toNapiString(env: napi_env): napi_value = this.name.toNapiValue(env)`
    </action>
    <acceptance_criteria>
      - `grep -q "fun String.toNapiObject" harmony-napi-runtime/src/linuxArm64Main/kotlin/com.realtech/harmony/napi/runtime/utils/NapiUtils.kt` returns 0
      - `grep -q "fun napi_value.toJsonString" harmony-napi-runtime/src/linuxArm64Main/kotlin/com.realtech/harmony/napi/runtime/utils/NapiUtils.kt` returns 0
      - `grep -q "fun <reified T> napi_value.toKotlinObject" harmony-napi-runtime/src/linuxArm64Main/kotlin/com.realtech/harmony/napi/runtime/utils/NapiUtils.kt` returns 0
      - `grep -q "fun <reified T> T.toNapiObject" harmony-napi-runtime/src/linuxArm64Main/kotlin/com.realtech/harmony/napi/runtime/utils/NapiUtils.kt` returns 0
      - `grep -q "fun <reified T : Enum<T>> napi_value.toKotlinEnum" harmony-napi-runtime/src/linuxArm64Main/kotlin/com.realtech/harmony/napi/runtime/utils/NapiUtils.kt` returns 0
      - `grep -q "fun <reified T : Enum<T>> T.toNapiString" harmony-napi-runtime/src/linuxArm64Main/kotlin/com.realtech/harmony/napi/runtime/utils/NapiUtils.kt` returns 0
    </acceptance_criteria>
  </task>

  <task id="ksp-models" wave="3" depends_on="napi-utils-json">
    <description>Update KSP Models to support data classes and enums</description>
    <read_first>
      <file>harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/models/Models.kt</file>
    </read_first>
    <action>
      In `Models.kt`:
      1. Create `data class HarmonyPropertyModel(val name: String, val type: HarmonyTypeModel)`
      2. Update `HarmonyTypeModel` constructor to add:
         - `val qualifiedName: String = ""`
         - `val isSerializable: Boolean = false`
         - `val isEnum: Boolean = false`
         - `val properties: List&lt;HarmonyPropertyModel&gt; = emptyList()`
         - `val enumValues: List&lt;String&gt; = emptyList()`
    </action>
    <acceptance_criteria>
      - `grep -q "data class HarmonyPropertyModel" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/models/Models.kt` returns 0
      - `grep -q "val isSerializable: Boolean" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/models/Models.kt` returns 0
      - `grep -q "val isEnum: Boolean" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/models/Models.kt` returns 0
      - `grep -q "val enumValues: List<String>" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/models/Models.kt` returns 0
    </acceptance_criteria>
  </task>

  <task id="ksp-processor" wave="4" depends_on="ksp-models">
    <description>Update KSP Processor to detect Serializable and Enums</description>
    <read_first>
      <file>harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/HarmonyNapiProcessor.kt</file>
    </read_first>
    <action>
      In `HarmonyNapiProcessor.kt` inside `resolveType` function:
      1. Extract `qualifiedName = typeRef.resolve().declaration.qualifiedName?.asString() ?: ""`
      2. Check `isSerializable = typeRef.resolve().declaration.annotations.any { it.shortName.asString() == "Serializable" }`
      3. Check `isEnum = (typeRef.resolve().declaration as? KSClassDeclaration)?.classKind == ClassKind.ENUM_CLASS`
      4. If `isSerializable`, extract properties via `(typeRef.resolve().declaration as? KSClassDeclaration)?.getDeclaredProperties()?.map { HarmonyPropertyModel(it.simpleName.asString(), resolveType(it.type)) }?.toList() ?: emptyList()`
      5. If `isEnum`, extract `enumValues` via `(typeRef.resolve().declaration as? KSClassDeclaration)?.declarations?.filterIsInstance&lt;KSClassDeclaration&gt;()?.filter { it.classKind == ClassKind.ENUM_ENTRY }?.map { it.simpleName.asString() }?.toList() ?: emptyList()`
      6. Pass these 5 new arguments to the `HarmonyTypeModel` constructor call.
      7. Add `import com.google.devtools.ksp.symbol.ClassKind` and `import com.realtech.harmony.napi.ksp.models.HarmonyPropertyModel` to the file imports.
    </action>
    <acceptance_criteria>
      - `grep -q "isSerializable =" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/HarmonyNapiProcessor.kt` returns 0
      - `grep -q "isEnum =" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/HarmonyNapiProcessor.kt` returns 0
      - `grep -q "enumValues =" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/HarmonyNapiProcessor.kt` returns 0
    </acceptance_criteria>
  </task>

  <task id="type-mapper" wave="5" depends_on="ksp-processor">
    <description>Update TypeMapper to route Data Classes and Enums correctly</description>
    <read_first>
      <file>harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/mapper/TypeMapper.kt</file>
    </read_first>
    <action>
      In `TypeMapper.kt`:
      1. In `getNapiToKotlinMethod`, add logic before the `when` block: `if (typeModel.isSerializable) return "toKotlinObject&lt;${typeModel.qualifiedName}&gt;(env!!)"; if (typeModel.isEnum) return "toKotlinEnum&lt;${typeModel.qualifiedName}&gt;(env!!)"`
      2. In `getKotlinToNapiMethod`, add logic before the `when` block: `if (typeModel.isSerializable) return "toNapiObject(env!!)"; if (typeModel.isEnum) return "toNapiString(env!!)"`
      3. In `getTsType`, add logic before the `when` block: `if (typeModel.isSerializable || typeModel.isEnum) return typeModel.simpleName`
    </action>
    <acceptance_criteria>
      - `grep -q "typeModel.isSerializable" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/mapper/TypeMapper.kt` returns 0
      - `grep -q "typeModel.isEnum" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/mapper/TypeMapper.kt` returns 0
      - `grep -q "toKotlinObject" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/mapper/TypeMapper.kt` returns 0
      - `grep -q "toKotlinEnum" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/mapper/TypeMapper.kt` returns 0
    </acceptance_criteria>
  </task>

  <task id="typescript-generator" wave="5" depends_on="ksp-processor">
    <description>Update TypeScriptGenerator to generate interface and enum blocks</description>
    <read_first>
      <file>harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/TypeScriptGenerator.kt</file>
    </read_first>
    <action>
      In `TypeScriptGenerator.kt` inside `generate(modules: List&lt;HarmonyModuleModel&gt;)` function:
      1. Create a recursive function to collect all unique `HarmonyTypeModel`s from module export parameters, return types, and their nested properties into a `Set`.
      2. Filter collected types for `isEnum == true`, and for each, append to `tsContent`: `export enum ${type.simpleName} { ... }` containing `name = "name",` for each string in `enumValues`.
      3. Filter collected types for `isSerializable == true`, and for each, append to `tsContent`: `export interface ${type.simpleName} { ... }` containing `${prop.name}: ${TypeMapper.getTsType(prop.type)};` for each property.
      4. Place these block generations *before* the `export declare namespace` generation.
    </action>
    <acceptance_criteria>
      - `grep -q "export enum" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/TypeScriptGenerator.kt` returns 0
      - `grep -q "export interface" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/TypeScriptGenerator.kt` returns 0
      - `grep -q "isSerializable" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/TypeScriptGenerator.kt` returns 0
      - `grep -q "isEnum" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/TypeScriptGenerator.kt` returns 0
    </acceptance_criteria>
  </task>

  <task id="kotlin-wrapper" wave="5" depends_on="ksp-processor">
    <description>Update KotlinWrapperGenerator imports</description>
    <read_first>
      <file>harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt</file>
    </read_first>
    <action>
      In `KotlinWrapperGenerator.kt`, update the `.addImport` call for `"com.realtech.harmony.napi.runtime.utils"` to explicitly include `"toKotlinObject"`, `"toNapiObject"`, `"toKotlinEnum"`, `"toNapiString"`.
      Ensure the strings `"toKotlinObject"`, `"toNapiObject"`, `"toKotlinEnum"`, `"toNapiString"` are present in the list of strings passed to `addImport`.
    </action>
    <acceptance_criteria>
      - `grep -q '"toKotlinObject"' harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt` returns 0
      - `grep -q '"toNapiObject"' harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt` returns 0
      - `grep -q '"toKotlinEnum"' harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt` returns 0
      - `grep -q '"toNapiString"' harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt` returns 0
    </acceptance_criteria>
  </task>

  <task id="tests" wave="6" depends_on="type-mapper, typescript-generator, kotlin-wrapper">
    <description>Add User/Role test to HelloWorldPlugin and KhnTest</description>
    <read_first>
      <file>sample-plugin/src/commonMain/kotlin/com.realtech/harmony/sample/HelloWorldPlugin.kt</file>
      <file>ohos/entry/src/main/ets/test/KhnTest.ets</file>
    </read_first>
    <action>
      1. In `HelloWorldPlugin.kt`:
         - Add `import kotlinx.serialization.Serializable`
         - Add `@Serializable data class User(val name: String, val age: Int)`
         - Add `enum class Role { ADMIN, USER }`
         - Add `@HarmonyExport fun processUser(user: User, role: Role): User { return user.copy(name = user.name + "-" + role.name) }` inside `HelloWorldPlugin` object.
      2. In `KhnTest.ets`:
         - Add a test block `it('should process data classes and enums', 0, () =&gt; { ... })`
         - Call `const result = hello_world_plugin.processUser({ name: "Test", age: 20 }, "ADMIN");`
         - Assert the result: `expect(result.name).assertEqual("Test-ADMIN"); expect(result.age).assertEqual(20);`
    </action>
    <acceptance_criteria>
      - `grep -q "data class User" sample-plugin/src/commonMain/kotlin/com.realtech/harmony/sample/HelloWorldPlugin.kt` returns 0
      - `grep -q "enum class Role" sample-plugin/src/commonMain/kotlin/com.realtech/harmony/sample/HelloWorldPlugin.kt` returns 0
      - `grep -q "fun processUser" sample-plugin/src/commonMain/kotlin/com.realtech/harmony/sample/HelloWorldPlugin.kt` returns 0
      - `grep -q "should process data classes and enums" ohos/entry/src/main/ets/test/KhnTest.ets` returns 0
      - `grep -q "hello_world_plugin.processUser" ohos/entry/src/main/ets/test/KhnTest.ets` returns 0
    </acceptance_criteria>
  </task>
</tasks>

## Verification
- Run `./hvigorw assembleHar` in the `ohos` directory to verify the TS generation and C++ code compilation.
- Run tests in Hypium using `hdc` or device runner to ensure that `hello_world_plugin.processUser` succeeds and returns the correctly mapped object.
