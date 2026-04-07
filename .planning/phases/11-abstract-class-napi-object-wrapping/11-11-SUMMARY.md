---
phase: 11-abstract-class-napi-object-wrapping
plan: 11
subsystem: napi-bridge
tags: [napi, kotlin-native, object-wrapping, abstract-class]

# Dependency graph
requires:
  - phase: 10-kotlin-sealed-class-support
    provides: [basic class mapping structure]
provides:
  - [Abstract class Kotlin -> JS mapping via NAPI Object Wrapping]
  - [JS Abstract class constructor and method generation]
  - [TypeMapper generic type parameter fallback to Any?]
affects: [typescript-generation, napi-runtime]

# Tech tracking
tech-stack:
  added: []
  patterns: [NAPI Object Wrapping using StableRef, Constructor registry for dynamic JS instantiation]

key-files:
  created: []
  modified:
    - harmony-napi-runtime/src/linuxArm64Main/kotlin/com.realtech/harmony/napi/runtime/utils/NapiUtils.kt
    - harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt
    - harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/InitEntryGenerator.kt
    - harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/mapper/TypeMapper.kt
    - harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/TypeScriptGenerator.kt

key-decisions:
  - "Used StableRef and napi_wrap to bridge Kotlin objects with JS instances"
  - "Created ConstructorRegistry to globally map Kotlin class names to JS constructors"
  - "Type parameters in generics fallback to toKotlinAny / toNapiValue mapping to bypass generic erasure"

patterns-established:
  - "Object Wrapping Pattern: NAPI Object Wrapping for pass-by-reference classes"

requirements-completed: []

# Metrics
duration: 15min
completed: 2026-04-05
---

# Phase 11: Abstract Class NAPI Object Wrapping Summary

**Implemented NAPI Object Wrapping for Kotlin abstract classes, allowing instances to be passed by reference to JavaScript with memory managed via StableRef.**

## Performance

- **Duration:** 15 min
- **Started:** 2026-04-05T15:42:00Z
- **Completed:** 2026-04-05T15:57:00Z
- **Tasks:** 5
- **Files modified:** 6

## Accomplishments
- Implemented `toNapiWrappedObject` and `unwrapKotlinObject` using `napi_wrap`/`napi_unwrap` and `StableRef` to pass Kotlin objects by reference.
- Added `ConstructorRegistry` to store JS class constructors created by `napi_define_class`.
- Updated KSP generators to produce JS class wrappers for Kotlin abstract classes and map methods to the instance using `napi_get_cb_info`.
- Supported generic abstract classes by falling back type parameters to `Any?` NAPI mapping methods.
- TypeScript generator now exports abstract classes instead of interfaces.

## Task Commits

Each task was committed atomically:

1. **Task 1: Add ConstructorRegistry and NapiUtils** - `ebb000ff` (feat)
2. **Task 2: Update KotlinWrapperGenerator for abstract classes** - `eda30978` (feat)
3. **Task 3: Update InitEntryGenerator with napi_define_class** - `321c6248` (feat)
4. **Task 4: Update TypeMapper for abstract class methods** - `987af06b` (feat)
5. **Task 5: Update TypeScriptGenerator for abstract classes** - `62f2cfe3` (feat)
6. **Task 6: Fix TypeMapper for generic type parameters** - `5f38c833` (fix)

## Files Created/Modified
- `NapiUtils.kt` - Added `ConstructorRegistry`, `toNapiWrappedObject` and `unwrapKotlinObject` implementations.
- `KotlinWrapperGenerator.kt` - Handled abstract class constructor and unwrapped instance method calls.
- `InitEntryGenerator.kt` - Switched to `napi_define_class` for abstract modules and populated `ConstructorRegistry`.
- `TypeMapper.kt` - Handled `unwrapKotlinObject` generation and type parameter fallbacks.
- `TypeScriptGenerator.kt` - Generated `export declare abstract class`.
- `HarmonyNapiProcessor.kt` & `Models.kt` - Added `isAbstract` flag to `HarmonyTypeModel`.

## Decisions Made
- For generic type parameters, used `toKotlinAny` and `toNapiValue` since KSP cannot retain the exact generic type during runtime for NAPI conversion.
- Bounded `unwrapKotlinObject` to `reified T : Any` to work smoothly with Kotlin Native `StableRef`.

## Deviations from Plan

### Auto-fixed Issues

**1. [Rule 3 - Blocking] Handle Generic Type Parameters**
- **Found during:** Task 4 (TypeMapper) / Validation
- **Issue:** The sample plugin contained an abstract class TestAbstract<T>. When generating the wrapper, T was treated as an unknown class, leading to compilation errors.
- **Fix:** Handled `isTypeParameter = true` in `TypeMapper.kt`, falling back to `toKotlinAny` and `toNapiValue`. Modified generic arguments in `KotlinWrapperGenerator.kt` to generate `<Any?>` for the instance unwrap cast.
- **Files modified:** TypeMapper.kt, KotlinWrapperGenerator.kt
- **Verification:** Ran `./gradlew build` successfully.
- **Committed in:** 5f38c833

---

**Total deviations:** 1 auto-fixed (1 blocking)
**Impact on plan:** Ensured generic abstract classes can compile.

## Issues Encountered
- `StableRef.fromCPointer` syntax was deprecated/invalid in this Kotlin Native version, so I auto-corrected to `asStableRef()`.
- Kotlin abstract class interfaces (like List, Map) had isAbstract true but shouldn't be NAPI-wrapped. Prioritized primitive/collection checks before isAbstract in `TypeMapper.kt`.

## User Setup Required

None - no external service configuration required.

## Next Phase Readiness
- The NAPI Object Wrapping infrastructure is complete.
- Future phases can build on `ConstructorRegistry` to support concrete classes or instantiated objects passed back and forth.

---
*Phase: 11-abstract-class-napi-object-wrapping*
*Completed: 2026-04-05*
