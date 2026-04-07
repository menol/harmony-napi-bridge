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

**Goal:** Support exporting Kotlin interfaces with generics (e.g., interface DemoInterface<T>) to TypeScript interfaces, bypassing NAPI C++ wrapper generation.
**Requirements**: TBD
**Depends on:** Phase 7
**Plans:** 1/1 complete

Plans:
- [x] 08: Update KSP models and generators to support Kotlin interfaces and generics

### Phase 9: 支持抽象类的导出

**Goal:** Support exporting Kotlin abstract classes with generics to TypeScript abstract classes.
**Requirements**: TBD
**Depends on:** Phase 8
**Plans:** 1/1 complete

Plans:
- [x] 09: Update KSP models and generators to support Kotlin abstract classes

### Phase 10: 支持密封类的导出

**Goal:** Support exporting Kotlin sealed classes with generics to TypeScript discriminated union types.
**Requirements**: TBD
**Depends on:** Phase 9
**Plans:** 1/1 plans complete

Plans:
- [x] 10: Update KSP models and generators to support Kotlin sealed classes

### Phase 11: 支持完整的 abstract class (NAPI Object Wrapping)

**Goal:** Support exporting Kotlin abstract classes to TypeScript abstract classes, using NAPI Object Wrapping to pass instances by reference instead of copying via JSON serialization.
**Requirements**: TBD
**Depends on:** Phase 10
**Plans:** 1/1 plans complete

Plans:
- [x] 11: Update NAPI runtime and generators to support NAPI object wrapping for abstract classes

### Phase 12: 自动生成 ArkTS 的 Index 导出层 (Auto-generate ArkTS Index.ets exports)

**Goal:** Automate the generation of an `ets` file containing the exports for ArkTS and seamlessly integrate it into the HarmonyOS project.
**Requirements**: TBD
**Depends on:** Phase 11
**Plans:** 1/1 complete

Plans:
- [x] 12: Auto-generate ArkTS Index.ets exports

### Phase 13: 支持普通 Class 的导出 (Support normal class export)

**Goal:** Support exporting normal Kotlin classes to ArkTS, handling direct JS instantiation (`new Class()`) vs Kotlin returned instances via `napi_external` wrapping, mapping primary constructor parameters, and updating TS definitions.
**Requirements**: TBD
**Depends on:** Phase 12
**Plans:** 3/0 plans complete

Plans:
- [x] 13: Update Domain Models and `NapiUtils.kt` to handle normal classes and `napi_external` pointers
- [x] 13: Update KSP generators (InitEntry, KotlinWrapper, TypeScript) for normal class lifecycle
- [x] 13: Implement end-to-end testing for normal classes in HelloWorldPlugin

### Phase 14: KMP 状态管理与 Flow 跨端支持 (ArkUI V2 响应式集成)

**Goal:** Provide seamless reactivity support for KMP Data Classes and `StateFlow` by bridging them with ArkUI V2's `@ObservedV2` and `@Trace`.
**Requirements:** TBD
**Depends on:** Phase 13
**Plans:** 0 plans

Plans:
- [ ] 14: Support Kotlin `Flow`/`StateFlow` as return types and generate thread-safe JS callbacks (`napi_threadsafe_function`).
- [ ] 14: Provide Kotlin annotations (e.g., `@HarmonyObservable`) to trigger KSP generation of ArkTS `.ets` classes equipped with `@ObservedV2` and `@Trace`.
- [ ] 14: Implement `updateFrom()` or `Object.assign` deep-proxy logic in generated ArkTS classes to allow precise partial updates without breaking UI reactivity.

## Backlog

### Phase 999.1: 方案三：终极形态 —— 在 KSP 层自动生成 ArkUI V2 包装类 (探索方向) (BACKLOG)

**Goal:** 探索基于 KSP 在编译期自动生成 ArkUI V2 包装类，彻底消除调用鸿蒙系统底层 UI 的胶水代码。
**Requirements:** TBD
**Plans:** 0 plans

Plans:
- [ ] TBD (promote with /gsd-review-backlog when ready)