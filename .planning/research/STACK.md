# Stack Research

**Domain:** HarmonyOS Module Testing & NAPI Integration
**Researched:** 2026-04-05
**Confidence:** HIGH

## Recommended Stack

### Core Technologies

| Technology | Version | Purpose | Why Recommended |
|------------|---------|---------|-----------------|
| `@ohos/hypium` | 1.0.6+ | ArkTS unit testing framework | Official HarmonyOS testing framework. Built into DevEco Studio, uses familiar Mocha syntax (`describe`, `it`), and directly runs ArkTS code to validate NAPI bindings. |
| DevEco Hvigor | 5.0+ | Build system & test runner | Required to package the `ohos` test module, compile the C++ NAPI glue code via CMake, and execute Hypium tests on devices/emulators. |
| CMake | 3.24+ | C/C++ build configuration | Standard for compiling the generated NAPI glue code and linking the Kotlin Native `.so` with HarmonyOS's `libace_napi.z.so`. |

### Supporting Libraries

| Library | Version | Purpose | When to Use |
|---------|---------|---------|-------------|
| GoogleTest (`gtest`) | 1.14.0+ | C++ unit testing | When testing complex C++ NAPI wrapper logic or type conversions in isolation, without the overhead of the ArkTS JS engine. |

### Development Tools

| Tool | Purpose | Notes |
|------|---------|-------|
| HDC (HarmonyOS Device Connector) | CLI device interaction and test execution | Replaces ADB. Used by Hvigor to deploy the `ohos` test module and execute tests via command line. |
| DevEco Testing | Stability and performance profiling | Useful for tracking memory leaks (OOM) across the ArkTS -> C++ -> Kotlin Native boundary. |

## Installation

```bash
# Core testing framework (usually pre-configured in DevEco Studio projects)
npm install @ohos/hypium --save-dev

# Ensure CMake is configured in the build.profile.json5 or hvigorfile.ts
```

## Alternatives Considered

| Recommended | Alternative | When to Use Alternative |
|-------------|-------------|-------------------------|
| ArkTS tests (`@ohos/hypium`) | Pure C++ tests (`gtest`) | When the primary risk is C++ memory management or data conversion logic rather than ArkTS integration. However, Hypium is better for end-to-end NAPI validation. |

## What NOT to Use

| Avoid | Why | Use Instead |
|-------|-----|-------------|
| Hamock / External Mocking | `@ohos/hypium` includes `MockKit` starting from v1.0.1. External libraries add bloat and may break with ArkTS updates. | Built-in `MockKit` via `@ohos/hypium` |
| Jest / Mocha | Not supported in the ArkTS runtime environment. They rely on Node.js/V8 specifics rather than ArkCompiler. | `@ohos/hypium` (uses Mocha-like syntax but runs natively) |
| JUnit / `kotlin.test` | Cannot test the C++ NAPI bridge or the HarmonyOS integration. Only tests pure Kotlin logic. | `@ohos/hypium` for ArkTS -> C++ -> Kotlin Native end-to-end tests |

## Stack Patterns by Variant

**If testing ArkTS-to-NAPI bindings:**
- Use `@ohos/hypium`
- Because the goal is to verify that the generated C++ glue code correctly exposes methods to the ArkTS runtime.

**If testing internal C++ NAPI wrapper logic:**
- Use `GoogleTest`
- Because it isolates the C++ data conversions and memory management from the ArkTS engine.

## Version Compatibility

| Package A | Compatible With | Notes |
|-----------|-----------------|-------|
| `@ohos/hypium` | DevEco Studio 5.0+ | `MockKit` requires v1.0.1+; Data-driven testing requires v1.0.2+. Use 1.0.6+ for best stability in 2025/2026. |

## Sources

- Web Search — Official Hypium documentation and Medium/Dev.to articles on HarmonyOS 5 / NEXT testing practices (2025). Verified that `MockKit` is built-in and Hypium is the standard.
- Web Search — GoogleTest documentation. Verified it as the industry standard for C++ testing if needed.
- Existing `.planning/codebase/STACK.md` — Identified Hvigor, CMake, and NAPI as the existing build and runtime stack.

---
*Stack research for: HarmonyOS Module Testing & NAPI Integration*
*Researched: 2026-04-05*