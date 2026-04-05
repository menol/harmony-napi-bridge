# Phase 11: Abstract Class NAPI Object Wrapping - Research

## 1. Goal
Support exporting Kotlin `abstract class`es so that they are mapped to JS classes via NAPI Object Wrapping. Instead of passing these objects by value (JSON serialization), they will be passed by reference. This allows Kotlin to return subclass instances to JS, and JS can hold them and invoke their methods, maintaining state on the Kotlin side.

## 2. NAPI Object Wrapping Concepts in Kotlin Native
NAPI provides a way to bind a native object to a JS object using `napi_wrap` and retrieve it using `napi_unwrap`.
In Kotlin Native, memory is managed via ARC. To pass a Kotlin object to C/C++ safely, we must use `StableRef`.

- **Wrapping**: Create a `StableRef` for the Kotlin object. Pass `stableRef.asCPointer()` to `napi_wrap`.
- **Unwrapping**: In JS method callbacks, use `napi_unwrap` to get the `COpaquePointer`, convert it back to `StableRef.fromCPointer(ptr)`, and access `.get()` to call the method.
- **Memory Management**: Provide a finalizer callback to `napi_wrap` that calls `stableRef.dispose()` when the JS object is garbage collected.

## 3. Implementation Plan

### A. KSP Models Update
- Currently, `KotlinWrapperGenerator.kt` skips generating wrappers for `module.isAbstract`. We need to remove this skip condition for `isAbstract`.

### B. Generating the JS Class Constructor
For an exported abstract class (e.g., `MyAbstractClass`), KSP should generate:
1. **A Constructor Wrapper**:
   ```kotlin
   fun MyAbstractClass_constructor(env: napi_env?, info: napi_callback_info?): napi_value? {
       // Extract 'this' and arguments
       // If instantiated from Kotlin (e.g. passing napi_external), wrap the StableRef.
       // If instantiated from JS directly, throw error (since it's abstract).
   }
   ```
2. **Method Wrappers**:
   For each `@HarmonyExport` method, generate a wrapper that unwraps `this`:
   ```kotlin
   fun MyAbstractClass_doSomething_wrapper(env: napi_env?, info: napi_callback_info?): napi_value? {
       // napi_unwrap(env, this_arg, &native_obj)
       // val obj = StableRef.fromCPointer(native_obj).get() as MyAbstractClass
       // obj.doSomething()
   }
   ```

### C. Module Initialization (`InitEntryGenerator`)
- Use `napi_define_class` to define the JS class with its methods.
- Store the resulting constructor `napi_value` (using `napi_create_reference` to make it a global `napi_ref`) so it can be accessed later when we need to return an instance from Kotlin to JS.

### D. Object Conversion Bridge (`TypeMapper` and Runtime Utils)
- Provide a runtime utility to wrap an existing Kotlin object:
  ```kotlin
  fun wrapKotlinObject(env: napi_env, obj: Any, constructorRef: napi_ref): napi_value
  ```
- Update `TypeMapper.kt` to use the reference bridge for abstract classes instead of the JSON value bridge (`toKotlinObject`/`toNapiObject`).
  - When returning an abstract class instance from a Kotlin method: fetch the `constructorRef`, create a JS instance via `napi_new_instance`, wrap the `StableRef`, and return the JS object.
  - When accepting an abstract class instance as a parameter: unwrap the JS object to get the Kotlin `StableRef`.

## 4. Challenges & Considerations
- **Global Constructor Reference**: We need a way to store the `napi_ref` of the defined class so that `wrapKotlinObject` can access it globally. We might need a global map or a static registry in the generated code to store class constructors.
- **Finalizer Execution**: The finalizer is called by the JS garbage collector. We must ensure `stableRef.dispose()` is called correctly to avoid memory leaks.
- **TypeScript Definitions**: `TypeScriptGenerator` needs to generate `export declare abstract class MyAbstractClass { ... }` with the exported methods, instead of a namespace or interface.