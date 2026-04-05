# Requirements: Harmony NAPI Bridge - ohos module unit tests

**Defined:** 2026-04-05
**Core Value:** The generated NAPI glue code must seamlessly bridge Kotlin Native and HarmonyOS ArkTS, allowing developers to call Kotlin code from HarmonyOS without manual C++ boilerplate.

## v1 Requirements

### Environment Setup

- [ ] **ENV-01**: Configure Hvigor/CMake in `ohos` module to build the NAPI bridge
- [ ] **ENV-02**: Create `deploy_to_ohos.sh` script to copy Kotlin Native `.so` and generated C++ to the `ohos` module
- [ ] **ENV-03**: Integrate `@ohos/hypium` test framework in the `ohos` module

### Test Cases

- [ ] **TEST-01**: Test primitive type conversions (Int, String, Boolean) between ArkTS and Kotlin
- [ ] **TEST-02**: Test complex object passing (Data classes) between ArkTS and Kotlin
- [ ] **TEST-03**: Test callback execution (passing JS function to Kotlin and invoking it)
- [ ] **TEST-04**: Test error handling (Kotlin exceptions converted to JS exceptions)

### Automation

- [ ] **AUTO-01**: Run test suite via command line (`hdc` or `hvigorw`) without UI interaction
- [ ] **AUTO-02**: Generate test report

## v2 Requirements

### Advanced Testing

- **ADV-01**: Memory leak detection during NAPI calls
- **ADV-02**: Performance benchmarking of NAPI calls

## Out of Scope

| Feature | Reason |
|---------|--------|
| UI Testing | Not testing a UI app, only testing the C++/ArkTS boundary |
| Cross-platform tests | Focus is exclusively on HarmonyOS NAPI |

## Traceability

| Requirement | Phase | Status |
|-------------|-------|--------|
| **ENV-01** | Phase 1 | Not Started |
| **ENV-02** | Phase 1 | Not Started |
| **ENV-03** | Phase 1 | Not Started |
| **TEST-01** | Phase 2 | Not Started |
| **TEST-02** | Phase 2 | Not Started |
| **TEST-03** | Phase 2 | Not Started |
| **TEST-04** | Phase 2 | Not Started |
| **AUTO-01** | Phase 3 | Not Started |
| **AUTO-02** | Phase 3 | Not Started |

**Coverage:**
- v1 requirements: 9 total
- Mapped to phases: 9
- Unmapped: 0 ✅

---
*Requirements defined: 2026-04-05*
*Last updated: 2026-04-05 after initial definition*