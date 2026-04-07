# Directory Structure

## Directory Layout
- **`harmony-napi-annotations/`**: Kotlin Multiplatform (KMP) module containing the bridging annotations (`@HarmonyModule`, `@HarmonyExport`).
- **`harmony-napi-runtime/`**: KMP module providing N-API and Hilog CInterop definitions (`.def` files) and N-API helper utilities.
- **`harmony-napi-ksp-processor/`**: JVM module containing the Kotlin Symbol Processor implementation.
  - `src/main/kotlin/.../ksp/generator/`: Code generators for Kotlin wrappers, C++ entry points, and TypeScript definitions (`KotlinWrapperGenerator.kt`, `InitEntryGenerator.kt`, `TypeScriptGenerator.kt`).
  - `src/main/kotlin/.../ksp/mapper/`: Type mapping logic (`TypeMapper.kt`).
  - `src/main/kotlin/.../ksp/HarmonyNapiProcessor.kt`: The main KSP processor entry point.
- **`sample-plugin/`**: KMP module serving as an example for the user's business code (e.g., `HelloWorldPlugin.kt`). It produces the final `.so` library.
- **`ohos/`**: The HarmonyOS project directory containing ArkTS applications and libraries.
  - **`khn/`**: HarmonyOS Native C++ Library module (Kotlin Harmony Native) that consumes the generated artifacts.
    - `libs/arm64-v8a/`: Destination directory for the compiled Kotlin/Native `.so` and `.h` files.
    - `src/main/cpp/napi_init.cpp`: Minimal C++ file defining the `napi_module` and calling the generated `InitGeneratedWrappers`.
    - `src/main/cpp/types/libkhn/Index.d.ts`: Destination directory for the generated ArkTS TypeScript type definitions.
  - **`entry/`**: HarmonyOS application module that uses the `khn` library to access the Kotlin logic.
- **`gradle/`**: Gradle configuration and wrapper scripts.
- **`deploy_to_ohos.sh`**: Bash script that builds the KMP project, extracts the generated `.so`, `.h`, and `.d.ts` files, copies them to the `ohos/khn` directory, and builds the HAR.
- **`DESIGN.md`**: Detailed architectural and design documentation outlining the bridging framework's goals and implementation.

## Key Locations
- **`sample-plugin/src/commonMain/kotlin/com.realtech/harmony/sample/HelloWorldPlugin.kt`**: Example of how a developer writes pure Kotlin business logic.
- **`ohos/khn/src/main/cpp/napi_init.cpp`**: The C++ entry point that registers the N-API module and delegates initialization to the Kotlin-generated wrappers.
- **`harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/HarmonyNapiProcessor.kt`**: The core engine that orchestrates the code generation.

## Naming Conventions
- **Generated CInterop wrappers**: End with `_wrapper` (e.g., `HelloWorldPlugin_add_wrapper`).
- **HarmonyOS Library Name**: The library is consistently named `khn` (Kotlin Harmony Native).
- **Annotations**: Start with `Harmony` (e.g., `@HarmonyModule`, `@HarmonyExport`).
- **Generated C Entry**: The central initialization function is named `InitGeneratedWrappers`.
