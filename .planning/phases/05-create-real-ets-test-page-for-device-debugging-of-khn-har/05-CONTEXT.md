# Phase 5: Create real ETS test page for device debugging of khn.har - Context

**Gathered:** 2026-04-05 (assumptions mode)
**Status:** Ready for planning

<domain>
## Phase Boundary

Build an interactive ETS test page within the HarmonyOS `ohos` application module to execute, visualize, and debug tests against the imported `khn.har` library (generated NAPI bindings) directly on a device or simulator.

</domain>

<decisions>
## Implementation Decisions

### App Entry / UI Architecture
- **D-01:** The existing `Index.ets` page will be refactored into a dynamic test runner UI, replacing hardcoded method calls with a dynamically or statically registered list of test cases.

### Test Runner Integration
- **D-02:** The test page will integrate programmatically with `@ohos/hypium` to execute structured test suites and display results directly on the device screen. (using `hypium.Core.getInstance().execute()`)

### Memory & Performance Metrics Display
- **D-03:** The UI will explicitly display memory usage and performance metrics (e.g., execution time, memory diff) alongside the test results, utilizing `@ohos.hidebug` to query native heap metrics.

### Logging and Output Console
- **D-04:** The simple logging area will be expanded into a dedicated, scrollable log console that can capture and format detailed native errors and standard `hilog` outputs.

### Native Crash Capture
- **D-05:** C++ exceptions will be caught at the NAPI boundary and converted to JS errors using `napi_throw_error`. Fatal native crashes will be captured via `@ohos.faultLogger` for post-crash review.

### Folded Todos
- **D-06:** 支持协程异步返回 - Ensure that the test runner correctly handles and awaits Promises returned from `khn.har` via `napi_create_promise` and `napi_threadsafe_function`.

</decisions>

<canonical_refs>
## Canonical References

**Downstream agents MUST read these before planning or implementing.**

### Hypium Test Integration
- `https://developer.huawei.com/consumer/en/doc/harmonyos-guides/unittest-guidelines` — HarmonyOS Automated Test Framework User Guides

### Native Metrics and Faults
- `https://developer.huawei.com/consumer/en/doc/harmonyos-guides/hidebug-guidelines-arkts` — HarmonyOS HiDebug API Documentation
- `https://developer.huawei.com/consumer/en/doc/harmonyos-references/js-apis-faultlogger` — HarmonyOS FaultLogger API

</canonical_refs>

<code_context>
## Existing Code Insights

### Reusable Assets
- `ohos/entry/src/main/ets/pages/Index.ets`: The current layout structure can be repurposed for the test console UI.
- `ohos/khn/src/main/cpp/types/libkhn/Index.d.ts`: Provides the type definitions for all available NAPI bindings to be tested.

### Established Patterns
- **ArkUI Declarative Syntax**: Building stateful views using `@State`, `Column`, `Row`, and `Scroll` components.

### Integration Points
- `ohos/entry/src/ohosTest/ets/test/`: Existing test files can be refactored or imported by the new UI test runner.

</code_context>

<specifics>
## Specific Ideas

No specific requirements — open to standard approaches

</specifics>

<deferred>
## Deferred Ideas

None — analysis stayed within phase scope

</deferred>
