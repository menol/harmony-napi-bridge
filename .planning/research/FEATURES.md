# Feature Research

**Domain:** HarmonyOS NAPI Unit Testing
**Researched:** 2026-04-05
**Confidence:** HIGH

## Feature Landscape

### Table Stakes (Users Expect These)

Features users assume exist. Missing these = product feels incomplete.

| Feature | Why Expected | Complexity | Notes |
|---------|--------------|------------|-------|
| Automated Test Execution | Need a reliable way to run tests automatically without manual interaction | MEDIUM | Must execute within the `ohos` module environment (emulator/device). |
| NAPI Binding Verification | Core purpose of the testing suite: ensuring generated bindings actually work in ArkTS | HIGH | Requires testing the successful loading of the `.so` library and C++ glue code via Hvigor/CMake. |
| Type Conversion Testing | Developers expect standard types (primitives, strings) to cross the NAPI bridge accurately | MEDIUM | Must cover basic Kotlin primitives -> NAPI types. |
| Build System Integration | The test module must seamlessly integrate Gradle (KSP) and Hvigor (HarmonyOS build) | HIGH | Complex due to cross-ecosystem orchestration (JVM/Kotlin + Node-API/ArkTS). |

### Differentiators (Competitive Advantage)

Features that set the product apart. Not required, but valuable.

| Feature | Value Proposition | Complexity | Notes |
|---------|-------------------|------------|-------|
| Async/Callback Testing | Verifies custom networking bridges (e.g., `registerNetworkCallback`) and asynchronous flows | HIGH | Critical for real-world usage where ArkTS needs to trigger HTTP calls and wait for native Kotlin callbacks. |
| Exhaustive Auto-Generated Tests | Tests are automatically generated for *ALL* exported KHN methods based on KSP metadata | HIGH | Eliminates manual test authoring, ensuring 100% coverage of the generated NAPI glue. |
| Memory Leak Detection | Ensures the NAPI bridge properly manages object lifecycles and avoids memory leaks | HIGH | Requires tooling to track NAPI reference counts during test runs. |
| Native Logging Capture | Routes Hilog output into test reports for easier debugging | LOW | Bridges `hilog.def` calls from Kotlin into the test runner's console. |

### Anti-Features (Commonly Requested, Often Problematic)

Features that seem good but create problems.

| Feature | Why Requested | Why Problematic | Alternative |
|---------|---------------|-----------------|-------------|
| Full UI Test Application | Seems intuitive to see tests run on a screen | Introduces UI flakiness, requires maintenance of ArkUI components, slows down CI execution | Headless/Console-based test runner executed via command line in the `ohos` module |
| Cross-Platform Test Suite | Developers want to test Android/iOS bindings in the same suite | Project focus is exclusively on HarmonyOS NAPI generation; dilutes focus and adds immense complexity | Maintain a dedicated `ohos` module for HarmonyOS validation only |

## Feature Dependencies

```
[Build System Integration]
    └──requires──> [Automated Test Execution]
                       └──requires──> [NAPI Binding Verification]

[NAPI Binding Verification]
    └──requires──> [Type Conversion Testing]
    └──requires──> [Async/Callback Testing]

[Exhaustive Auto-Generated Tests] ──enhances──> [NAPI Binding Verification]
```

### Dependency Notes

- **[Automated Test Execution] requires [Build System Integration]:** The tests cannot run until the Gradle plugin and Hvigor correctly package the Kotlin `.so` and C++ glue code together.
- **[NAPI Binding Verification] requires [Automated Test Execution]:** Verification happens by executing the test runner.
- **[Exhaustive Auto-Generated Tests] enhances [NAPI Binding Verification]:** Moves verification from manual to comprehensive and automated.

## MVP Definition

### Launch With (v1)

Minimum viable product — what's needed to validate the concept.

- [ ] Build System Integration — Essential for packaging the generated artifacts into the testable `ohos` module.
- [ ] Automated Test Execution — Basic capability to manually trigger a test suite on an emulator/device.
- [ ] NAPI Binding Verification — Verifying basic synchronous calls from ArkTS to Kotlin Native.
- [ ] Type Conversion Testing — Ensuring primitive types (Int, String, Boolean) cross the bridge successfully.

### Add After Validation (v1.x)

Features to add once core is working.

- [ ] Async/Callback Testing — Validate `registerNetworkCallback` and complex asynchronous interactions.
- [ ] Native Logging Capture — Intercept `hilog` to improve debugging during test failures.
- [ ] CI/CD Headless Execution — Running the `ohos` tests automatically in CI environments.

### Future Consideration (v2+)

Features to defer until product-market fit is established.

- [ ] Exhaustive Auto-Generated Tests — High complexity; better to start with manual tests of the generated code before auto-generating the tests themselves.
- [ ] Memory Leak Detection — Important but not critical for initial validation of the NAPI bridge generation.

## Feature Prioritization Matrix

| Feature | User Value | Implementation Cost | Priority |
|---------|------------|---------------------|----------|
| Build System Integration | HIGH | HIGH | P1 |
| NAPI Binding Verification | HIGH | MEDIUM | P1 |
| Type Conversion Testing | HIGH | LOW | P1 |
| Automated Test Execution | HIGH | MEDIUM | P1 |
| Async/Callback Testing | HIGH | HIGH | P2 |
| Native Logging Capture | MEDIUM | LOW | P2 |
| CI/CD Headless Execution | HIGH | MEDIUM | P2 |
| Exhaustive Auto-Generated Tests | MEDIUM | HIGH | P3 |
| Memory Leak Detection | MEDIUM | HIGH | P3 |

**Priority key:**
- P1: Must have for launch
- P2: Should have, add when possible
- P3: Nice to have, future consideration

## Competitor Feature Analysis

| Feature | KMP (Kotlin Multiplatform) standard | React Native (JSI) | Our Approach (KHN NAPI) |
|---------|-------------------------------------|--------------------|-------------------------|
| HarmonyOS Binding | Not natively supported without manual C++ | Community forks exist, complex C++ | Automated NAPI C++ glue generation via KSP |
| Testing Strategy | Standard unit tests in common code | Custom C++ test runners | Run directly within an `ohos` test module via ArkTS test framework |

## Sources

- `.planning/PROJECT.md`
- `.planning/codebase/INTEGRATIONS.md`
- HarmonyOS Node-API documentation guidelines

---
*Feature research for: HarmonyOS NAPI Unit Testing*
*Researched: 2026-04-05*