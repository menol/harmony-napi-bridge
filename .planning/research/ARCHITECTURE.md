# Architecture Research

**Domain:** HarmonyOS NAPI Unit Testing Architecture
**Researched:** 2026-04-05
**Confidence:** HIGH

## Standard Architecture

### System Overview

```
┌─────────────────────────────────────────────────────────────┐
│                   Test Runner Layer (ArkTS)                 │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────────┐  ┌─────────────────┐                   │
│  │ ArkTS Test Suite│  │ @ohos/test env  │                   │
│  └────────┬────────┘  └────────┬────────┘                   │
│           │                    │                            │
├───────────┴────────────────────┴────────────────────────────┤
│               HarmonyOS NAPI Boundary (C++)                 │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────┐    │
│  │     napi_init.cpp (NAPI Module Registration)        │    │
│  └─────────────────────────┬───────────────────────────┘    │
├────────────────────────────┴────────────────────────────────┤
│               Kotlin Native Generated Bridge                │
├─────────────────────────────────────────────────────────────┤
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐       │
│  │ C++ Wrappers │  │ TypeMapper   │  │ Init Function│       │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘       │
├─────────┴─────────────────┴─────────────────┴───────────────┤
│                   Business Logic Layer                      │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────┐    │
│  │           Pure Kotlin Code (@HarmonyModule)         │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

### Component Responsibilities

| Component | Responsibility | Typical Implementation |
|-----------|----------------|------------------------|
| **ArkTS Test Suite** | Executes unit/integration tests, provides inputs, and asserts expected outcomes. | `@ohos/test` framework in the `ohos/entry/src/ohosTest` directory. |
| **NAPI Init (`napi_init.cpp`)** | Registers the NAPI module with HarmonyOS and links the generated Kotlin bridge. | Minimal C++ file using `napi_module_register`. |
| **Generated Glue Code** | Translates `napi_value` arguments to Kotlin types and vice-versa; binds methods. | KSP-generated CInterop and Kotlin wrappers compiled into `.so`. |
| **Business Logic** | The actual functionality being tested. | Pure Kotlin KMP module (`sample-plugin`). |
| **Deployment Script** | Bridges the Gradle and Hvigor build systems by transferring generated artifacts. | Bash script (`deploy_to_ohos.sh`). |

## Recommended Project Structure

```
.
├── sample-plugin/                  # KMP module containing business logic
│   ├── build.gradle.kts            # Configures KSP and Kotlin/Native compilation
│   └── src/commonMain/kotlin/      # Annotated Kotlin source code
├── deploy_to_ohos.sh               # Script to bridge Gradle and Hvigor builds
└── ohos/                           # HarmonyOS test project
    ├── build-profile.json5         # Hvigor workspace configuration
    ├── khn/                        # HarmonyOS library module (Consumer)
    │   ├── src/main/cpp/           # napi_init.cpp and CMakeLists.txt
    │   ├── src/main/ets/           # ArkTS wrappers and Index.d.ts
    │   └── libs/                   # Copied .so files from KMP (git-ignored)
    └── entry/                      # HarmonyOS application module (Test Runner)
        └── src/ohosTest/ets/       # ArkTS unit and integration tests
```

### Structure Rationale

- **`sample-plugin/`:** Isolated Kotlin environment to verify the KSP plugin works correctly on real code without mingling with the compiler source.
- **`deploy_to_ohos.sh`:** Decouples the Gradle build from the Hvigor build. Since these two ecosystems don't natively interoperate, an explicit handoff script is the most reliable pattern.
- **`ohos/khn/`:** Acts as the consumption library. It mimics how a real developer would integrate the NAPI output into their own HarmonyOS project.
- **`ohos/entry/src/ohosTest/`:** The standard location for HarmonyOS instrumented tests that require a real device or emulator to load native libraries and execute the JS engine.

## Architectural Patterns

### Pattern 1: Build Bridging (Artifact Handoff)

**What:** Using a script to copy compiled native artifacts (`.so`), headers (`.h`), and TypeScript definitions (`.d.ts`) from the Kotlin build output to the HarmonyOS source tree.
**When to use:** When integrating two disparate build systems (Gradle for Kotlin, Hvigor/CMake for HarmonyOS) that cannot be easily unified.
**Trade-offs:** Requires running an extra script before testing; less seamless than a single IDE "Run" button, but extremely robust and predictable.

**Example:**
```bash
# deploy_to_ohos.sh
cp sample-plugin/build/bin/native/releaseShared/libsample_plugin.so ohos/khn/libs/arm64-v8a/
cp sample-plugin/build/generated/ksp/native/commonMain/resources/Index.d.ts ohos/khn/src/main/ets/
```

### Pattern 2: Dynamic Native Registration

**What:** A minimal, static `napi_init.cpp` that delegates all property registration to an auto-generated Kotlin/Native function (`InitGeneratedWrappers`).
**When to use:** To attach dynamically generated Kotlin wrappers to the HarmonyOS NAPI module lifecycle without modifying C++ code for each new Kotlin function.
**Trade-offs:** Keeps C++ boilerplate to near-zero, but requires careful CInterop configuration to expose the initialization function correctly.

## Data Flow

### Request Flow (Test Execution)

```
[ArkTS Test Case]
    ↓ (method call: khn.testMethod(args))
[HarmonyOS N-API Boundary]
    ↓ (routing via napi_init.cpp)
[Generated Kotlin/CInterop Wrapper]
    ↓ (TypeMapper: napi_value → Kotlin Type)
[Kotlin Business Logic]
    ↓ (Return computed value)
[Generated Kotlin/CInterop Wrapper]
    ↓ (TypeMapper: Kotlin Type → napi_value)
[HarmonyOS N-API Boundary]
    ↓
[ArkTS Test Assertion (expect(res).assertEqual(...))]
```

### Suggested Build Order

1. **KSP Processor Compilation:** Ensure the KSP plugin itself is built and published (or included via composite build).
2. **KMP Module Processing:** Gradle runs KSP on `sample-plugin` to generate Kotlin wrappers, C++ init code, and TypeScript definitions.
3. **Kotlin/Native Compilation:** Gradle compiles `sample-plugin` and the generated code into `libsample_plugin.so`.
4. **Artifact Deployment:** Execute `deploy_to_ohos.sh` to transfer the `.so`, `.h`, and `.d.ts` files into the `ohos/khn` module.
5. **CMake Build:** Hvigor invokes CMake to compile `napi_init.cpp`, dynamically linking against `libsample_plugin.so` to produce `libkhn.so`.
6. **ArkTS Compilation & Packaging:** Hvigor compiles the ArkTS test code and packages the application (HAP) containing both `.so` libraries.
7. **Test Execution:** The HAP is deployed to a HarmonyOS device/emulator, and the ArkTS test runner executes the test suites.

## Scaling Considerations

| Scale | Architecture Adjustments |
|-------|--------------------------|
| 10-50 functions | Standard setup; single `napi_init.cpp` and a single exported initialization function is sufficient. |
| 100+ functions | Kotlin/Native binary size may grow. Might need to split into multiple KMP modules and multiple NAPI modules to avoid hitting NAPI initialization timeouts. |
| Complex Types | The `TypeMapper` needs to be extended to handle deeply nested data classes or asynchronous callbacks (Promises). |

### Scaling Priorities

1. **First bottleneck:** NAPI type conversion complexity. As developers export more complex objects, the `TypeMapper` will need to support serialization (e.g., passing JSON strings vs. deeply mapping JS objects to Kotlin Data Classes).
2. **Second bottleneck:** Build times. Kotlin/Native compilation is slow. Caching the `.so` and only rebuilding when Kotlin source changes is critical for the test loop.

## Anti-Patterns

### Anti-Pattern 1: Manual C++ Testing (GoogleTest)

**What people do:** Write C++ unit tests (e.g., using GoogleTest) to verify the generated NAPI wrappers directly.
**Why it's wrong:** NAPI is tightly coupled to the ArkTS/JS engine (ArkCompiler/V8). Testing NAPI calls without the JS engine context often leads to false positives, missing memory leaks, or crashes that only happen in production.
**Do this instead:** Write ArkTS unit tests (`@ohos/test`) and run them on a real HarmonyOS emulator or device to exercise the full end-to-end NAPI lifecycle.

### Anti-Pattern 2: Committing Generated Artifacts

**What people do:** Checking `libsample_plugin.so` or the generated `Index.d.ts` into version control.
**Why it's wrong:** Causes merge conflicts, binary bloat in the git repository, and frequent desynchronization between the source Kotlin code and the tested artifacts.
**Do this instead:** Add `ohos/khn/libs/` and generated ArkTS files to `.gitignore`. Require the build pipeline or developers to run the `deploy_to_ohos.sh` script locally before testing.

## Integration Points

### Internal Boundaries

| Boundary | Communication | Notes |
|----------|---------------|-------|
| **KMP ↔ HarmonyOS (Build)** | File System (`deploy_to_ohos.sh`) | Must ensure ABI compatibility (e.g., compiling for `arm64-v8a`). |
| **ArkTS ↔ Native (Runtime)** | N-API (`napi_value`) | All data must cross the C-boundary safely. Ensure memory allocated by Kotlin is freed appropriately if passed to ArkTS. |
| **C++ ↔ Kotlin (Runtime)** | CInterop | C++ acts only as a passthrough. The heavy lifting is done by Kotlin/Native's CInterop capabilities. |

## Sources

- [HarmonyOS NAPI Bridge Project Context](file:///Users/api/Desktop/itime-rust/harmony-napi-bridge/.planning/PROJECT.md)
- [HarmonyOS NAPI Bridge Architecture](file:///Users/api/Desktop/itime-rust/harmony-napi-bridge/.planning/codebase/ARCHITECTURE.md)

---
*Architecture research for: HarmonyOS NAPI Unit Testing Architecture*
*Researched: 2026-04-05*