# Project Research Summary

**Project:** HarmonyOS NAPI Unit Testing
**Domain:** HarmonyOS Module Testing & NAPI Integration
**Researched:** 2026-04-05
**Confidence:** HIGH

## Executive Summary

The project aims to establish a robust end-to-end unit testing and integration framework for Kotlin Native code bridged to HarmonyOS via Node-API (NAPI). The core challenge lies in orchestrating two disparate build systems (Gradle for Kotlin/KSP and Hvigor/CMake for HarmonyOS) to validate auto-generated C++ bindings natively on an ArkTS runtime.

Based on our research, the recommended approach is an "Artifact Handoff" architecture. A deployment script bridges the Gradle build output (Kotlin `.so` and generated C++ wrappers) into a standard HarmonyOS test project (`ohos/khn` and `ohos/entry`). Testing should be conducted using the official `@ohos/hypium` framework, which natively executes within the ArkCompiler, providing the most accurate representation of production behavior.

The most critical risks involve manual glue code desynchronization (testing handwritten C++ instead of KSP output) and FFI boundary issues such as unhandled Kotlin exceptions crashing the ArkTS process or memory safety bugs (use-after-free) during NAPI module initialization.

## Key Findings

### Recommended Stack

Our research identifies a combination of official HarmonyOS tools and native build systems as the optimal stack.

**Core technologies:**
- **`@ohos/hypium` (1.0.6+)**: ArkTS unit testing framework — Official framework that runs directly on ArkTS, accurately validating NAPI bindings.
- **DevEco Hvigor (5.0+) & CMake (3.24+)**: Build system — Required to package the `ohos` test module, compile C++ glue code, and link the Kotlin `.so`.
- **HDC (HarmonyOS Device Connector)**: Development tool — Facilitates CLI device interaction and automated test execution.

### Expected Features

The MVP focuses on establishing the pipeline and verifying core conversions.

**Must have (table stakes):**
- **Build System Integration** — Seamless handoff between Gradle (KSP) and Hvigor.
- **Automated Test Execution** — Running tests on emulator/device via CLI.
- **NAPI Binding Verification** — Ensuring the generated C++ glue correctly loads in ArkTS.
- **Type Conversion Testing** — Validating primitive types (Int, String, Boolean) cross the bridge.

**Should have (competitive):**
- **Async/Callback Testing** — Verifying asynchronous flows and network callbacks.
- **Native Logging Capture** — Routing `hilog` to test reports.
- **CI/CD Headless Execution** — Automated execution in pipeline environments.

**Defer (v2+):**
- **Exhaustive Auto-Generated Tests** — Too complex for initial validation.
- **Memory Leak Detection** — Important but not a blocker for MVP.

### Architecture Approach

The architecture relies on decoupling the Kotlin build from the HarmonyOS build, using a script to transfer artifacts.

**Major components:**
1. **`sample-plugin/`** — KMP module containing pure Kotlin business logic and KSP annotations.
2. **`deploy_to_ohos.sh`** — Bash script responsible for artifact handoff between Gradle and Hvigor.
3. **`ohos/khn` & `ohos/entry`** — HarmonyOS library (consumer) and application (test runner) modules for executing `@ohos/hypium` tests.

### Critical Pitfalls

Avoid these common errors to ensure stability and accuracy.

1. **Manual Glue Code Desync** — Forbid manual `napi_module_register` in tests; ensure CMake only links KSP-generated output.
2. **Memory Safety in Initialization** — Allocate N-API structs (like module names) on the heap or static memory to avoid use-after-free after `memScoped` blocks exit.
3. **Missing FFI Exception Boundary** — Wrap all Kotlin calls in `try-catch` within the C++ wrapper to prevent process crashes, throwing JS errors instead.
4. **Ignoring N-API Status Codes** — Strictly check `napi_status == napi_ok` to avoid uninitialized memory or OOM crashes.
5. **Uninitialized Array Dereference** — Validate `argc` against expected arguments before accessing the `argv` array.

## Implications for Roadmap

Based on research, suggested phase structure:

### Phase 1: Build System Integration & Artifact Handoff
**Rationale:** The tests cannot run until the Gradle plugin and Hvigor correctly package the Kotlin `.so` and C++ glue code together.
**Delivers:** `deploy_to_ohos.sh` script and a basic `ohos` project structure configured with CMake.
**Addresses:** Build System Integration.
**Avoids:** Manual Glue Code Desync (by automating the transfer of generated code).

### Phase 2: Core N-API Wrapper Generation & Memory Safety
**Rationale:** Before writing complex tests, the foundational N-API initialization and function registration must be robust and memory-safe.
**Delivers:** Correct KSP generation of `napi_module_register` and safe property definitions.
**Uses:** CMake, Kotlin/Native CInterop.
**Implements:** NAPI Init component and dynamic native registration pattern.

### Phase 3: FFI Boundary & Type Conversion Testing
**Rationale:** With the module loading correctly, the focus shifts to ensuring data safely crosses the C/Kotlin boundary without crashing.
**Delivers:** Argument validation, exception handling (`try-catch`), and primitive type conversion tests.
**Addresses:** Type Conversion Testing, NAPI Binding Verification.
**Avoids:** Missing FFI Exception Boundary, Ignoring N-API Status Codes, Uninitialized Array Dereference.

### Phase 4: Automated Test Execution Pipeline
**Rationale:** Once tests can be written and run manually, automate the process for continuous validation.
**Delivers:** Headless test runner integration via HDC and `@ohos/hypium`.
**Uses:** `@ohos/hypium`, HDC.
**Addresses:** Automated Test Execution.

### Phase Ordering Rationale

- We must establish the physical build bridge (Phase 1) before we can test any generated code.
- We address memory safety (Phase 2) immediately to prevent debugging nightmare crashes during later test development.
- FFI boundary and type conversions (Phase 3) form the core feature set to be verified.
- Automation (Phase 4) caps off the MVP, enabling CI integration.

### Research Flags

Phases likely needing deeper research during planning:
- **Phase 1:** Requires deep understanding of Hvigor's `build-profile.json5` and CMake integration.
- **Phase 3:** Needs careful mapping of Kotlin Native exceptions to N-API error throwing.
- **Phase 4:** Headless execution of `@ohos/hypium` via HDC CLI might lack clear documentation.

## Confidence Assessment

| Area | Confidence | Notes |
|------|------------|-------|
| Stack | HIGH | Hypium and CMake are standard for HarmonyOS Native development. |
| Features | HIGH | Clear alignment on MVP requirements for validating the bridge. |
| Architecture | HIGH | Artifact handoff is the most reliable pattern for disjoint build ecosystems. |
| Pitfalls | HIGH | Identified issues directly map to common C/C++ FFI challenges. |

**Overall confidence:** HIGH

### Gaps to Address

- **Hvigor CLI Headless Testing:** Need to validate the exact HDC commands to trigger `@ohos/hypium` tests without DevEco Studio UI.

## Sources

### Primary (HIGH confidence)
- [STACK.md](file:///Users/api/Desktop/itime-rust/harmony-napi-bridge/.planning/research/STACK.md) — Tech stack and toolchain
- [FEATURES.md](file:///Users/api/Desktop/itime-rust/harmony-napi-bridge/.planning/research/FEATURES.md) — Requirements and scope
- [ARCHITECTURE.md](file:///Users/api/Desktop/itime-rust/harmony-napi-bridge/.planning/research/ARCHITECTURE.md) — Integration patterns
- [PITFALLS.md](file:///Users/api/Desktop/itime-rust/harmony-napi-bridge/.planning/research/PITFALLS.md) — Technical risks

---
*Research completed: 2026-04-05*
*Ready for roadmap: yes*
