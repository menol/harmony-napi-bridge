# Phase 5: Create real ETS test page for device debugging of khn.har - Research

## Context Understanding
The goal of this phase is to transform the manual `Index.ets` testing page into an automated, interactive test runner using `@ohos/hypium`. It needs to execute tests programmatically, display results, show memory metrics via `@ohos.hidebug`, and capture native crashes via `@ohos.faultLogger`.

## Key Findings

### 1. App Entry / UI Architecture (D-01 & D-04)
- **ArkUI State**: `Index.ets` needs to be refactored to use `@State` variables for tracking test execution status, log outputs, and metrics.
- **Layout**: The UI can be split into a control panel (run buttons, metrics summary) and a `Scroll` view containing a `Text` component for the detailed log console.
- **Logging**: Since native `hilog` cannot be easily intercepted programmatically in ArkTS, we will implement a custom logger utility that appends messages to the UI's `@State logText` while simultaneously calling `hilog.info()`.

### 2. Programmatic Hypium Integration (D-02)
- `@ohos/hypium` provides a `Core` class (`import { Core } from '@ohos/hypium'`).
- The standard test execution flow is `Core.getInstance().execute(abilityDelegator)`.
- **Capturing Results**: Hypium relies on an `abilityDelegator` object (which provides `printSync` and `finishTest` methods) to output test reports. To display results in our UI, we can provide a **mock abilityDelegator** to `Core.getInstance().execute()`. This mock will intercept `printSync` calls and pipe the formatted test results directly into our UI log console.
- Tests can be defined using the standard `describe` and `it` functions, but located directly within the `entry/src/main/ets` directory instead of the isolated `ohosTest` directory.

### 3. Memory & Performance Metrics (D-03)
- **API**: The `@ohos.hidebug` module provides `getNativeHeapAllocatedSize()` and `getNativeHeapSize()`.
- **Implementation**: We can capture `hidebug.getNativeHeapAllocatedSize()` before the test suite starts and after it finishes (or around individual tests) to calculate the native memory diff, helping detect memory leaks in the NAPI bridge.
- Execution time is already tracked by Hypium, but we can also wrap our test runner execution in a simple timer for overall suite duration.

### 4. Native Crash Capture (D-05)
- **API**: `@ohos.faultLogger` provides `query(faultType)` where `faultType` can be `faultLogger.FaultType.CPP_CRASH` (value: 2).
- **Implementation**: Since a native crash will terminate the app, we cannot display it during the test run. Instead, we can query `faultLogger` during the `aboutToAppear()` lifecycle method of `Index.ets`. If a `CPP_CRASH` from our application is found (based on the timestamp or process info), we can immediately display the crash stack trace in the UI console for post-crash review.

### 5. Async Coroutine Support (D-06)
- Hypium's `it` blocks natively support `async` functions.
- Kotlin `suspend` functions exported via the NAPI bridge will return Promises. The test cases can simply `await` these calls. The programmatic Hypium runner will naturally wait for these Promises to resolve or reject, fulfilling the requirement to test asynchronous coroutine returns.

## Recommended Plan Outline
Based on these findings, Phase 5 should be planned with the following tasks:

1. **Task 1: Basic UI Skeleton & Logger Setup** 
   - Refactor `Index.ets` layout.
   - Implement the dual-output logger (UI + hilog).
2. **Task 2: Hypium Runner Integration** 
   - Create a mock `abilityDelegator`.
   - Wire up `Core.getInstance().execute()` to a "Run Tests" button.
3. **Task 3: Test Suite Migration & Async Tests** 
   - Create a structured `describe`/`it` test suite for `khn.har`.
   - Add specific tests covering synchronous methods and asynchronous coroutines (Promises).
4. **Task 4: Metrics & Crash Detection** 
   - Integrate `@ohos.hidebug` to calculate and display memory diffs.
   - Add `@ohos.faultLogger.query()` on app startup to detect and display previous native crashes.

This research covers all necessary context to safely proceed with `/gsd-plan-phase 5`.