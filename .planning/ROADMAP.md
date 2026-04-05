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

### Phase 4: Advanced Testing

**Goal:** Implement memory leak detection and performance benchmarking
**Requirements**: ADV-01, ADV-02
**Depends on:** Phase 3
**Plans:** 0 plans

Plans:
- [ ] TBD (run /gsd-plan-phase 4 to break down)

### Phase 5: Create real ETS test page for device debugging of khn.har

**Goal:** [To be planned]
**Requirements**: TBD
**Depends on:** Phase 4
**Plans:** 0 plans

Plans:
- [ ] TBD (run /gsd-plan-phase 5 to break down)

### Phase 6: Support any type conversion for ArkTS map and list

**Goal:** [To be planned]
**Requirements**: TBD
**Depends on:** Phase 5
**Plans:** 0 plans

Plans:
- [x] TBD (run /gsd-plan-phase 6 to break down)

### Phase 7: Implement JSON-based cross-platform complex object bridge

**Goal:** Support zero-overhead data class and enum passing using kotlinx.serialization + NAPI C bindings
**Requirements**: TEST-02
**Depends on:** Phase 6
**Plans:** 1/1 complete

Plans:
- [x] 07: Implement JSON bridge for complex objects

### Phase 8: 支持接口类型的导出

**Goal:** [To be planned]
**Requirements**: TBD
**Depends on:** Phase 7
**Plans:** 0 plans

Plans:
- [ ] TBD (run /gsd-plan-phase 8 to break down)
