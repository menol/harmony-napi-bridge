#!/bin/bash
set -e

# Define paths
PROJECT_ROOT=$(pwd)
KMP_MODULE_DIR="$PROJECT_ROOT/sample-plugin"
OHOS_MODULE_DIR="$PROJECT_ROOT/ohos/khn"

# Determine gradle command
if [ -f "$PROJECT_ROOT/gradlew" ]; then
    GRADLE_CMD="$PROJECT_ROOT/gradlew"
elif [ -f "$PROJECT_ROOT/../platform/ffi/kmp/gradlew" ]; then
    GRADLE_CMD="$PROJECT_ROOT/../platform/ffi/kmp/gradlew"
elif [ -f "$PROJECT_ROOT/../platform/ffi/kmp/gradle-8.5/bin/gradle" ]; then
    GRADLE_CMD="$PROJECT_ROOT/../platform/ffi/kmp/gradle-8.5/bin/gradle"
else
    GRADLE_CMD="gradle"
fi

# 1. Build KMP Shared Module
echo "Building KMP sample-plugin Module with $GRADLE_CMD..."
$GRADLE_CMD :sample-plugin:linkReleaseSharedLinuxArm64

# 2. Copy Artifacts
echo "Copying Artifacts to HarmonyOS Module..."

# 2.1 Copy .so and .h
# 清理旧的 so 和头文件，防止旧项目残留的 libkn.so 等文件被一起打包
rm -rf "$OHOS_MODULE_DIR/libs/arm64-v8a/"
mkdir -p "$OHOS_MODULE_DIR/libs/arm64-v8a/include"

# 注意：现在 sample-plugin 产出的是 libsample_plugin.so
cp "$KMP_MODULE_DIR/build/bin/linuxArm64/releaseShared/libsample_plugin.so" "$OHOS_MODULE_DIR/libs/arm64-v8a/"
cp "$KMP_MODULE_DIR/build/bin/linuxArm64/releaseShared/libsample_plugin_api.h" "$OHOS_MODULE_DIR/libs/arm64-v8a/include/"

# 2.2 Copy TypeScript Definitions
mkdir -p "$OHOS_MODULE_DIR/src/main/cpp/types/libkhn"
echo "Searching for generated Index.d.ts..."
TS_GENERATED_PATH=$(find "$KMP_MODULE_DIR/build/generated/ksp" -name "Index.d.ts" | head -n 1)

if [ -n "$TS_GENERATED_PATH" ] && [ -f "$TS_GENERATED_PATH" ]; then
    echo "Found TS definitions at: $TS_GENERATED_PATH"
    cp "$TS_GENERATED_PATH" "$OHOS_MODULE_DIR/src/main/cpp/types/libkhn/"
else
    echo "KSP generated Index.d.ts not found yet. Creating a placeholder."
    echo "export const add: (a: number, b: number) => number;" > "$OHOS_MODULE_DIR/src/main/cpp/types/libkhn/Index.d.ts"
    echo "export const greet: (name: string) => string;" >> "$OHOS_MODULE_DIR/src/main/cpp/types/libkhn/Index.d.ts"
fi

# 2.3 Copy ArkTS Exports (GeneratedExports.ets)
mkdir -p "$OHOS_MODULE_DIR/src/main/ets/generated"
echo "Searching for generated GeneratedExports.ets..."
ETS_GENERATED_PATH=$(find "$KMP_MODULE_DIR/build/generated/ksp" -name "GeneratedExports.ets" | head -n 1)

if [ -n "$ETS_GENERATED_PATH" ] && [ -f "$ETS_GENERATED_PATH" ]; then
    echo "Found ArkTS exports at: $ETS_GENERATED_PATH"
    cp "$ETS_GENERATED_PATH" "$OHOS_MODULE_DIR/src/main/ets/generated/"
else
    echo "KSP generated GeneratedExports.ets not found yet."
fi

echo "Artifacts copied successfully."

# 3. Build HarmonyOS HAR
echo "Building HarmonyOS HAR..."
cd "$PROJECT_ROOT/ohos"

# Check if hvigorw exists locally, otherwise assume it's in PATH
if [ -f "./hvigorw" ]; then
    chmod +x ./hvigorw
    ./hvigorw --mode module -p module=khn assembleHar
else
    hvigorw --mode module -p module=khn assembleHar
fi

echo "Done! HAR file generated."
