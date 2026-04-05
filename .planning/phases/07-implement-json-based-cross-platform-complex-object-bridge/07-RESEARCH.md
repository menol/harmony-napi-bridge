# Phase 7: Implement JSON-based cross-platform complex object bridge - Research

## Context & Goal
Phase 7 requires supporting "zero-overhead data class and enum passing using kotlinx.serialization + NAPI C bindings". 
This means that HarmonyOS developers should be able to pass standard JS Objects and receive standard JS Objects when interacting with Kotlin `@Serializable` Data Classes and Enums, without any manual conversion or C++ boilerplate.

Instead of writing a complex and manual NAPI property-by-property reflection encoder/decoder, the most performant and "zero-overhead" (from a developer perspective) approach is to leverage the highly optimized JS Engine's native `JSON.parse` and `JSON.stringify` via NAPI, coupled with Kotlin's `kotlinx.serialization`.

## Technical Strategy

1. **Serialization Bridge (NAPI + JS Engine)**:
   - **Kotlin to JS**: Kotlin serializes the Data Class to a JSON string using `Json.encodeToString()`. The string is converted to a `napi_value`, and we use NAPI to invoke the global `JSON.parse()` to return a native JS object to ArkTS.
   - **JS to Kotlin**: ArkTS passes a JS object. We use NAPI to invoke the global `JSON.stringify()` to get a JSON string, read it into Kotlin, and deserialize it using `Json.decodeFromString()`.

2. **KSP Processor Enhancements**:
   - The KSP plugin must detect if a type is annotated with `@Serializable` or if it's an `Enum`.
   - The `TypeMapper` must generate the correct inline extension function calls (`toKotlinObject<T>`, `toNapiObject()`, `toKotlinEnum<T>`, `toNapiString()`).
   - The `TypeScriptGenerator` should automatically generate TypeScript `interface` definitions for the detected Data Classes to provide a seamless frontend developer experience.

## Actionable Plan

### 1. Build Configuration
- Update `gradle/libs.versions.toml` to add `kotlinx-serialization-json`.
- Apply `kotlin("plugin.serialization")` to `harmony-napi-runtime/build.gradle.kts` and `sample-plugin/build.gradle.kts`.
- Add the `kotlinx-serialization-json` dependency to both modules.

### 2. Runtime Extensions (`NapiUtils.kt`)
Add the following functions:
- `String.toNapiObject(env)`: Creates a JS string, gets the global `JSON` object, and calls `JSON.parse`.
- `napi_value.toJsonString(env)`: Gets the global `JSON` object, calls `JSON.stringify`, and returns a Kotlin `String`.
- `inline fun <reified T> napi_value.toKotlinObject(env): T`: Combines `toJsonString` + `Json.decodeFromString<T>`.
- `inline fun <reified T> T.toNapiObject(env): napi_value`: Combines `Json.encodeToString` + `String.toNapiObject(env)`.
- `inline fun <reified T : Enum<T>> napi_value.toKotlinEnum(env): T`: Uses `enumValueOf<T>(this.toKotlinString(env))`.
- `inline fun <reified T : Enum<T>> T.toNapiString(env): napi_value`: Uses `this.name.toNapiValue(env)`.

### 3. KSP Models & Processor
- **`Models.kt`**: Update `HarmonyTypeModel` to store `qualifiedName`, `isSerializable`, `isEnum`, and a list of `HarmonyPropertyModel` for TypeScript generation.
- **`HarmonyNapiProcessor.kt`**: Update `resolveType` to detect the `@Serializable` annotation and `ENUM_CLASS` kind. Recursively resolve properties for Data Classes.

### 4. Code Generators
- **`TypeMapper.kt`**: 
  - Update `getNapiToKotlinMethod` to return `toKotlinObject<${qualifiedName}>(env!!)` or `toKotlinEnum<${qualifiedName}>(env!!)`.
  - Update `getKotlinToNapiMethod` to return `toNapiObject(env!!)` or `toNapiString(env!!)`.
  - Update `getTsType` to return the `simpleName` for Data Classes/Enums.
- **`TypeScriptGenerator.kt`**: 
  - Traverse all functions and collect unique `@Serializable` models.
  - Generate corresponding `interface` blocks in `Index.d.ts` alongside the module declarations.

### 5. Verification
- Create a `@Serializable data class User` and `enum class Role` in `sample-plugin/src/commonMain/kotlin/com/itime/harmony/sample/HelloWorldPlugin.kt`.
- Export a function `processUser(user: User, role: Role): User`.
- Verify that `HelloWorldPlugin_NapiWrapper.kt` generates correct inline calls and `Index.d.ts` contains the `User` interface.
- Run `./hvigorw assembleHar` in the `ohos` module to ensure the C++ and Kotlin/Native code compiles successfully without linker or type errors.