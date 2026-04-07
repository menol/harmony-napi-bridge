---
wave: 1
depends_on: []
files_modified:
  - "ohos/entry/src/ohosTest/ets/test/MemoryLeak.test.ets"
  - "ohos/entry/src/ohosTest/ets/test/Performance.test.ets"
  - "ohos/entry/src/ohosTest/ets/test/List.test.ets"
autonomous: true
requirements:
  - ADV-01
  - ADV-02
---

# Phase 04 Plan: Advanced Testing (Memory & Performance)

## Goal
Implement memory leak detection and performance benchmarking for the NAPI bridge.

## Must Haves
- A test suite for memory leak detection that forces ArkTS garbage collection (e.g. via `gc()`) and verifies that memory usage does not grow out of bounds after creating many NAPI bridge objects.
- A test suite for performance benchmarking that measures execution time of NAPI bridge calls (primitive and complex objects) and fails if the performance is worse than a defined threshold.
- Both test suites must be integrated into the Hypium test runner (`List.test.ets`).

## Tasks

### Wave 1: Memory Leak Detection

<task>
  <id>04-01</id>
  <description>Implement Memory Leak Detection Tests</description>
  <read_first>
    - ohos/entry/src/ohosTest/ets/test/NapiWrapper.test.ets
    - ohos/entry/src/ohosTest/ets/test/List.test.ets
  </read_first>
  <action>
    Create a new file `ohos/entry/src/ohosTest/ets/test/MemoryLeak.test.ets` containing:
    ```typescript
    import { describe, it, expect } from '@ohos/hypium';
    import hidebug from '@ohos.hidebug';
    import { TestClass } from 'khn';

    export default function memoryLeakTest() {
      describe('MemoryLeakTest', () => {
        it('should not leak memory after many object creations', 0, () => {
          const iterations = 10000;
          const initialMemory = Number(hidebug.getNativeHeapAllocatedSize());
          
          for (let i = 0; i < iterations; i++) {
            let tc = new TestClass(i);
            tc.increment();
            tc = null as any;
          }
          
          // Force Garbage Collection to ensure unreferenced objects are freed
          // Note: In ArkTS, we can use global gc() if exposed, or rely on ArkTools.
          // For standard API, we might need to use a short delay or system GC trigger if available.
          // Assuming gc() is available in the test environment.
          if (typeof gc === 'function') {
            gc();
          }
          
          const finalMemory = Number(hidebug.getNativeHeapAllocatedSize());
          const diff = finalMemory - initialMemory;
          
          // Assert the difference is less than 2MB (2 * 1024 * 1024 bytes) to ensure no leak.
          // 50MB was too permissive and could hide small leaks.
          expect(diff).assertLess(2 * 1024 * 1024);
        });
      });
    }
    ```
    Modify `ohos/entry/src/ohosTest/ets/test/List.test.ets` to import and call `memoryLeakTest()`:
    1. Add `import memoryLeakTest from './MemoryLeak.test';` at the top.
    2. Add `memoryLeakTest();` inside `export default function testsuite()`.
  </action>
  <acceptance_criteria>
    - `grep -q "memoryLeakTest" ohos/entry/src/ohosTest/ets/test/MemoryLeak.test.ets`
    - `grep -q "hidebug.getNativeHeapAllocatedSize" ohos/entry/src/ohosTest/ets/test/MemoryLeak.test.ets`
    - `grep -q "gc" ohos/entry/src/ohosTest/ets/test/MemoryLeak.test.ets`
    - `grep -q "import memoryLeakTest" ohos/entry/src/ohosTest/ets/test/List.test.ets`
    - `grep -q "memoryLeakTest();" ohos/entry/src/ohosTest/ets/test/List.test.ets`
  </acceptance_criteria>
</task>

### Wave 2: Performance Benchmarking

<task>
  <id>04-02</id>
  <description>Implement Performance Benchmarking Tests</description>
  <read_first>
    - ohos/entry/src/ohosTest/ets/test/NapiWrapper.test.ets
    - ohos/entry/src/ohosTest/ets/test/List.test.ets
  </read_first>
  <action>
    Create a new file `ohos/entry/src/ohosTest/ets/test/Performance.test.ets` containing:
    ```typescript
    import { describe, it, expect } from '@ohos/hypium';
    import { TestClass, hello_world_plugin } from 'khn';

    export default function performanceTest() {
      describe('PerformanceTest', () => {
        it('should execute 10000 primitive conversions within threshold', 0, () => {
          const iterations = 10000;
          const start = Date.now();
          for (let i = 0; i < iterations; i++) {
            hello_world_plugin.add(i, i);
          }
          const end = Date.now();
          const duration = end - start;
          // Expect 10000 simple calls to take less than 500ms
          expect(duration).assertLess(500);
        });

        it('should execute 10000 object instantiations within threshold', 0, () => {
          const iterations = 10000;
          const start = Date.now();
          for (let i = 0; i < iterations; i++) {
            const tc = new TestClass(i);
            tc.increment();
          }
          const end = Date.now();
          const duration = end - start;
          // Expect 10000 object creations and calls to take less than 1500ms
          expect(duration).assertLess(1500);
        });
      });
    }
    ```
    Modify `ohos/entry/src/ohosTest/ets/test/List.test.ets` to import and call `performanceTest()`:
    1. Add `import performanceTest from './Performance.test';` at the top.
    2. Add `performanceTest();` inside `export default function testsuite()`.
  </action>
  <acceptance_criteria>
    - `grep -q "performanceTest" ohos/entry/src/ohosTest/ets/test/Performance.test.ets`
    - `grep -q "Date.now()" ohos/entry/src/ohosTest/ets/test/Performance.test.ets`
    - `grep -q "import performanceTest" ohos/entry/src/ohosTest/ets/test/List.test.ets`
    - `grep -q "performanceTest();" ohos/entry/src/ohosTest/ets/test/List.test.ets`
  </acceptance_criteria>
</task>

<threat_model>
  - Block on: high
  - Threats:
    - **Resource Exhaustion**: If tests loop infinitely or allocate too much memory without bounds, the test device could crash or freeze. The 10,000 iterations threshold mitigates this.
    - **Flaky Tests**: Performance benchmarks are inherently flaky in virtual environments. If assertions are too tight, CI might fail intermittently. Setting a conservative upper bound (e.g. 500ms / 1500ms) prevents this while still catching gross regressions.
</threat_model>

## Verification
- Both `MemoryLeak.test.ets` and `Performance.test.ets` exist in `ohos/entry/src/ohosTest/ets/test/`.
- `List.test.ets` correctly imports and runs both suites.
- Code matches the acceptance criteria defined in tasks.
