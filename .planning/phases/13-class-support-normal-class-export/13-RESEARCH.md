# Phase 13 Research: Support Normal Class Export

## What is a "Normal Class" in this context?
A Kotlin class annotated with `@HarmonyModule` that is NOT an `object` (`ClassKind.OBJECT`), NOT `abstract`, NOT an `interface`, and NOT `sealed`. It has a primary constructor and instance methods, and carries state.

## Current Behavior vs Target Behavior
- **Current Behavior**: The KSP processor does not differentiate between an `object` and a normal `class`. It treats normal classes as singletons/namespaces. It generates Kotlin wrappers that call methods statically (`val result = MyClass.method()`), which causes compilation errors for normal classes. TypeScript generation outputs an `export declare namespace` with functions, instead of an `export declare class` with instance methods.
- **Target Behavior**: KSP should recognize normal classes (`ClassKind.CLASS`) and generate NAPI bindings that allow JS to instantiate them (`new MyClass(...)`), call their instance methods, and allow Kotlin to return existing instances to JS.

## Key Architectural Changes Required

### 1. Update Domain Models (`HarmonyModuleModel.kt` & `HarmonyNapiProcessor.kt`)
- **Distinguish Singletons**: Introduce `isObject: Boolean` (derived from `classDecl.classKind == ClassKind.OBJECT`) to correctly differentiate singletons from instantiable classes.
- **Constructor Parameters**: Capture `primaryConstructor` parameters in `HarmonyModuleModel` (as a `List<HarmonyParameterModel>`) so we can generate JS-to-Kotlin argument conversions in the constructor wrapper.

### 2. Refactor Runtime Utilities (`NapiUtils.kt`)
Currently, `toNapiWrappedObject` calls `napi_new_instance` (which triggers the JS constructor callback) and then manually calls `napi_wrap`. If we support JS instantiating normal classes, the constructor callback itself must call `napi_wrap`. To avoid "double-instantiation" and "double-wrapping":
- Update `toNapiWrappedObject` to pass the existing Kotlin `StableRef` pointer as a **`napi_external` argument** to `napi_new_instance`.
- Remove the manual `napi_wrap` from `toNapiWrappedObject`, delegating the wrapping responsibility entirely to the NAPI constructor callbacks.

### 3. Refactor Wrapper Generation (`KotlinWrapperGenerator.kt`)
- **Instance Methods**: Use `unwrapKotlinObject` to extract the `instance` from `thisVar` for normal classes (exactly as we do for `isAbstract`), instead of making static calls.
- **Constructor Wrapper Logic**:
  The generated NAPI constructor callback (`MyClass_constructor`) must handle two scenarios by checking `argc` and the argument type:
  1. **Called from JS (`new MyClass(args)`)**: Unpack the JS arguments, invoke the Kotlin primary constructor, wrap the resulting Kotlin instance into `thisVar` via `napi_wrap`, and return it.
  2. **Called from Kotlin (`toNapiWrappedObject`)**: Intercept the `napi_external` argument, wrap the existing pointer into `thisVar` via `napi_wrap`, and return it without instantiating a new Kotlin object.
- *Note*: Abstract class constructor wrappers must also be updated to support the `napi_external` wrapping mechanism.

### 4. Update Module Initialization (`InitEntryGenerator.kt`)
- Use `napi_define_class` for normal classes (same as abstract classes) and register their constructor references in `ConstructorRegistry`.
- Restrict `napi_create_object` + `napi_define_properties` exclusively to true `object` (singleton) declarations.

### 5. Update TypeScript Generation (`TypeScriptGenerator.kt`)
- Generate `export declare class MyClass` for normal classes.
- Add a `constructor(args...)` signature inside the `class` block matching the Kotlin primary constructor.
- Restrict `export declare namespace MyModule` to `object` singletons.

## Potential Pitfalls & Threat Model
- **Memory Leaks**: We must ensure `StableRef.dispose()` is correctly bound to the `finalize` callback in the newly generated constructor wrapping logic.
- **Wrapper Conflicts**: Failing to implement the `napi_external` check in the constructor wrapper will cause double instantiation (leaking Kotlin objects) and crashes when `napi_wrap` is called twice on the same JS object.
- **Constructor Overloading**: KSP does not support multiple constructors out-of-the-box in this context. The plan relies solely on the primary constructor.

## Plan Validation
- Create a normal class `TestClass` in `HelloWorldPlugin.kt` with state (e.g., `private var count: Int`) and a primary constructor.
- Verify from ArkTS side that `const tc = new TestClass(10)` works, methods mutate the state, and Kotlin can return existing instances of `TestClass` to JS successfully.