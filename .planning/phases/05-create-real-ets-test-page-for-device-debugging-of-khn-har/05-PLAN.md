---
phase: 05
slug: create-real-ets-test-page-for-device-debugging-of-khn-har
---

# Phase 05 Plan: Create real ETS test page for device debugging of khn.har

**must_haves**:
- A single "Run Tests" button that triggers Hypium tests.
- UI explicitly displays memory usage differences using `@ohos.hidebug`.
- Test results (via `printSync` / `print`) appear in a scrollable text area on screen.
- Async coroutine NAPI calls are awaited in tests.
- App startup queries `@ohos.faultLogger` for `CPP_CRASH` and displays it.

---
wave: 1
depends_on: []
files_modified:
  - ohos/entry/src/main/ets/test/KhnTest.ets
autonomous: true
---

### Task 1.1: Create KhnTest.ets test suite

<read_first>
- ohos/khn/src/main/cpp/types/libkhn/Index.d.ts
</read_first>

<action>
Create `ohos/entry/src/main/ets/test/KhnTest.ets` to contain a Hypium test suite for the `hello_world_plugin` NAPI bindings.

1. Import `describe`, `it`, `expect` from `@ohos/hypium`.
2. Import `hello_world_plugin` from `khn`.
3. Export a default function `export default function khnTests()`.
4. Inside `khnTests`, define a `describe('KhnNapiTests', () => { ... })` block.
5. Add the following `it` blocks:
   - `it('should add two numbers', 0, () => { ... })` asserting `hello_world_plugin.add(12, 30)` equals `42`.
   - `it('should greet with name', 0, () => { ... })` asserting `hello_world_plugin.greet('Harmony')` is a string (e.g. using `expect(...).assertContain('Kotlin')` or similar depending on actual string).
   - `it('should process list', 0, () => { ... })` passing an array and checking the returned array.
   - `it('should process map', 0, () => { ... })` passing a Record and checking the returned map.
   - `it('should support async coroutine returns (placeholder)', 0, async () => { ... })` which is an async function that awaits a `Promise.resolve(true)` to demonstrate async test runner support.
</action>

<acceptance_criteria>
- `ohos/entry/src/main/ets/test/KhnTest.ets` contains `export default function khnTests()`
- `ohos/entry/src/main/ets/test/KhnTest.ets` contains `describe('KhnNapiTests'`
- `ohos/entry/src/main/ets/test/KhnTest.ets` contains `it('should add two numbers'`
- `ohos/entry/src/main/ets/test/KhnTest.ets` contains `async () => {`
</acceptance_criteria>

---
wave: 1
depends_on: []
files_modified:
  - ohos/entry/src/main/ets/pages/Index.ets
autonomous: true
---

### Task 1.2: Refactor Index.ets Layout & Logger

<read_first>
- ohos/entry/src/main/ets/pages/Index.ets
- .planning/phases/05-create-real-ets-test-page-for-device-debugging-of-khn-har/05-UI-SPEC.md
</read_first>

<action>
Overhaul `ohos/entry/src/main/ets/pages/Index.ets` to be a test runner UI.

1. Replace the existing `@State` variables (name, addResult, greetResult, listResult, mapResult) with:
   - `@State logText: string = "No Tests Executed\nTap 'Run Tests' to execute the khn.har test suite."`
   - `@State metricsText: string = 'Memory Diff: 0 KB | Time: 0 ms'`
   - `@State isRunning: boolean = false`
2. Update the UI `build()` method to use the design system:
   - A `Column` containing a `Text('khn.har 真机联调面板')` (24vp, Bold).
   - A `Text(this.metricsText)` (14vp, `#666666`).
   - A `Row` with two buttons: `Button('Run Tests')` (color `#007DFF`) and `Button('Clear Logs')`.
   - A `Divider()`.
   - A `Scroll` containing a `Text(this.logText)` (12vp) inside a `Column` with background `#F1F3F5` (Light Gray).
3. Update the `appendLog` method to:
   - `this.logText += message + '\n';`
   - `hilog.info(DOMAIN, TAG, '%{public}s', message);`
4. Wire the "Clear Logs" button to clear `logText` and `metricsText`.
</action>

<acceptance_criteria>
- `ohos/entry/src/main/ets/pages/Index.ets` contains `@State metricsText: string = 'Memory Diff: 0 KB | Time: 0 ms'`
- `ohos/entry/src/main/ets/pages/Index.ets` contains `Button('Run Tests')`
- `ohos/entry/src/main/ets/pages/Index.ets` contains `Button('Clear Logs')`
- `ohos/entry/src/main/ets/pages/Index.ets` contains `Text(this.logText)`
- `ohos/entry/src/main/ets/pages/Index.ets` does not contain `Button('调用 add(12, 30)')`
</acceptance_criteria>

---
wave: 2
depends_on: [Task 1.1, Task 1.2]
files_modified:
  - ohos/entry/src/main/ets/pages/Index.ets
autonomous: true
---

### Task 2.1: Implement Programmatic Test Execution & Metrics

<read_first>
- ohos/entry/src/main/ets/pages/Index.ets
- ohos/entry/src/main/ets/test/KhnTest.ets
</read_first>

<action>
Integrate `@ohos/hypium`, `@ohos.hidebug`, and `@ohos.faultLogger` into `Index.ets` to execute the tests and capture metrics/crashes.

1. Import `Core` from `@ohos/hypium`.
2. Import `khnTests` from `../test/KhnTest`.
3. Import `hidebug` from `@ohos.hidebug` and `faultLogger` from `@ohos.faultLogger`.
4. Add a private property `private testsRegistered: boolean = false;` to the `Index` struct.
5. In `Index.ets`, add an `aboutToAppear()` lifecycle hook:
   - Call `faultLogger.query(faultLogger.FaultType.CPP_CRASH)` to check for native crashes.
   - If a crash array is returned with `length > 0`, format the first item's `reason` and `module` and append to logs: `Crash Detected: ...`.
6. Implement the "Run Tests" button `onClick`:
   - Set `this.isRunning = true`.
   - Record start memory `const startMem = hidebug.getNativeHeapAllocatedSize()`.
   - Record start time `const startTime = Date.now()`.
   - If `!this.testsRegistered`, call `khnTests()` and set `this.testsRegistered = true`.
   - Create a mock `abilityDelegator` object:
     ```typescript
     const mockDelegator = {
       addAbilityMonitor: () => {},
       removeAbilityMonitor: () => {},
       waitAbilityMonitor: () => Promise.resolve(),
       getAppContext: () => null,
       getAbilityState: () => 0,
       getCurrentTopAbility: () => Promise.resolve(null),
       print: (msg: string) => { this.appendLog(msg); },
       printSync: (msg: string) => { this.appendLog(msg); },
       finishTest: (msg: string, code: number, callback: () => void) => {
         this.appendLog(`Test Finished: ${msg} (Code: ${code})`);
         const endMem = hidebug.getNativeHeapAllocatedSize();
         const endTime = Date.now();
         const memDiff = endMem - startMem;
         const timeDiff = endTime - startTime;
         this.metricsText = \`Memory Diff: \${memDiff} KB | Time: \${timeDiff} ms\`;
         this.isRunning = false;
         callback();
       }
     };
     ```
   - Execute the tests: `Core.getInstance().execute(mockDelegator as any)`.
</action>

<acceptance_criteria>
- `ohos/entry/src/main/ets/pages/Index.ets` contains `import { Core } from '@ohos/hypium'`
- `ohos/entry/src/main/ets/pages/Index.ets` contains `import hidebug from '@ohos.hidebug'`
- `ohos/entry/src/main/ets/pages/Index.ets` contains `import faultLogger from '@ohos.faultLogger'`
- `ohos/entry/src/main/ets/pages/Index.ets` contains `faultLogger.query(faultLogger.FaultType.CPP_CRASH)`
- `ohos/entry/src/main/ets/pages/Index.ets` contains `Core.getInstance().execute(`
- `ohos/entry/src/main/ets/pages/Index.ets` contains `printSync: (msg: string) =>`
- `ohos/entry/src/main/ets/pages/Index.ets` contains `hidebug.getNativeHeapAllocatedSize()`
</acceptance_criteria>
