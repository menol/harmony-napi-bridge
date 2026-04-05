# Codebase Concerns & Technical Debt

This document outlines technical debt, known issues, and areas of concern in the `harmony-napi-bridge` project, categorized by severity.

## 1. Technical Debt & Architecture Issues

- **Manual N-API Demo Desync**: The sample plugin `NAPIInit.kt` file manually registers the module and functions (`Add`, `InitOpenMiniEffect`) rather than using the KSP-generated `InitGeneratedWrappers`. The generated code is entirely disconnected from the actual runtime execution, defeating the purpose of the generator.
- **KSP Constraint Enforcement**: `HarmonyNapiProcessor` assumes that `@HarmonyModule` is only applied to Kotlin `object` declarations (singletons). It generates calls like `ClassName.methodName()`. If a user annotates a regular `class`, the generated code will fail to compile. The processor lacks validation to emit KSP errors for invalid targets.
- **Incomplete Type Mapping**: `TypeMapper.kt` only partially supports basic types. `Boolean` and `Unit` lack corresponding `toNapiValue` and `toKotlinBoolean` implementations in `NapiUtils.kt`, meaning functions returning `Unit` or using `Boolean` will result in uncompilable generated code. Furthermore, mapping `Int` to `toKotlinDouble` produces a `Double`, which causes a compile-time type mismatch if the Kotlin function strictly expects an `Int`.

## 2. Security & Memory Safety (Fragile Areas)

- **Use-After-Free in N-API Initialization**: In `NAPIInit.kt`, `demoModule` is created using a `memScoped` block, but it returns a `readValue()` copy of the struct while its properties (e.g., `nm_modname = "khn".cstr.getPointer(this)`) point to memory allocated within that exact `memScoped` block. When the block exits, the strings are freed, causing a critical dangling pointer / use-after-free vulnerability when `napi_module_register` is called.
- **Uninitialized Array Dereference (Out-of-Bounds)**: In `KotlinWrapperGenerator.kt`, the generated wrapper blindly accesses `argv[index]!!` without verifying the actual argument count (`argc.value`) returned by `napi_get_cb_info`. If a JavaScript caller provides fewer arguments than expected, `argv` elements remain uninitialized, leading to a null pointer dereference or undefined behavior (memory corruption).
- **Ignored N-API Status Codes**: Throughout `NapiUtils.kt` and generated wrappers, N-API function return values (`napi_status`) are entirely ignored. For example, if `napi_get_value_string_utf8` fails because the JS value isn't a string, `lengthVar` is left uninitialized. This results in allocating a randomly sized `ByteArray`, causing potential Out-of-Memory (OOM) crashes or buffer overflows.

## 3. Stability & Error Handling

- **Missing Exception Boundary (FFI)**: The generated wrapper functions do not catch Kotlin exceptions. If a Kotlin function throws an exception, it propagates across the C/C++ FFI boundary. In Kotlin Native, unhandled exceptions crossing the C boundary immediately terminate the process. Wrappers must wrap calls in a `try-catch` block and throw a JavaScript error using `napi_throw_error`.
- **Inefficient Property Definition**: `InitEntryGenerator.kt` loops through all export functions and calls `napi_define_properties` individually with an array size of 1. It would be much more performant and idiomatic to allocate a single array of `napi_property_descriptor` for the entire module and define all properties in a single N-API call.
