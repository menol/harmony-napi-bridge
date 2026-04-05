# Phase 08: Support Interface Export - Research

## 1. Goal
Support applying `@HarmonyModule` to a Kotlin `interface` (including generic interfaces) so that they are exported correctly to the TypeScript/ArkTS side, and do not break the existing Kotlin Native compilation.

Example:
```kotlin
@HarmonyModule(name = "TestInterface")
interface DemoInterface<T> {
    @HarmonyExport
    fun sayHello(name: T): String
}
```

## 2. Current Implementation Analysis
Currently, `@HarmonyModule` assumes the annotated declaration is an `object` (singleton) or a class with static methods. 
- **`KotlinWrapperGenerator`**: Generates static function calls like `ModuleName.functionName(...)`.
- **`InitEntryGenerator`**: Registers these wrapper functions as static methods on a JavaScript object.
- **`TypeScriptGenerator`**: Generates `export declare namespace ModuleName { functionName(...) }`.

## 3. Problems with Interfaces
If the user applies `@HarmonyModule` to an `interface DemoInterface<T>`, the current generation process fails in multiple places:
- **Compile Error (Kotlin)**: `KotlinWrapperGenerator` generates `DemoInterface.sayHello(...)`, which is invalid because `DemoInterface` is an interface and `sayHello` is an instance method.
- **Syntax Error (TypeScript)**: `TypeScriptGenerator` generates `export declare namespace DemoInterface { function sayHello(name: T): string }`. Namespaces in TypeScript cannot have type parameters (`<T>`), and `T` is left unresolved.
- **Type Mapping Error**: `TypeMapper.getTsType` throws an `IllegalArgumentException` or returns `"unknown"` because it doesn't recognize a type parameter like `T`.

## 4. Required Changes for PLAN

### A. KSP Models Update (`Models.kt` & `HarmonyNapiProcessor.kt`)
- Update `HarmonyModuleModel` to track if the module is an interface and capture its type parameters:
  - `val isInterface = classDeclaration.classKind == ClassKind.INTERFACE`
  - `val typeParameters = classDeclaration.typeParameters.map { it.name.asString() }`
- Update `HarmonyTypeModel` to track if a type is a type parameter (e.g., `T`):
  - `val isTypeParameter = resolved.declaration is KSTypeParameter`

### B. Generator Updates
1. **`KotlinWrapperGenerator` & `InitEntryGenerator`**:
   - If `module.isInterface` is true, **skip** generating the C-interop wrapper functions and NAPI module registration. Interfaces don't have static implementations to export directly.
2. **`TypeScriptGenerator`**:
   - If `module.isInterface` is true, generate `export interface ModuleName<T1, T2> { ... }` instead of a namespace.
3. **`TypeMapper`**:
   - Update `getTsType` to return the simple name (e.g., `"T"`) if `typeModel.isTypeParameter` is true.

### C. Future Extension (Callback Support)
If the user's intent is to pass a JS object implementing this interface into a Kotlin function, this requires KSP to generate a proxy class in Kotlin that implements the interface and forwards method calls to a `napi_ref` using `napi_call_function` (related to Phase 2 `TEST-03`). For now, fixing the KSP generation to correctly output the TS type definition and not break the Kotlin build is the primary goal of exporting the interface type.
