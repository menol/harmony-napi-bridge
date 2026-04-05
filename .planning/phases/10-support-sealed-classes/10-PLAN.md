---
wave: 1
depends_on: []
files_modified:
  - "harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/models/Models.kt"
  - "harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/HarmonyNapiProcessor.kt"
  - "harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt"
  - "harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/InitEntryGenerator.kt"
  - "harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/TypeScriptGenerator.kt"
  - "sample-plugin/src/commonMain/kotlin/com/itime/harmony/sample/HelloWorldPlugin.kt"
autonomous: true
---

# Phase 10: Support Sealed Classes and Generics

## Goal
Support exporting Kotlin sealed classes (with generics) to TypeScript definitions, fulfilling both API bridge declarations and data model discriminated unions.

## Verification
- Test successful compilation of the `sample-plugin` using `./gradlew build`.
- The generated `ts/Index.d.ts` must contain valid `export type NetworkResult<T> = Success<T> | Error<T>;` for `@Serializable` sealed classes.
- The generated `ts/Index.d.ts` must contain valid `export interface TestSealed<T>` for `@HarmonyModule` sealed classes.
- Subclasses of `@Serializable` sealed classes must include a `type` discriminator property (e.g., `type: "Success";`).

## Must Haves
- Support for extracting `Modifier.SEALED` and `getSealedSubclasses()`.
- Support for type parameters (`<T>`) on both models and modules.
- Skipping of NAPI wrappers for sealed modules.
- Generation of TypeScript discriminated union types for data models.

<threat_model>
- High: Ensure type parameter extraction does not cause unbounded recursion or stack overflow during KSP type resolution.
- High: Ensure discriminator `type` values don't collide. KSP serialization should default to fully qualified names if `@SerialName` is omitted.
</threat_model>

## Tasks

<task>
<read_first>
- harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/models/Models.kt
</read_first>
<action>
Update `Models.kt` to include sealed class metadata and generics support.
- In `HarmonyModuleModel`: add `val isSealed: Boolean = false`
- In `HarmonyTypeModel`: 
  - add `val isSealed: Boolean = false`
  - add `val sealedSubclasses: List<HarmonyTypeModel> = emptyList()`
  - add `val typeParameters: List<String> = emptyList()`
  - add `val serialName: String? = null`
</action>
<acceptance_criteria>
- `Models.kt` contains `val isSealed: Boolean = false` in both `HarmonyModuleModel` and `HarmonyTypeModel`.
- `HarmonyTypeModel` contains `val sealedSubclasses: List<HarmonyTypeModel>`, `val typeParameters: List<String>`, and `val serialName: String?`.
</acceptance_criteria>
</task>

<task>
<read_first>
- harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/HarmonyNapiProcessor.kt
</read_first>
<action>
Update `HarmonyNapiProcessor.kt` to extract sealed modifiers, sealed subclasses, type parameters, and `@SerialName` annotations.
- In `resolveType()`, set `isSealed = (resolved.declaration as? KSClassDeclaration)?.modifiers?.contains(Modifier.SEALED) == true`.
- Extract `serialName` from the `@SerialName` annotation on `resolved.declaration`.
- Extract `typeParameters` from `resolved.declaration.typeParameters`.
- Extract `sealedSubclasses` by calling `getSealedSubclasses()` on `KSClassDeclaration`, creating a basic `HarmonyTypeModel` for each subclass containing its `simpleName`, `qualifiedName`, `isSerializable`, `properties`, `typeParameters`, and `serialName`.
- In `process()`, when creating `HarmonyModuleModel`, set `isSealed = classDecl.modifiers.contains(Modifier.SEALED)`.
</action>
<acceptance_criteria>
- `resolveType` extracts `isSealed`, `serialName`, `typeParameters`, and `sealedSubclasses`.
- `process` sets `isSealed` on `HarmonyModuleModel`.
- `grep "getSealedSubclasses" harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/HarmonyNapiProcessor.kt` returns a match.
</acceptance_criteria>
</task>

<task>
<read_first>
- harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt
- harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/InitEntryGenerator.kt
</read_first>
<action>
Update code generators to skip generating NAPI bindings for sealed classes.
- In `KotlinWrapperGenerator.kt`, change the skip condition to: `if (module.isInterface || module.isAbstract || module.isSealed) return`.
- In `InitEntryGenerator.kt`, change the filter condition to: `val validModules = modules.filter { !it.isInterface && !it.isAbstract && !it.isSealed }`.
</action>
<acceptance_criteria>
- `grep "module.isSealed" harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt` returns a match.
- `grep "!it.isSealed" harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/InitEntryGenerator.kt` returns a match.
</acceptance_criteria>
</task>

<task>
<read_first>
- harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/TypeScriptGenerator.kt
</read_first>
<action>
Update `TypeScriptGenerator.kt` to handle TS declarations for sealed classes and generics.
- Update `module.isInterface || module.isAbstract` condition to `module.isInterface || module.isAbstract || module.isSealed`.
- When filtering `allTypes.filter { it.isSerializable }`, change it to handle sealed classes specifically:
  - Generate `export type {SealedName}<T> = SubClassA<T> | SubClassB<T>;` for `isSealed`.
  - For normal serializable classes (`isSerializable && !isSealed`), generate `export interface {Name}<T> { ... }`.
  - If a normal serializable class is a subclass of a sealed class, inject a `type` property using its `serialName` or `qualifiedName`.
</action>
<acceptance_criteria>
- `grep "export type" harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/TypeScriptGenerator.kt` returns a match.
- `grep "type:" harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/TypeScriptGenerator.kt` returns a match for the discriminator.
</acceptance_criteria>
</task>

<task>
<read_first>
- sample-plugin/src/commonMain/kotlin/com/itime/harmony/sample/HelloWorldPlugin.kt
</read_first>
<action>
Add test cases for generic sealed classes to `HelloWorldPlugin.kt`.
- Add a `@Serializable sealed class NetworkResult<T>` with subclasses `Success<T>` and `Error<T>`, using `@SerialName` annotations.
- Add a `@HarmonyModule(name = "TestSealed") sealed class TestSealed<T>` with an abstract `@HarmonyExport` method `abstract fun process(item: T): T`.
- Add an `@HarmonyExport` function in `HelloWorldPlugin` that accepts and returns `NetworkResult<String>`.
</action>
<acceptance_criteria>
- `grep "sealed class NetworkResult" sample-plugin/src/commonMain/kotlin/com/itime/harmony/sample/HelloWorldPlugin.kt` returns a match.
- `grep "sealed class TestSealed" sample-plugin/src/commonMain/kotlin/com/itime/harmony/sample/HelloWorldPlugin.kt` returns a match.
</acceptance_criteria>
</task>
