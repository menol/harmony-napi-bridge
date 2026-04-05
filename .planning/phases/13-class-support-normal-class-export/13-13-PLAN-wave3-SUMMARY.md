# Phase 13 - Wave 3 Summary

In this wave, we successfully added support for exporting normal classes as Harmony modules and returning them from other exported methods.

### Key Changes
1. **Processor Updates (`harmony-napi-ksp-processor`)**:
   - Updated `TypeMapper.kt` to not throw an exception when encountering normal classes, but instead map them to `toNapiWrappedObject` (for Kotlin -> JS) and `unwrapKotlinObject` (for JS -> Kotlin).
   - Ensured `TypeMapper.getTsType` correctly returns the class name instead of `"unknown"` for normal classes.
   - Updated `KotlinWrapperGenerator.kt` to import `toNapiWrappedObject`.

2. **Sample Plugin (`sample-plugin`)**:
   - Added a `TestClass` in `HelloWorldPlugin.kt` annotated with `@HarmonyModule` and normal methods like `getValue()` and `increment()`.
   - Exposed `getTestClass()` in `HelloWorldPlugin` that returns a new instance of `TestClass`.

3. **ArkTS Tests (`ohos`)**:
   - Created `NapiWrapper.test.ets` to verify instantiating `TestClass` directly from ArkTS, calling its methods, and calling methods on an existing instance returned by Kotlin (`getTestClass()`).
   - Integrated the new test file into the `List.test.ets` test suite.

The test suite runs successfully, confirming that standard classes can be correctly exported, instantiated on both sides, and have their methods safely called across the N-API boundary.