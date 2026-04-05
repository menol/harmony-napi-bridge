# Roadmap: Harmony NAPI Bridge - ohos module unit tests

## Phase 1: Build System Integration & Environment Setup
**Requirements:** ENV-01, ENV-02, ENV-03

**Success Criteria:**
- `deploy_to_ohos.sh` successfully copies `.so` and C++ files to the `ohos` module.
- `ohos` module builds successfully using `hvigorw` and CMake without linking errors.
- `@ohos/hypium` test framework is configured and can execute a baseline empty test.

## Phase 2: NAPI Binding Test Cases
**Requirements:** TEST-01, TEST-02, TEST-03, TEST-04

**Success Criteria:**
- Tests for primitive type conversions (Int, String, Boolean) pass in the Hypium runner.
- Tests for complex object passing (Data classes) pass successfully.
- Tests for callback execution pass successfully.
- Tests verify that Kotlin exceptions are correctly thrown and caught as JS exceptions.

## Phase 3: Automation & Reporting
**Requirements:** AUTO-01, AUTO-02

**Success Criteria:**
- Tests can be fully triggered via CLI (`hdc` or `hvigorw`) without UI interaction.
- Test execution generates a readable and parsable test report.
- The end-to-end pipeline (build Kotlin -> deploy -> run tests) can be executed via a single script.
