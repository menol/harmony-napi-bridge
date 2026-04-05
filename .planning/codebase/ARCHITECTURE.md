# Architecture

## Pattern
The project implements a **Metaprogramming-based Foreign Function Interface (FFI) Bridging Pattern**. It uses Kotlin Symbol Processing (KSP) to inspect annotated pure Kotlin business logic (`@HarmonyModule`, `@HarmonyExport`) and automatically generates the corresponding C/C++ Native-API (N-API) glue code and TypeScript type definitions. This enables Kotlin code to run on HarmonyOS without writing manual C++ wrappers.

## Layers
1. **Annotations Layer (`harmony-napi-annotations`)**: Provides the `@HarmonyModule` and `@HarmonyExport` annotations that mark Kotlin classes and functions for bridging.
2. **Code Generation Layer (`harmony-napi-ksp-processor`)**: The core KSP compiler plugin. It parses the annotated Kotlin code at compile time and generates:
   - Kotlin/Native wrappers (`_Wrapper.kt`) that map N-API calls to the Kotlin functions.
   - A central C++ initialization function (`InitGeneratedWrappers`).
   - TypeScript definition files (`Index.d.ts`) for the ArkTS frontend.
3. **Runtime Layer (`harmony-napi-runtime`)**: Provides Kotlin/Native CInterop bindings for HarmonyOS N-API and Hilog (`napi.def`, `hilog.def`), along with runtime utility functions (`NapiUtils.kt`).
4. **Business Logic Layer (`sample-plugin`)**: A pure Kotlin module where the developer writes their business logic, heavily relying on the annotations to expose functionality to HarmonyOS.
5. **Consumption Layer (`ohos`)**: The HarmonyOS project comprising a library (`khn`) and an application (`entry`). The library contains a minimal C++ entry point (`napi_init.cpp`) that dynamically links the generated Kotlin/Native shared library (`.so`) and registers the module.

## Data Flow
1. **Build Time**:
   - The Kotlin compiler runs the KSP processor on the `sample-plugin`.
   - The processor generates `[PluginName]_Wrapper.kt`, `InitGeneratedWrappers.kt` (using KotlinPoet), and `Index.d.ts`.
   - Kotlin/Native compiles the generated code and the business logic into a shared library (`libsample_plugin.so`).
   - The `deploy_to_ohos.sh` script copies the `.so`, `.h`, and `.d.ts` files into the HarmonyOS library module (`ohos/khn`).
2. **Run Time (ArkTS to Kotlin)**:
   - The ArkTS frontend calls `PluginName.method(args)`.
   - The request goes through the HarmonyOS N-API, reaching the generated `PluginName_method_wrapper` C-function.
   - The wrapper reads the `napi_value` arguments, converts them to native Kotlin types (e.g., `Double`, `String`), and invokes the actual Kotlin object method.
   - The return value is converted back to a `napi_value` and returned to ArkTS.

## Abstractions
- **`TypeMapper`**: Translates Kotlin types (like `String`, `Double`) to their N-API equivalents and TypeScript definitions.
- **`KotlinWrapperGenerator`**: Generates the memory-scoped C-Interop functions that act as N-API property descriptors.
- **`InitEntryGenerator`**: Generates a single exported C function (`InitGeneratedWrappers`) to attach all module properties to the N-API `exports` object.
- **`TypeScriptGenerator`**: Generates the `.d.ts` representation for seamless frontend integration.

## Entry Points
- **KSP Processor Entry**: `HarmonyNapiProcessorProvider.kt` and `HarmonyNapiProcessor.kt`.
- **C++ Initialization**: `napi_init.cpp` in the `ohos/khn` module, which defines the `napi_module` and calls `InitGeneratedWrappers`.
- **Build/Deployment**: `deploy_to_ohos.sh`, which orchestrates building the KMP module and injecting the artifacts into the HarmonyOS project.
