---
wave: 1
depends_on: []
files_modified:
  - harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/TypeScriptGenerator.kt
  - deploy_to_ohos.sh
  - ohos/khn/Index.ets
autonomous: true
---

# Phase 12: Auto-generate ArkTS Index.ets exports

## Goal
Automate the generation of an `ets` file containing the exports for ArkTS and seamlessly integrate it into the HarmonyOS project.

## Must Haves
- KSP processor must generate `ts/GeneratedExports.ets`.
- `deploy_to_ohos.sh` must copy the generated `.ets` file to `ohos/khn/src/main/ets/generated/GeneratedExports.ets`.
- `ohos/khn/Index.ets` must use the generated exports instead of manual exports.

## Tasks

<task>
  <id>1</id>
  <title>Update TypeScriptGenerator.kt to generate GeneratedExports.ets</title>
  <read_first>
    - harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/TypeScriptGenerator.kt
    - .planning/phases/12-arkts-index-auto-generate-arkts-index-ets-exports/12-RESEARCH.md
  </read_first>
  <action>
    Update `TypeScriptGenerator.kt` to collect `runtimeValues` (normal modules, abstract classes) and `typeDefinitions` (interfaces, sealed modules, user-defined pure types).
    Generate a new file `ts/GeneratedExports.ets` using KSP `CodeGenerator`.
    The content should be:
    ```typescript
    import { [runtimeValues] } from 'libkhn.so';
    import type { [typeDefinitions] } from 'libkhn.so';

    export { [runtimeValues] };
    export type { [typeDefinitions] };
    ```
    If the file already implements this (e.g. from previous manual steps), verify it exactly matches the logic.
  </action>
  <acceptance_criteria>
    - `grep "val etsContent = buildString {" harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/TypeScriptGenerator.kt` must find a match.
    - `grep "GeneratedExports" harmony-napi-ksp-processor/src/main/kotlin/com/itime/harmony/napi/ksp/generator/TypeScriptGenerator.kt` must find a match.
  </acceptance_criteria>
</task>

<task>
  <id>2</id>
  <title>Update deploy_to_ohos.sh to copy GeneratedExports.ets</title>
  <read_first>
    - deploy_to_ohos.sh
    - .planning/phases/12-arkts-index-auto-generate-arkts-index-ets-exports/12-RESEARCH.md
  </read_first>
  <action>
    Update `deploy_to_ohos.sh` to copy `GeneratedExports.ets`.
    Add the following lines under section `2.3 Copy ArkTS Exports`:
    ```bash
    mkdir -p "$OHOS_MODULE_DIR/src/main/ets/generated"
    echo "Searching for generated GeneratedExports.ets..."
    ETS_GENERATED_PATH=$(find "$KMP_MODULE_DIR/build/generated/ksp" -name "GeneratedExports.ets" | head -n 1)

    if [ -n "$ETS_GENERATED_PATH" ] && [ -f "$ETS_GENERATED_PATH" ]; then
        echo "Found ArkTS exports at: $ETS_GENERATED_PATH"
        cp "$ETS_GENERATED_PATH" "$OHOS_MODULE_DIR/src/main/ets/generated/"
    else
        echo "KSP generated GeneratedExports.ets not found yet."
    fi
    ```
    If it is already present, just ensure it's correct.
  </action>
  <acceptance_criteria>
    - `grep "GeneratedExports.ets" deploy_to_ohos.sh` must return matches.
    - `grep "cp \\\"\$ETS_GENERATED_PATH\\\" \\\"\$OHOS_MODULE_DIR/src/main/ets/generated/\\\"" deploy_to_ohos.sh` must return a match.
  </acceptance_criteria>
</task>

<task>
  <id>3</id>
  <title>Refactor ohos/khn/Index.ets</title>
  <read_first>
    - ohos/khn/Index.ets
    - .planning/phases/12-arkts-index-auto-generate-arkts-index-ets-exports/12-RESEARCH.md
  </read_first>
  <action>
    Modify `ohos/khn/Index.ets`.
    Remove manual `libkhn.so` exports.
    Ensure it contains:
    `export * from './src/main/ets/generated/GeneratedExports'`
  </action>
  <acceptance_criteria>
    - `grep "export \* from './src/main/ets/generated/GeneratedExports'" ohos/khn/Index.ets` must return a match.
  </acceptance_criteria>
</task>

<threat_model>
- **Spoofing**: N/A
- **Tampering**: N/A
- **Repudiation**: N/A
- **Information Disclosure**: N/A
- **Denial of Service**: N/A
- **Elevation of Privilege**: N/A
</threat_model>
