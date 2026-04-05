# Testing Patterns

This document outlines the testing strategy, frameworks, and patterns used in the `harmony-napi-bridge` project.

## 1. Frameworks & Environments

The project spans Kotlin Multiplatform (KMP) and OpenHarmony (ArkTS). Testing is currently focused primarily on the OpenHarmony integration side.

- **OpenHarmony (ArkTS) Testing**: Uses the `@ohos/hypium` testing framework.
- **Kotlin Multiplatform Testing**: *(To Be Expanded)* Shared logic in `commonMain` should be tested using `kotlin.test`. Currently, the logic is bridged directly, so testing occurs at the integration level on the OpenHarmony side.

## 2. Directory Structure

Testing in the OpenHarmony module (`ohos/khn/` and `ohos/entry/`) is divided into two main categories:

- **Local Unit Tests (`src/test/`)**:
  - Contains tests that run locally on the host machine without needing an emulator or physical device.
  - Used for pure ArkTS/TypeScript logic that does not depend on native N-API modules or device-specific APIs.
  - Example: `ohos/khn/src/test/LocalUnit.test.ets`.

- **Instrumented Tests (`src/ohosTest/`)**:
  - Contains tests that must be executed on an OpenHarmony emulator or physical device.
  - Required for testing the actual KMP N-API bridge (`libkhn.so`), as the `.so` binaries (e.g., `linuxArm64`) must be loaded in the target environment.
  - Example: `ohos/khn/src/ohosTest/ets/test/Ability.test.ets`.

## 3. Writing Tests in ArkTS

When writing tests using `@ohos/hypium`, adhere to the standard BDD (Behavior-Driven Development) structure:

- **`describe`**: Group related tests (e.g., a specific `@HarmonyModule`).
- **`beforeAll` / `afterAll`**: Setup and teardown logic that runs once per suite (e.g., initializing mock data or setting up the N-API environment).
- **`beforeEach` / `afterEach`**: Setup and teardown logic that runs before/after every individual `it` block.
- **`it`**: Individual test cases. The `it` function accepts a name, a filter parameter (usually `0`), and the test closure.

**Example**:
```typescript
import { describe, it, expect } from '@ohos/hypium';
import { HelloWorldPlugin } from 'libkhn.so'; // Generated KSP bridge

export default function pluginTest() {
  describe('HelloWorldPluginTest', () => {
    it('should add two numbers correctly', 0, () => {
      let result = HelloWorldPlugin.add(5.0, 3.5);
      expect(result).assertEqual(8.5);
    });
  });
}
```

## 4. Mocking & Coverage

- **Mocking**: For complex device APIs, use OpenHarmony's built-in mocking capabilities or standard TypeScript mocking patterns. For N-API tests, avoid mocking the bridge itself; instead, test the actual `.so` integration in `ohosTest`.
- **Coverage**: Test coverage for ArkTS code can be collected via DevEco Studio's test runner. Ensure that all exported Kotlin functions (via `@HarmonyExport`) have corresponding test cases in the `ohosTest` directory to verify parameter passing and return value conversions.
