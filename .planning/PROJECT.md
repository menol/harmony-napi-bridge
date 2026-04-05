# Harmony NAPI Bridge

## What This Is

This project is a Kotlin Native KSP plugin that automatically generates NAPI (Node-API) glue code for HarmonyOS. The current focus is to ensure that the generated artifacts (Kotlin Native library and C++ NAPI glue code) can be packaged and successfully run within a HarmonyOS module (`ohos`) through unit and integration testing.

## Core Value

The generated NAPI glue code must seamlessly bridge Kotlin Native and HarmonyOS ArkTS, allowing developers to call Kotlin code from HarmonyOS without manual C++ boilerplate.

## Requirements

### Validated

<!-- Shipped and confirmed valuable. -->

- ✓ Kotlin Native KSP plugin structure is set up.
- ✓ Basic NAPI C++ glue code generation logic exists.
- ✓ Project uses Gradle for build and dependency management.

### Active

<!-- Current scope. Building toward these. -->

- [ ] Create a HarmonyOS module (`ohos`) for testing purposes.
- [ ] Configure the build system to package the Kotlin Native shared library (`.so`) and generated C++ glue code into the `ohos` module.
- [ ] Write unit/integration tests in the `ohos` module to verify the generated bindings.
- [ ] Ensure the tests can be executed successfully in a HarmonyOS environment (emulator or device).

### Out of Scope

<!-- Explicit boundaries. Includes reasoning to prevent re-adding. -->

- Support for platforms other than HarmonyOS (e.g., Android, iOS) — Focus is exclusively on HarmonyOS NAPI generation.
- Full UI application in HarmonyOS — We only need a test runner or simple module to execute tests, not a complete app.

## Context

- The project relies on Kotlin Symbol Processing (KSP) to analyze Kotlin Native code and generate corresponding C++ NAPI wrappers.
- The output needs to be consumed by a HarmonyOS ArkTS/C++ environment.
- The codebase already has an `ohos` directory, which likely serves as the target HarmonyOS module for these tests.
- Testing requires bridging the generated C++ with HarmonyOS's native build system (CMake/Hvigor).

## Constraints

- **Build System**: Must integrate with Gradle (for Kotlin/KSP) and Hvigor/CMake (for HarmonyOS).
- **Environment**: Requires HarmonyOS SDK and tools to compile and run the tests.
- **Language**: Glue code must strictly adhere to NAPI standards supported by HarmonyOS ArkTS.

## Key Decisions

<!-- Decisions that constrain future work. Add throughout project lifecycle. -->

| Decision | Rationale | Outcome |
|----------|-----------|---------|
| Test via `ohos` module | Ensures the generated artifacts work in a real HarmonyOS context | — Pending |

---
*Last updated: 2026-04-05 after initialization*