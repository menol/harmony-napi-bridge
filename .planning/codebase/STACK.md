# Technology Stack

## Languages
- **Kotlin**: Core language used for writing the NAPI bridge logic via Kotlin Multiplatform (KMP).
- **TypeScript / ArkTS**: Used on the OpenHarmony side to interact with the generated C++ wrappers and build UI.
- **C++**: Used for the NAPI module entry point (`napi_init.cpp`) to load the Kotlin Native shared library.
- **JSON5**: Configuration files for the OpenHarmony project.

## Runtime
- **Kotlin Native (linuxArm64)**: Compiles Kotlin directly to a native shared library (`.so`) for OpenHarmony.
- **ArkTS Runtime / Node-API (NAPI)**: The OpenHarmony C++ interop layer used to execute JavaScript/ArkTS and bridge to native code.

## Frameworks
- **Kotlin Multiplatform**: Multiplatform logic organization, using Kotlin Native target for OpenHarmony.
- **KSP (Kotlin Symbol Processing)**: Processes `@HarmonyExport` annotations and automatically generates Kotlin C-interop NAPI glue code and TypeScript definitions using **KotlinPoet**.
- **OpenHarmony SDK**: The target OS framework, including **ArkUI** for declarative UI.

## Dependencies
- **ace_napi.z & hilog_ndk.z**: HarmonyOS NAPI and logging libraries linked via `cinterop`.
- **Ktor / Coil / Koin / kotlinx.coroutines**: Ecosystem libraries defined in the `libs.versions.toml` to support multiplatform logic and networking (though primarily used as general template configs).
- **Hypium & Hamock**: OpenHarmony testing frameworks.

## Configuration & Build
- **Gradle**: Multi-project build system managing the KMP projects (`harmony-napi-runtime`, `harmony-napi-annotations`, `sample-plugin`, etc.).
- **Hvigor**: The OpenHarmony build tool running `hvigorfile.ts` to build the ArkTS entry app and the `khn` native module.
- **CMake**: Configures the C++ compilation for the NAPI module, linking the `libsample_plugin.so` with `libace_napi.z.so`.