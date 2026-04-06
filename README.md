# Harmony NAPI Bridge

**Harmony NAPI Bridge** 是一个基于 Kotlin/Native 和 KSP (Kotlin Symbol Processing) 技术的自动化跨平台桥接框架。

本项目旨在**彻底消除 Kotlin/Native 与 HarmonyOS (鸿蒙) 之间的 C/C++ 胶水代码**。通过本框架，开发者只需编写纯 Kotlin 代码，编译期即可自动生成所有底层的 N-API (Node-API) 交互代码，并同步生成供 ArkTS/JS 直接调用的强类型 `.d.ts` 声明文件，实现接口类型的“单点真实 (Single Source of Truth)”。

---

## ✨ 核心特性

- **🚀 零 C/C++ 胶水代码**：通过 KSP 元编程，在编译期自动生成所有的 N-API 类型映射与内存管理代码。
- **📦 强类型 `.d.ts` 自动生成**：精准映射 Kotlin 的基础类型、数据类、枚举及集合类型，完美保留 Kotlin 的空安全（Null Safety）特性。
- **⚡️ 协程与 Promise 无缝桥接**：自动将 Kotlin `suspend` 函数转换为 ArkTS 中的 `Promise`，轻松处理异步操作。
- **🛡 严格的线程安全**：通过 `napi_threadsafe_function` 自动将后台协程或子线程的回调安全地投递回 ArkTS 主线程 Event Loop，杜绝多线程 Crash。
- **♻️ 智能内存管理**：自动绑定 N-API `finalize` 回调，使 Kotlin 对象的生命周期跟随 ArkTS 的 GC 自动释放，防止内存泄漏。
- **⚠️ 全局异常拦截**：自动捕获 Kotlin 侧抛出的异常，并将其转化为标准的 JS Error 抛给 ArkTS 层。

---

## 🏗 项目架构

框架自顶向下由以下几个核心模块构成：

1. **`harmony-napi-annotations`**  
   提供 `@HarmonyModule`, `@HarmonyExport` 等核心注解，用于标记需要导出给 ArkTS 使用的 Kotlin 类和函数。
   
2. **`harmony-napi-runtime`**  
   运行时核心库。提供基础的类型转换工具类、线程安全包装器以及对 CInterop 的 N-API 基础扩展。

3. **`harmony-napi-ksp-processor`**  
   编译期处理核心，负责双路输出：
   - **底层产物**：基于 KotlinPoet 自动生成 Kotlin/Native 与 N-API 交互的 `xxx_Napi_Wrapper.kt` 胶水代码。
   - **前端产物**：解析 AST，自动生成对应的 `xxx.d.ts` TypeScript 声明文件。

4. **`ohos`**  
   鸿蒙侧的集成测试与演示模块。包含 `entry` 和 `khn` 模块，用于验证生成的 N-API 动态库（`.so`）与 ArkTS 侧的真实交互。

---

## 💻 接入示例

业务开发者只需关注纯 Kotlin 代码逻辑，添加注解即可完成跨语言暴露：

### 1. 编写 Kotlin 代码
```kotlin
@HarmonyModule(name = "HardwarePlugin")
object HardwarePlugin {

    @HarmonyExport
    fun getDeviceInfo(id: String): DeviceInfo { 
        return DeviceInfo(id = id, version = "1.0.0")
    }

    @HarmonyExport
    suspend fun downloadFirmware(url: String): Boolean {
        return withContext(Dispatchers.IO) { true } // 自动桥接为 Promise
    }
}

data class DeviceInfo(val id: String, val version: String?)
```

### 2. 自动生成的 ArkTS 侧调用接口 (`.d.ts`)
编译后，鸿蒙侧会自动获得如下类型安全的声明文件，可以直接在 ArkTS 中导入使用：
```typescript
export interface DeviceInfo {
    id: string;
    version?: string; // 自动处理 Kotlin 的空安全 (?)
}

export declare namespace HardwarePlugin {
    function getDeviceInfo(id: string): DeviceInfo;
    function downloadFirmware(url: string): Promise<boolean>; 
}
```

---

## 🛠 开发与构建指南

### 环境依赖
- **JDK 17+**
- **Kotlin 1.9.24+**
- **HarmonyOS SDK / DevEco Studio** (用于构建和测试 `ohos` 模块)
- **CMake & Ninja** (鸿蒙 NDK 构建工具链)

### 常用构建指令

**编译 Kotlin/Native 产物及 KSP 生成代码：**
```bash
./gradlew build
```

**部署与测试：**
可以使用项目提供的快捷脚本，将编译好的动态库及声明文件同步至鸿蒙工程并触发测试：
```bash
./deploy_to_ohos.sh
```

---

## 📅 路线图与当前状态

当前项目正处于**核心特性验证与 ohos 模块单元测试集成**阶段：

- [x] KSP 插件基础架构搭建
- [x] 基础类型与 N-API 的映射 (生成 C++ 胶水代码)
- [x] 支持基础 Data Class 的导出与映射
- [x] Gradle 构建逻辑与 Hvigor 构建系统的打通
- [ ] Kotlin 协程 (suspend) 与 JS Promise 的打通验证
- [ ] 自动化测试用例覆盖 (在 `ohos` 模块中执行真实的 N-API 调用验证)

详情请参阅项目内的 `.planning/PROJECT.md` 与 `DESIGN.md`。
