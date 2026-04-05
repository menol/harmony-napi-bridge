# Codebase Conventions

This document outlines the coding standards, patterns, and conventions used in the `harmony-napi-bridge` project, which connects Kotlin Multiplatform (KMP) code to OpenHarmony's N-API using Kotlin/Native and KSP.

## 1. Architecture & Core Patterns

- **Kotlin Symbol Processing (KSP) over Manual Binding**: Developers should rarely, if ever, write raw N-API `cinterop` bindings manually. Instead, use the custom annotations `@HarmonyModule` and `@HarmonyExport` on standard Kotlin objects and functions.
- **Code Generation**: KSP automatically generates the necessary Kotlin/Native C-Interop wrappers (e.g., `${ClassName}_${FunctionName}_wrapper`), N-API initialization entries (`InitGeneratedWrappers`), and TypeScript declaration files (`Index.d.ts`).
- **Platform Specificity**: 
  - Common logic belongs in `commonMain`.
  - OpenHarmony N-API logic and `cinterop` definitions belong in `linuxArm64Main` or `nativeMain`.
  - The OpenHarmony App/Module logic is maintained in the `ohos/` directory using ArkTS (eTS).

## 2. Memory Management & C-Interop

When writing or extending N-API bridging utilities (e.g., in `NapiUtils.kt`):

- **Scoped Memory Allocation**: Always use `memScoped { ... }` to manage the lifecycle of C pointers and allocated memory block. This ensures that memory allocated via `alloc<T>()` or `allocArray<T>()` is automatically freed when the block exits.
- **Foreign API Opt-in**: Interacting with N-API requires opting into experimental Kotlin/Native features. Use `@OptIn(ExperimentalForeignApi::class)` and `@OptIn(ExperimentalNativeApi::class)` on functions interacting with C-Interop.
- **String Handling**: When converting `napi_value` to Kotlin `String`, handle memory carefully. First, query the required buffer length by passing `null` to `napi_get_value_string_utf8`, allocate a `ByteArray` of `length + 1`, retrieve the data using `refTo(0)`, and finally decode it to a Kotlin String discarding the null-terminator.

## 3. Error Handling

- **N-API Status Checks**: *(Recommended)* When making calls to N-API (e.g., `napi_get_value_double`, `napi_create_string_utf8`), you should check the returned `napi_status`. Instead of forcefully unwrapping nullable values (using `!!`), properly handle cases where the status is not `napi_ok` by throwing descriptive Kotlin Exceptions that can be caught or passed back to the JS context.
- **Nullability**: Kotlin's type system handles nullability strictly. Ensure that optional parameters in JS are reflected as nullable types in Kotlin (`T?`) and that the KSP generators handle `napi_value` nullability correctly.

## 4. Naming Conventions

- **Annotations**: Use `PascalCase` (e.g., `@HarmonyModule`, `@HarmonyExport`).
- **Generated Wrappers**: Follow the pattern `${ClassName}_${FunctionName}_wrapper`.
- **N-API Init Export**: The generated entry point must be exported to C using `@CName("InitGeneratedWrappers")`.
- **ArkTS/ETS Modules**: The native OpenHarmony module is typically named using lowercase (e.g., `khn` for the library name).

## 5. Dependency Management

- **Gradle Version Catalogs**: Use `gradle/libs.versions.toml` to manage dependencies and plugin versions centrally across the Kotlin Multiplatform project.
- **Hvigor**: The OpenHarmony module (`ohos/`) uses Hvigor (`hvigorfile.ts`) and `oh-package.json5` for dependency management. Keep these separate from the Gradle build system but ensure the compiled `.so` outputs are properly synced to the `ohos/khn/libs/` directory.
