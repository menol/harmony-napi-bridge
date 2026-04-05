# Phase 10: support-sealed-classes Research

## Goal
To plan the implementation of Phase 10: "支持密封类的导出" (Support exporting sealed classes), along with the user constraint "还需要支持泛型" (Need to support generics as well).

## 1. Current Context & Background
- The `harmony-napi-ksp-processor` currently supports exporting `interface` and `abstract class` (Phase 9) via `@HarmonyModule`.
- For data models, `@Serializable` data classes and `enum` classes are supported as parameters/return types in `@HarmonyExport` functions.
- The user requires support for **sealed classes**, which in Kotlin act as restricted class hierarchies. These are most commonly used as generic data models (e.g., `sealed class Result<T>`) to represent discriminated states.
- Support for **generics** on sealed classes is an explicit requirement.

## 2. KSP Capabilities & Extraction Strategy
- **Detecting Sealed Classes**: KSP provides `Modifier.SEALED`. We can check `classDecl.modifiers.contains(Modifier.SEALED)`.
- **Extracting Subclasses**: KSP provides `getSealedSubclasses()` on `KSClassDeclaration`, returning a sequence of its subclass declarations.
- **Extracting Discriminators**: For `@Serializable` sealed classes, `kotlinx.serialization` uses a discriminator field (usually `type` by default) with the value being the fully qualified class name or the `@SerialName` value. We need to extract `@SerialName` to properly map this in TypeScript.
- **Handling Generics**: Currently, `HarmonyTypeModel` only stores type `arguments` (usage-side, e.g., `Result<Int>`), but not the class's `typeParameters` (definition-side, e.g., `<T>`). We need to capture `typeParameters` to correctly generate generic TypeScript definitions like `export type Result<T> = ...`.

## 3. Necessary Model Updates (`Models.kt`)
- **`HarmonyTypeModel`**:
  - Add `isSealed: Boolean = false`
  - Add `sealedSubclasses: List<HarmonyTypeModel> = emptyList()`
  - Add `typeParameters: List<String> = emptyList()` (To support generics on the model definition side).
  - Add `serialName: String? = null` (To support accurate TS union discriminators).
- **`HarmonyModuleModel`**:
  - Add `isSealed: Boolean = false` (to align with `isInterface` and `isAbstract`).

## 4. Code Generation Strategy
- **Kotlin Wrapper & Init Entry (`KotlinWrapperGenerator.kt`, `InitEntryGenerator.kt`)**:
  - Sealed classes cannot be instantiated from JS directly. Like `isAbstract` and `isInterface`, any `@HarmonyModule` sealed class should be skipped from generating `_NapiWrapper` bindings and skipped in `InitGeneratedWrappers`.
- **TypeScript Definitions (`TypeScriptGenerator.kt`)**:
  - **For `@Serializable` sealed classes (Data Models)**:
    - Generate a discriminated union type.
      ```typescript
      export type Result<T> = Success<T> | Error<T>;
      ```
    - For each subclass, generate its interface and inject the discriminator property (e.g., `type: "Success"`).
      ```typescript
      export interface Success<T> {
          type: "Success"; // Or fully qualified name if no @SerialName
          data: T;
      }
      ```
  - **For `@HarmonyModule` sealed classes (API Bridges)**:
    - Generate them as interfaces similar to abstract classes, including their generic `typeParameters`.

## 5. Proposed Implementation Plan
1. **Update KSP Models**: Modify `Models.kt` to add `isSealed`, `sealedSubclasses`, `serialName`, and `typeParameters`.
2. **Update Processor**: Modify `HarmonyNapiProcessor.kt` to extract the `SEALED` modifier, resolve sealed subclasses recursively, and capture class `typeParameters` and `@SerialName` annotations.
3. **Update Generators**:
   - Skip `isSealed` in `KotlinWrapperGenerator` and `InitEntryGenerator`.
   - Update `TypeScriptGenerator` to correctly emit `export type ... = ...` for sealed classes and inject discriminator properties into subclass interfaces.
4. **Add Tests**: Add a generic sealed class (e.g., `sealed class NetworkResult<T>`) to `HelloWorldPlugin.kt` and ensure KSP successfully generates the correct TS definitions and Kotlin compilation succeeds.
