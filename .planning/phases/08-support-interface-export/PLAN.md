---
wave: 1
depends_on: []
files_modified:
  - harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/models/Models.kt
  - harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/HarmonyNapiProcessor.kt
  - harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt
  - harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/InitEntryGenerator.kt
  - harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/TypeScriptGenerator.kt
  - harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/mapper/TypeMapper.kt
autonomous: true
requirements: []
---

# Phase 08: Support Interface Export

## Goal
Support applying `@HarmonyModule` to a Kotlin `interface` (including generic interfaces) so that they are exported correctly to the TypeScript/ArkTS side, and do not break the existing Kotlin Native compilation.

## Must Haves
- `HarmonyModuleModel` and `HarmonyTypeModel` must track if a class is an interface and if it has type parameters.
- Code Generators must skip generating C-interop wrapper functions and NAPI module registration for interfaces.
- `TypeScriptGenerator` must generate `export interface ModuleName<T> { ... }` instead of a namespace for interfaces.
- `TypeMapper` must return the simple name for type parameters.

## Tasks

<tasks>
  <task id="update-models" wave="1">
    <description>Update KSP Models to support interface tracking</description>
    <read_first>
      <file>harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/models/Models.kt</file>
    </read_first>
    <action>
      In `Models.kt`:
      1. Update `HarmonyModuleModel` to add `val isInterface: Boolean = false` and `val typeParameters: List<String> = emptyList()`.
      2. Update `HarmonyTypeModel` to add `val isTypeParameter: Boolean = false`.
    </action>
    <acceptance_criteria>
      - `grep -q "val isInterface: Boolean" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/models/Models.kt` returns 0
      - `grep -q "val typeParameters: List<String>" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/models/Models.kt` returns 0
      - `grep -q "val isTypeParameter: Boolean" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/models/Models.kt` returns 0
    </acceptance_criteria>
  </task>

  <task id="update-processor" wave="2" depends_on="update-models">
    <description>Update KSP Processor to detect interfaces and type parameters</description>
    <read_first>
      <file>harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/HarmonyNapiProcessor.kt</file>
    </read_first>
    <action>
      In `HarmonyNapiProcessor.kt`:
      1. In `resolveType`, check if `isTypeParameter = resolved.declaration is KSTypeParameter` and pass it to `HarmonyTypeModel`.
      2. In `process`, check if `isInterface = classDecl.classKind == ClassKind.INTERFACE`.
      3. In `process`, extract `typeParameters = classDecl.typeParameters.map { it.name.asString() }`.
      4. Pass `isInterface` and `typeParameters` to `HarmonyModuleModel`.
    </action>
    <acceptance_criteria>
      - `grep -q "isTypeParameter = resolved.declaration is KSTypeParameter" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/HarmonyNapiProcessor.kt` returns 0
      - `grep -q "isInterface = classDecl.classKind == ClassKind.INTERFACE" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/HarmonyNapiProcessor.kt` returns 0
      - `grep -q "typeParameters =" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/HarmonyNapiProcessor.kt` returns 0
    </acceptance_criteria>
  </task>

  <task id="update-generators" wave="3" depends_on="update-processor">
    <description>Update generators to handle interfaces correctly</description>
    <read_first>
      <file>harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt</file>
      <file>harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/InitEntryGenerator.kt</file>
      <file>harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/TypeScriptGenerator.kt</file>
      <file>harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/mapper/TypeMapper.kt</file>
    </read_first>
    <action>
      1. In `KotlinWrapperGenerator.kt`, add `if (module.isInterface) return` at the beginning of `generate`.
      2. In `InitEntryGenerator.kt`, filter out interfaces: `val validModules = modules.filter { !it.isInterface }` and use `validModules`.
      3. In `TypeScriptGenerator.kt`, if `module.isInterface` is true, generate `export interface ModuleName<T1, T2> { ... }` instead of a namespace.
      4. In `TypeMapper.kt`, inside `getTsType`, add `if (typeModel.isTypeParameter) return typeModel.simpleName`.
    </action>
    <acceptance_criteria>
      - `grep -q "if (module.isInterface) return" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/KotlinWrapperGenerator.kt` returns 0
      - `grep -q "modules.filter { \!it.isInterface }" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/InitEntryGenerator.kt` returns 0
      - `grep -q "export interface \${module.moduleName}" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/generator/TypeScriptGenerator.kt` returns 0
      - `grep -q "if (typeModel.isTypeParameter) return typeModel.simpleName" harmony-napi-ksp-processor/src/main/kotlin/com.realtech/harmony/napi/ksp/mapper/TypeMapper.kt` returns 0
    </acceptance_criteria>
  </task>
</tasks>

## Verification
- Verify that `HelloWorldPlugin` with `DemoInterface<T>` generates the correct TypeScript interface in `Index.d.ts`.
- Run the bash script `./deploy_to_ohos.sh` to confirm the Kotlin Native plugin compiles and TypeScript generates without breaking existing NAPI features.
