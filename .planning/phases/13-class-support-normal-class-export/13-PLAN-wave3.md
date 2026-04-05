---
wave: 3
depends_on: ["13-03", "13-04", "13-05"]
files_modified:
  - "sample-plugin/src/linuxArm64Main/kotlin/com/itime/harmony/napi/plugin/HelloWorldPlugin.kt"
  - "ohos/entry/src/ohosTest/ets/test/NapiWrapper.test.ets"
autonomous: true
---

# Phase 13 Plan: Validation

## Goal
Validate the end-to-end functionality of normal class export by creating a normal class in the plugin, instantiating it from ArkTS, calling its methods, and receiving instances from Kotlin.

## Requirements
- TBD

## Must Haves
- End-to-end test correctly compiles and executes in HarmonyOS test environment.
- No memory leaks or double wrapping occurs when exchanging objects between JS and Kotlin.

## Verification Criteria
- `hvigorw --sync -p product=default test` executes tests correctly.
- Test instances instantiated in JS and Kotlin behave consistently.

## Tasks

### Wave 3: Validation

<task>
  <id>13-06</id>
  <description>Create test cases for normal classes</description>
  <read_first>
    - sample-plugin/src/linuxArm64Main/kotlin/com/itime/harmony/napi/plugin/HelloWorldPlugin.kt
    - ohos/entry/src/ohosTest/ets/test/NapiWrapper.test.ets
  </read_first>
  <action>
    Add a normal class `TestClass` in `HelloWorldPlugin.kt` annotated with `@HarmonyModule(name = "TestClass")`. Give it a primary constructor `value: Int` and methods `getValue(): Int` and `increment()`.
    Add a method in `HelloWorldPlugin` singleton that returns a `TestClass` instance.
    Update `NapiWrapper.test.ets` to test:
    1. Instantiating `TestClass` from ArkTS (`const tc = new TestClass(10)`).
    2. Calling its instance methods (`tc.getValue()`, `tc.increment()`).
    3. Getting an existing instance from Kotlin (`HelloWorldPlugin.getTestClass()`) and verifying its methods work.
  </action>
  <acceptance_criteria>
    - `grep -q "class TestClass" sample-plugin/src/linuxArm64Main/kotlin/com/itime/harmony/napi/plugin/HelloWorldPlugin.kt`
    - `grep -q "new TestClass" ohos/entry/src/ohosTest/ets/test/NapiWrapper.test.ets`
    - The `ohosTest` test suite passes.
  </acceptance_criteria>
</task>

<threat_model>
  - Block on: high
  - Threats:
    - **Memory Leaks**: `StableRef.dispose()` is crucial in the constructor `napi_wrap` finalize callback to prevent memory leaks.
    - **Wrapper Conflicts**: Double wrapping could crash the JS runtime if `napi_external` logic fails.
</threat_model>
