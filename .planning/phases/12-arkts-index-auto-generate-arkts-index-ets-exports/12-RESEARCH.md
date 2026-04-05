# Phase 12: Auto-generate ArkTS Index.ets exports - Research

## Context and Goal
Currently, the Kotlin Symbol Processor (KSP) generates TypeScript definitions (`Index.d.ts`) for all `@HarmonyExport` annotated classes, interfaces, abstract classes, sealed classes, and their related types. These are packaged into `libkhn.so`. However, ArkTS modules require these to be explicitly exported from an `.ets` file (e.g., `ohos/khn/Index.ets`) for developers to use them.

This manual export process in `Index.ets` is error-prone and tedious:
```typescript
import { hello_world_plugin, DemoAbstract, TestAbstract } from 'libkhn.so'
import type { User, Role, TestInterface, DemoInterface, NetworkResult, BasePageState, PageState, TestSealed } from 'libkhn.so'

export { hello_world_plugin, DemoAbstract, TestAbstract }
export type { User, Role, TestInterface, DemoInterface, NetworkResult, BasePageState, PageState, TestSealed }
```
The goal of this phase is to automate the generation of an `ets` file containing these exports and seamlessly integrate it into the HarmonyOS project.

## Generator Implementation
We will modify `TypeScriptGenerator.kt` (or create an `ArkTSIndexGenerator.kt`) to output an additional file: `ts/GeneratedExports.ets`.

This file will contain automatic imports from `libkhn.so` (or a configured library name) and re-export them.
The types and values can be categorized as follows:
- **Runtime Values (`import { ... }`)**:
  - Normal modules (`!module.isInterface && !module.isSealed`)
  - Abstract classes (`module.isAbstract`) - because they can have constructors/static methods and their class definition exists at runtime.
- **Type Definitions (`import type { ... }`)**:
  - Interfaces (`module.isInterface`)
  - Sealed module interfaces (`module.isSealed`)
  - User-defined pure types: Enums (`isEnum`), Serializable Data Classes (`isSerializable`), Serializable Sealed Classes (`isSerializable && isSealed`). Primitive and standard types (Int, String, List, Map) are skipped.

We can support an optional KSP argument `harmony.napi.libName` (default: `"libkhn.so"`) so the generator knows which native library to import from.

## Deployment Script Update
The generated `ts/GeneratedExports.ets` will be placed in the KSP output directory `build/generated/ksp/.../ts/GeneratedExports.ets`.
We need to update the `deploy_to_ohos.sh` script to copy this file to the HarmonyOS module, e.g., `ohos/khn/src/main/ets/generated/GeneratedExports.ets`.

Then, developers only need to add one line to their main `ohos/khn/Index.ets`:
```typescript
export * from './src/main/ets/generated/GeneratedExports'
```
This entirely removes the manual bookkeeping of types and values.

## Steps to Implement (PLAN)
1. **Update KSP Processor**:
   - In `TypeScriptGenerator.kt`, collect `runtimeValues` and `typeDefinitions`.
   - Read library name from KSP options (fallback to `"libkhn.so"`).
   - Generate `ts/GeneratedExports.ets` with the correct `import` and `export` statements.
2. **Update Deployment Script**:
   - In `deploy_to_ohos.sh`, locate `GeneratedExports.ets` alongside `Index.d.ts`.
   - Copy it to `ohos/khn/src/main/ets/generated/GeneratedExports.ets` (create the directory if it doesn't exist).
3. **Refactor existing code**:
   - Remove manual imports/exports in `ohos/khn/Index.ets`.
   - Replace them with `export * from './src/main/ets/generated/GeneratedExports'`.
4. **Verification**:
   - Run the KSP processor via `./deploy_to_ohos.sh`.
   - Verify `ohos/khn/src/main/ets/generated/GeneratedExports.ets` is correctly generated and placed.
   - Run tests (`hvigorw test`) to ensure everything still compiles and executes correctly.

