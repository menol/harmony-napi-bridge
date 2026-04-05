---
phase: 13-class-support-normal-class-export
plan: wave1
subsystem: ksp, runtime
tags: class, object, constructor, napi, ksp

# Dependency graph
requires: []
provides:
  - Domain models capturing `isObject` and `primaryConstructorParams`
  - Refactored `toNapiWrappedObject` utilizing `napi_create_external` and `napi_new_instance`
affects:
  - 13-class-support-normal-class-export wave2 (generator updates)

# Tech tracking
tech-stack:
  added: []
  patterns: [napi_external constructor wrapper]

key-files:
  created: []
  modified:
    - harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/models/Models.kt
    - harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/HarmonyNapiProcessor.kt
    - harmony-napi-runtime/src/linuxArm64Main/kotlin/com/itime/harmony/napi/runtime/utils/NapiUtils.kt

key-decisions:
  - Extract primary constructor parameters for generating NAPI wrapper constructors.
  - Distinguish `object` declarations from normal classes in domain models.
  - Rely on NAPI external value passing and the generated JS constructor callback to perform `napi_wrap`, avoiding double wrapping.

patterns-established:
  - Use `napi_external` to pass Kotlin `StableRef` pointers to `napi_new_instance` during `toNapiWrappedObject`.

requirements-completed: []

# Metrics
duration: 10min
completed: 2026-04-06
---

# Phase 13: Support Normal Class Export (Wave 1) Summary

**Updated KSP domain models to capture normal class structures and refactored runtime NAPI utilities to support object instance creation via external constructors.**

## Performance

- **Duration:** 10 min
- **Started:** 2026-04-06T00:38:00Z
- **Completed:** 2026-04-06T00:48:00Z
- **Tasks:** 2
- **Files modified:** 3

## Accomplishments
- Domain models successfully identify whether a Kotlin declaration is an `object` or a normal class.
- Captured `primaryConstructorParams` in domain models for future wrapper generation.
- The runtime handles wrapping instances transparently by passing `napi_external` to `napi_new_instance`.

## Task Commits

Each task was committed atomically:

1. **Task 13-01: Update Domain Models to distinguish singletons and capture constructor parameters** - `c505ac8c` (feat)
2. **Task 13-02: Refactor NapiUtils for object wrapping and external constructors** - `c300136a` (refactor)

## Files Created/Modified
- `harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/models/Models.kt` - Added `isObject` and `primaryConstructorParams`.
- `harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/HarmonyNapiProcessor.kt` - Extracted `isObject` and constructor parameters.
- `harmony-napi-runtime/src/linuxArm64Main/kotlin/com/itime/harmony/napi/runtime/utils/NapiUtils.kt` - Updated `toNapiWrappedObject` to pass `napi_external`.

## Decisions Made
- Delegated the wrapping responsibility to NAPI constructor callbacks via `napi_external`, ensuring no duplicate object instantiation or wrapper conflicts.

## Deviations from Plan

None - plan executed exactly as written.

## Issues Encountered
None

## User Setup Required
None - no external service configuration required.

## Next Phase Readiness
- Ready for Wave 2: Wrapper Generation and Initialization logic for normal classes.

---
*Phase: 13-class-support-normal-class-export*
*Completed: 2026-04-06*
