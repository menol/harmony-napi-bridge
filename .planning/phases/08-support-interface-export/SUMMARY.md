---
phase: 08-support-interface-export
plan: 08
subsystem: code-generation
tags: [ksp, typescript, harmony-module, interface, generics]

# Dependency graph
requires:
  - phase: 07
    provides: []
provides:
  - Export of Kotlin `interface` (including generic interfaces) to TypeScript/ArkTS.
  - Generates `export interface ModuleName<T>` in TypeScript instead of namespaces for interfaces.
  - Skips C-interop wrapper generation and NAPI module registration for interfaces.
affects: [typescript-generator, kotlin-wrapper-generator, init-entry-generator, type-mapper]

# Tech tracking
tech-stack:
  added: []
  patterns: [generates TS interfaces for Kotlin interfaces, ignores C-interop wrapper for interfaces]

key-files:
  created: []
  modified: 
    - harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/models/Models.kt
    - harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/HarmonyNapiProcessor.kt
    - harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt
    - harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/InitEntryGenerator.kt
    - harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/TypeScriptGenerator.kt
    - harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/mapper/TypeMapper.kt

key-decisions:
  - "Skip generating C-interop wrapper functions and NAPI module registration for interfaces since they don't have static implementations."
  - "Generate `export interface ModuleName<T>` instead of `export declare namespace` for interfaces in TypeScript definitions."

patterns-established:
  - "Interface Export: KSP tracks `isInterface` and `typeParameters` on `HarmonyModuleModel` and uses them to modify generation behavior."

requirements-completed: []

# Metrics
duration: 5min
completed: 2026-04-05
---

# Phase 08 Plan 08: Support Interface Export Summary

**Export Kotlin interfaces (including generic interfaces) to TypeScript/ArkTS without breaking Kotlin Native compilation**

## Performance

- **Duration:** 5 min
- **Started:** 2026-04-05T12:20:00Z
- **Completed:** 2026-04-05T12:25:00Z
- **Tasks:** 3 completed
- **Files modified:** 7

## Accomplishments
- Updated KSP Models (`HarmonyModuleModel`, `HarmonyTypeModel`) to track `isInterface` and type parameters.
- Updated `HarmonyNapiProcessor` to extract interface kinds and type parameter declarations from `KSClassDeclaration`.
- `KotlinWrapperGenerator` and `InitEntryGenerator` now safely skip interface modules.
- `TypeScriptGenerator` correctly emits `export interface ModuleName<T> { ... }` instead of a namespace.
- Validated generic interface export via `HelloWorldPlugin` unit tests, ensuring the ArkTS side receives accurate type definitions.

## Task Commits

Each task was committed atomically:

1. **Task 1: update-models** - `c2b75c69` (feat)
2. **Task 2: update-processor** - `40a48641` (feat)
3. **Task 3: update-generators** - `03387797` (feat)
4. **Test: verify generic interface support** - `3fb0be0e` (test)

## Files Created/Modified
- `harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/models/Models.kt` - Added `isInterface` and `typeParameters` to `HarmonyModuleModel` and `isTypeParameter` to `HarmonyTypeModel`.
- `harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/HarmonyNapiProcessor.kt` - Extracted interface and type parameter properties.
- `harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt` - Skipped generating wrappers for interfaces.
- `harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/InitEntryGenerator.kt` - Filtered out interfaces from NAPI module registration.
- `harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/TypeScriptGenerator.kt` - Modified to generate TS interfaces (with generics) for Kotlin interfaces.
- `harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/mapper/TypeMapper.kt` - Returns simple name for type parameters to keep generic definitions intact.
- `sample-plugin/src/commonMain/kotlin/com/itime/harmony/sample/HelloWorldPlugin.kt` - Added `DemoInterface<T>` to verify generation.

## Decisions Made
- Skipped C++ wrapper logic for interfaces because they don't have concrete implementations to expose natively; only their TS definitions matter for ArkTS/JS interop callbacks or abstractions.
- Mapped KSP type parameters to their raw TS string representations (e.g. `T`) to preserve generics in output interfaces.

## Deviations from Plan

None - plan executed exactly as written.

## Issues Encountered
None.

## User Setup Required

None - no external service configuration required.

## Next Phase Readiness
- Interface export is complete and properly integrated into the KSP processing pipeline.
- Ready for future phases.
