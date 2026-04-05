# Kotlin/Native 鸿蒙 N-API 自动化桥接框架设计文档 (终极版)

## 1. 背景与目标
在鸿蒙系统（HarmonyOS）跨平台开发中，ArkTS/JS 与底层的交互依赖 Node-API（N-API）。传统方案需要开发者手动编写大量 C/C++ 胶水代码，并手动维护对应的 `.d.ts` 声明文件，存在开发效率低、极易内存泄漏、类型极易脱节等痛点。

**本框架目标**：
利用 Kotlin/Native 的 CInterop 能力与 KSP（Kotlin Symbol Processing）元编程技术，**彻底消灭 C/C++ 胶水代码，并实现接口类型的单点真实（Single Source of Truth）**。开发者只需编写纯 Kotlin 代码，编译期即可自动生成：
1. 底层所有的 N-API 类型映射与内存管理代码。
2. 供鸿蒙 ArkTS 直接消费的、与 Kotlin/JS 产物对齐的强类型 `.d.ts` 声明文件。

## 2. 整体架构设计
框架从上至下分为四个核心模块：
1. **`harmony-napi-annotations`**：提供 `@HarmonyModule`, `@HarmonyExport` 等注解声明。
2. **`harmony-napi-runtime`**：提供基础的类型转换工具类、线程安全包装器及 N-API 基础扩展。
3. **`harmony-napi-ksp-processor`**：编译期核心，负责双路输出：
   - **底层产物**：基于 KotlinPoet 生成 `xxx_Napi_Wrapper.kt` 胶水代码。
   - **前端产物**：解析 AST 并生成对应的 `xxx.d.ts` TypeScript 声明文件。
4. **`harmony-napi-gradle-plugin`**：工程化插件，负责处理毕昇编译器工具链冲突，并将 `.d.ts` 文件自动拷贝至鸿蒙工程的 `types` 目录下。

## 3. 底层类型映射与内存模型 (Kotlin <-> N-API)
框架内部实现一套强大的**类型路由器（Type Router）**，负责 Kotlin 与 JS 运行时的双向映射：

### 3.1 基础与值语义类型 (Value Semantics)
数据在跨语言边界时发生**深拷贝**。
* **基础类型**：直接对应 `napi_get_value_xxx` 与 `napi_create_xxx`。
* **Value Class (内联类)**：编译期解包，提取底层基础类型进行无损转换，零对象分配开销。
* **Enum (枚举)**：按 `name` (String) 或 `ordinal` (Int) 序列化与反序列化。
* **Data Class (数据类)**：递归拆解主构造函数，将属性逐一映射为 JS Object 的键值对。

### 3.2 集合与字典类型 (Collections)
由于 N-API 没有直接的高级数据结构，JS Array 和 JS Object 必须通过 C 函数手动遍历和装箱/拆箱。
* **`List<T>` / `Array<T>`**：KSP 生成循环，通过 `napi_set_element` 和 `napi_get_element` 映射为 **JS Array**。
* **`Map<K, V>`**：KSP 生成代码，通过 `napi_set_named_property` 和 `napi_get_property_names` 映射为 **JS Object (Record)**。
* **`Set<T>`**：跨语言边界时，统一降级并转换为 JS Array 传递。

### 3.3 引用与行为语义 (Reference & Callback)
* **对象包装 (`napi_wrap`)**：Kotlin 实例通过 `StableRef` 转换为不透明指针，注入 JS Object。绑定 N-API `finalize` 回调，跟随 ArkTS 的 GC 自动释放 Kotlin 对象，杜绝内存泄漏。
* **接口回调 (Interface)**：针对 JS 传入的 Callback，KSP 自动生成 Kotlin 匿名内部类并持有 `napi_ref` 全局引用，通过 `napi_call_function` 实现反向跨语言调用。

## 4. 前端类型定义生成 (.d.ts Generation)
为了给鸿蒙 ArkTS 提供完美的开发体验，KSP 插件在生成胶水代码的同时，会严格按照 Kotlin/JS 的规范输出 `.d.ts` 声明文件，确保前后端类型 100% 对应。

### 4.1 类型映射表
| Kotlin 语法 | 生成的 TypeScript 语法 (`.d.ts`) |
| :--- | :--- |
| `Int`, `Double`, `Float` | `number` |
| `String`, `Boolean` | `string`, `boolean` |
| `List<T>`, `Array<T>` | `Array<T>` (或 `T[]`) |
| `Map<K, V>` | `Record<K, V>` |
| `data class User(val id: Int)` | `export interface User { id: number; }` |
| `enum class Status { OK }` | `export enum Status { OK = "OK" }` |
| `suspend fun fetch(): String`| `fetch(): Promise<string>;` |
| `fun setListener(l: (Int)->Unit)` | `setListener(l: (p0: number) => void): void;` |

### 4.2 可选链与空安全 (Null Safety)
* Kotlin 中的可空类型（如 `String?`）在生成 `.d.ts` 时，自动转换为 TS 的可选属性或联合类型（如 `?: string` 或 `string | undefined`），强制前端进行判空处理。

## 5. 进阶核心特性

* **协程与 Promise 自动桥接**：KSP 自动将 `suspend` 函数包装为 `napi_create_promise`，并在协程恢复时自动决议 Promise。
* **严格的线程安全护航**：所有后台协程/线程向 JS 的回调，均自动包裹 `napi_threadsafe_function`，将任务投递回 ArkTS 主线程 Event Loop，彻底杜绝多线程 Crash。
* **全局异常拦截兜底**：KSP 为所有暴露函数包裹 `try-catch`，发生 Kotlin 异常时提取堆栈，通过 `napi_throw_error` 转化为标准 JS Error 抛给 ArkTS。

## 6. 开发者接入体验 (User API)

业务开发者只需正常编写 Kotlin 代码，剩下的全交由框架处理：

```kotlin
// --- 纯 Kotlin 业务侧代码 ---
@HarmonyModule(name = "HardwarePlugin")
object HardwarePlugin {

    @HarmonyExport
    fun getDeviceInfo(id: String): DeviceInfo { 
        return DeviceInfo(id = id, version = "1.0.0")
    }

    @HarmonyExport
    suspend fun downloadFirmware(url: String): Boolean {
        return withContext(Dispatchers.IO) { true } // 耗时任务
    }
}

data class DeviceInfo(val id: String, val version: String?)
```

**编译后，前端（鸿蒙）自动获得的产物**：
```typescript
// --- 自动生成的 index.d.ts ---
export interface DeviceInfo {
    id: string;
    version?: string; // 自动处理 Kotlin 的空安全
}

export declare namespace HardwarePlugin {
    function getDeviceInfo(id: string): DeviceInfo;
    // suspend 自动转换为 Promise
    function downloadFirmware(url: string): Promise<boolean>; 
}
```

## 7. 构建与发布流水线
1. **自动清理配置**：Gradle 插件自动注入适配鸿蒙毕昇编译器的构建参数（剔除 `libgcc_s`）。
2. **KSP 双路生成**：编译期间同步输出 Kotlin/Native 胶水代码与 `.d.ts` 文件。
3. **产物组装分发**：打包任务自动将生成的 `libHardwarePlugin.so` 与 `index.d.ts` 组装为一个标准的 HarmonyOS Native 模块结构（包含 `oh-package.json5`），可直接作为鸿蒙本地依赖或发布至 OHPM 私有仓。
