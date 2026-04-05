# Phase 5: Create real ETS test page for device debugging of khn.har - Discussion Log (Assumptions Mode)

> **Audit trail only.** Do not use as input to planning, research, or execution agents.
> Decisions captured in CONTEXT.md — this log preserves the analysis.

**Date:** 2026-04-05
**Phase:** 05-create-real-ets-test-page-for-device-debugging-of-khn-har
**Mode:** assumptions
**Areas analyzed:** App Entry / UI Architecture, Test Runner Integration, Memory & Performance Metrics Display, Logging and Output Console

## Assumptions Presented

### App Entry / UI Architecture
| Assumption | Confidence | Evidence |
|------------|-----------|----------|
| The existing `Index.ets` page will be refactored into a dynamic test runner UI, replacing hardcoded method calls with a dynamically or statically registered list of test cases. | Confident | ohos/entry/src/main/ets/pages/Index.ets, ohos/khn/src/main/cpp/types/libkhn/Index.d.ts |

### Test Runner Integration
| Assumption | Confidence | Evidence |
|------------|-----------|----------|
| The test page should integrate with `@ohos/hypium` to execute structured test suites and display results directly on the device screen. | Confident | .planning/ROADMAP.md |

### Memory & Performance Metrics Display
| Assumption | Confidence | Evidence |
|------------|-----------|----------|
| The UI needs to explicitly display memory usage and performance metrics (e.g., execution time, memory diff) alongside the test results. | Confident | .planning/ROADMAP.md |

### Logging and Output Console
| Assumption | Confidence | Evidence |
|------------|-----------|----------|
| The simple logging area in the current `Index.ets` should be expanded into a dedicated, scrollable log console that can capture and format detailed native errors and standard `hilog` outputs. | Confident | ohos/entry/src/main/ets/pages/Index.ets |

## Corrections Made

No corrections — all assumptions confirmed.

## External Research

- Hypium UI Integration: `@ohos/hypium` test suites can be triggered programmatically from an ArkTS UI page using `hypium.Core.getInstance().execute()` (Source: HarmonyOS Automated Test Framework User Guides)
- Native Memory Profiling in ArkTS: HarmonyOS provides the `@ohos.hidebug` system module, which allows ArkTS applications to query native memory usage directly via `hidebug.getNativeHeapSize()` (Source: HarmonyOS HiDebug API Documentation)
- Native Crash Capture: Best practice is to use `napi_throw_error` at the NAPI boundary, and `@ohos.faultLogger` for retrieving fatal native crashes upon restart (Source: HarmonyOS FaultLogger API)
