# Phase 04 Research: Advanced Testing & Performance

## 1. Goal & Context
Phase 4 (currently named "1" in the roadmap) is the logical progression to address the **v2 Requirements** defined in `REQUIREMENTS.md`, which focus on Advanced Testing:
- **ADV-01**: Memory leak detection during NAPI calls.
- **ADV-02**: Performance benchmarking of NAPI calls.

Since this phase depends on the test automation setup established in Phase 3, the planning must focus on how to integrate memory profiling and benchmarking into the automated CI/CD pipeline.

## 2. What We Need to Know to Plan Phase 4 Well

### A. Memory Leak Detection (ADV-01)
To verify that the generated NAPI bridge correctly manages memory boundaries between Kotlin Native and ArkTS, we need to research:
1. **Native Profiling Tools**: What memory profiling tools does HarmonyOS provide for C++? Can we enable AddressSanitizer (ASan) or LeakSanitizer (LSan) in the Hvigor/CMake build configuration (`ohos` module)?
2. **NAPI Object Lifecycle Hooks**: How can we programmatically assert object destruction in tests? We need to investigate NAPI functions like `napi_add_env_cleanup_hook`, finalizers via `napi_wrap`, and weak references to ensure Kotlin Native objects are properly freed when ArkTS objects are garbage-collected.
3. **Hypium Integration**: Does `@ohos/hypium` provide a mechanism to trigger ArkTS garbage collection deterministically? Deterministic GC is crucial for writing reliable memory leak tests.
4. **Kotlin Native Memory Manager**: How does the Kotlin Native memory manager interact with the NAPI bridge? Are there specific edge cases when passing complex data structures or callbacks back and forth?

### B. Performance Benchmarking (ADV-02)
To accurately measure the overhead of our generated NAPI bridge, we need to determine:
1. **Benchmarking Frameworks**: What is the standard way to write micro-benchmarks in HarmonyOS? Can we use a C++ framework (like Google Benchmark) invoked via NAPI, or should we rely on ArkTS performance APIs and native tracing tools like `hiTrace`/`bytrace`?
2. **Key Metrics to Measure**:
   - Primitive conversion latency (Int, Boolean, etc.).
   - Complex object conversion overhead (e.g., Data Classes).
   - String encoding/decoding overhead (UTF-8 ↔ UTF-16).
   - Callback invocation delay across the language boundary.
3. **Measurement Accuracy**: How to structure benchmark tests to account for ArkTS engine warmup (JIT/AOT) and Kotlin Native initialization overhead to ensure statistically significant results.

### C. Automation & Reporting Integration (Dependency on Phase 3)
Since Phase 4 depends on Phase 3:
1. **Artifact Extraction**: How to export benchmark results and memory profiling logs from the HarmonyOS environment (via `hdc` or `hvigorw`) into the standard test report.
2. **Regression Tracking**: How to establish baseline metrics and configure the automated pipeline to fail if performance drops below a certain threshold or if memory leaks are detected.

## 3. Recommended Next Steps for Planning
- **Update Roadmap**: Update the phase title in `ROADMAP.md` from "1" to "Advanced Testing (Memory & Performance)" and explicitly map `ADV-01` and `ADV-02` to Phase 4.
- **Proof of Concept**: Conduct a brief POC to trigger GC in Hypium and observe NAPI finalizers before finalizing the plan.
- **Generate Plan**: Run `/gsd-plan-phase 4` to break down these research findings into concrete implementation tasks.