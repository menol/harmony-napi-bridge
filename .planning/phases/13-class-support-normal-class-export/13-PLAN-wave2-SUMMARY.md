---
phase: 13-class-support-normal-class-export
plan: wave2
subsystem: generator
tags: [ksp, napi, typescript, normal-class]

# Dependency graph
requires:
  - phase: 13-class-support-normal-class-export
    provides: [Updated HarmonyModuleModel to identify singletons and capture constructors]
provides:
  - Generated Kotlin constructor callbacks for normal classes supporting JS instantiation and existing Kotlin instance wrapping
  - Registered normal classes in NAPI via napi_define_class
  - Emitted TypeScript export declare class definitions with constructors for normal classes
affects: [arkts-integration]

# Tech tracking
tech-stack:
  added: []
  patterns: [Dual-purpose constructor wrapper using napi_external check]

key-files:
  created: []
  modified: 
    - harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt
    - harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/InitEntryGenerator.kt
    - harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/TypeScriptGenerator.kt

key-decisions:
  - "Utilized `napi_valuetype` check in the constructor wrapper to distinguish JS `new` invocations from Kotlin object passing via `napi_external`."
  - "Used `StableRef.dispose()` in the `napi_wrap` finalize callback to prevent memory leaks for Kotlin instances owned by JS."
  - "Maintained `export declare namespace` exclusively for singleton Kotlin objects."

patterns-established:
  - "Dual-purpose constructor wrapper using napi_external check: The generated constructor callback handles both JS instantiation and Kotlin object wrapping."

requirements-completed: []

# Metrics
duration: 10min
completed: 2026-04-06
---

# Phase 13 Plan Wave 2: Generators Updates Summary

**Updated KSP generators to support NAPI wrapper generation and TypeScript declarations for normal Kotlin classes**

## Performance

- **Duration:** 10 min
- **Started:** 2026-04-06T15:50:00Z
- **Completed:** 2026-04-06T16:00:00Z
- **Tasks:** 3
- **Files modified:** 3

## Accomplishments
- Implemented dual-purpose NAPI constructor wrapper generation for normal classes that handles JS instantiation (`new Class()`) and Kotlin instance wrapping (`toNapiWrappedObject`).
- Ensured Kotlin instances created from JS are properly wrapped with `napi_wrap` and their `StableRef` is correctly disposed in the `finalize` callback.
- Updated module initialization to use `napi_define_class` for normal classes instead of treating them as singletons.
- Updated TypeScript generator to output `export declare class` with constructors for normal classes, matching the ArkTS/JS runtime behavior.

## Task Commits

Each task was committed atomically:

1. **Task 13-03: Refactor KotlinWrapperGenerator for normal classes** - `0a183a1` (feat)
2. **Task 13-04: Update InitEntryGenerator for normal classes** - `08ca958` (feat)
3. **Task 13-05: Update TypeScriptGenerator for normal classes** - `04b0a6b` (feat)

## Files Created/Modified
- `harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt` - Added constructor wrapper generation logic handling JS args and `napi_external`.
- `harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/InitEntryGenerator.kt` - Switched to `napi_define_class` for normal classes.
- `harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/TypeScriptGenerator.kt` - Emitted TS classes and constructors instead of namespaces.

## Decisions Made
- Generated a `disposeStableRef` callback locally within `KotlinWrapperGenerator.kt` for use in `napi_wrap`'s finalizer, avoiding manual memory leaks.
- Avoided importing `module.className` for file-level extensions since they are not real classes.
- Abstract classes continue to throw `IllegalStateException` when JS attempts to invoke `new` on them, while still supporting the `napi_external` wrapping path.

## Deviations from Plan

None - plan executed exactly as written.

## Issues Encountered
None

## Next Phase Readiness
Generators are successfully generating NAPI wrappers and TypeScript for normal classes. The framework is ready to support full ArkTS test cases to verify these behaviors at runtime.

---
*Phase: 13-class-support-normal-class-export*
*Completed: 2026-04-06*