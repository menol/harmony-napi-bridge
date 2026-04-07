# 方案三：终极形态 —— 在 KSP 层自动生成 ArkUI V2 包装类 (探索方向)

## 1. 背景与核心愿景

在 HarmonyOS NEXT 的演进中，系统开放了 **ArkUI V2 (Native C-API)**。这意味着开发者可以直接在 Native (C/C++) 侧创建、更新和销毁 UI 节点，从而绕过 ArkTS 与 C++ 之间频繁交互的性能瓶颈。

然而，对于 Kotlin/Native 开发者而言，直接通过 CInterop 调用 ArkUI C-API 是极其痛苦的。这需要手动编写大量繁琐、容易出错且涉及底层内存分配的胶水代码（如分配 `ArkUI_NumberValue` 数组、装配 `ArkUI_AttributeItem` 结构体、手动注册和强转 C 函数指针等）。

**核心愿景：**
借鉴本项目 `harmony-napi-bridge` 的成功经验，利用 Kotlin 注解驱动与 KSP 元编程技术，在编译期**自动生成 ArkUI 组件的生命周期管理、属性操作和事件回调的胶水代码**。最终实现对开发者屏蔽所有底层 C 内存与指针细节，赋能开发者使用**纯 Kotlin 强类型、声明式的语法**来构建鸿蒙原生高性能 UI。

---

## 2. 整体架构设计

该探索方向将以独立模块的形式平行于现有的 N-API 桥接项目，主要由以下三部分组成：

### 2.1 `harmony-arkui-annotations` (元数据标注)
提供专门针对 ArkUI 节点的注解，用于向 KSP 描述 UI 组件的结构：
- `@ArkUINode(type = "...")`: 标注一个 Kotlin 类对应底层的哪种 ArkUI 节点类型（如 `ARKUI_NODE_BUTTON`）。
- `@ArkUIProperty(id = "...")`: 标注属性，对应底层的样式或属性枚举（如 `ARKUI_NODE_ATTR_WIDTH`）。
- `@ArkUIEvent(id = "...")`: 标注事件回调，对应底层的交互事件（如 `ARKUI_NODE_EVENT_ON_CLICK`）。

### 2.2 `harmony-arkui-runtime` (底层运行时)
提供 Kotlin/Native 调用 ArkUI CInterop 的基础支撑环境：
- **API 懒加载与缓存**：封装 `OH_ArkUI_GetNativeAPI` 的获取逻辑，全局缓存 `ArkUI_NativeNodeAPI_1` 接口指针，避免频繁获取。
- **生命周期管理基类**：提供基础的 `ArkUIBaseNode`，负责管理底层 `ArkUI_NodeHandle` 句柄的创建与销毁。
- **上下文桥接**：管理 Kotlin `StableRef` 与 Native 句柄之间的绑定关系，确保在 C 函数回调时能准确还原出 Kotlin 对象。

### 2.3 `harmony-arkui-ksp-processor` (编译期生成器)
核心处理器。解析上述注解，并利用 KotlinPoet 自动生成强类型的 UI 组件代理类，自动生成 Kotlin 基础类型与 ArkUI C-API 底层结构体（如 `ArkUI_NumberValue`、`ArkUI_AttributeItem`）之间的双向转换代码。

---

## 3. 核心机制映射方案

### 3.1 节点管理与生命周期 (Node Lifecycle)
ArkUI V2 的所有操作都围绕 `ArkUI_NodeHandle` 展开。
- **生成逻辑**：当 KSP 解析到 `@ArkUINode(type = "ARKUI_NODE_BUTTON")` 时，自动在生成的包装类的主构造函数或初始化块中，调用底层的 `nativeNodeApi->createNode(ARKUI_NODE_BUTTON)` 获取节点句柄。
- **安全释放**：混入 `dispose()` 逻辑或利用 Kotlin/Native 的回收机制，在对象销毁时自动触发 `nativeNodeApi->disposeNode(handle)`，防止 C 层内存泄露。

### 3.2 属性无缝桥接 (Property Bridge)
在 C-API 中设置属性，通常需要将值打包进 `ArkUI_NumberValue` 数组，再包装成 `ArkUI_AttributeItem` 传入。
- **生成逻辑**：KSP 拦截被 `@ArkUIProperty` 修饰的变量，自动生成底层的 Getter 和 Setter 逻辑。
- **自动拆装箱**：
  - **Setter**：自动包裹 `memScoped { ... }`，将 Kotlin 的 `Float`, `Int`, `String` 等类型转换为底层对应的 C 结构体，并调用 `nativeNodeApi->setAttribute`。
  - **Getter**：调用 `nativeNodeApi->getAttribute`，从返回的 C 结构体指针中提取数据，拆箱还原为 Kotlin 的强类型并返回。

### 3.3 事件回调闭环 (Event Callback)
C-API 只能接收全局的 C 函数指针作为事件回调，无法直接接收 Kotlin 的 Lambda 闭包（带有捕获上下文）。
- **生成逻辑**：对于 `@ArkUIEvent` 标注的闭包属性，KSP 会在顶层（文件级别）自动生成对应的 CInterop 静态回调函数（`staticCFunction`）。
- **上下文透传机制**：在注册事件时，将当前 Kotlin 组件实例的 `StableRef` 转换为指针，作为 `userData` 传递给底层 `registerNodeEvent`。当事件触发时，顶层 C 函数通过解析传入的 `userData` 指针还原出真实的 Kotlin 对象实例，然后安全地调用该实例上挂载的 Lambda 闭包。

### 3.4 树形结构与布局 (Tree & Layout)
对于容器类节点（如 `ARKUI_NODE_COLUMN`、`ARKUI_NODE_ROW`），运行时基类将统一提供子节点管理方法（如 `addChild`, `removeChild`, `insertChildAt`）。包装器生成的代码将天然支持嵌套调用，为未来上层封装类似 Compose/SwiftUI 的声明式 DSL 铺平道路。

---

## 4. 开发者 API 体验预期

在这个终极形态下，业务开发者**完全不需要了解任何底层的 CInterop 细节**，只需以纯 Kotlin 的方式定义和使用 UI：

**定义组件映射 (纯 Kotlin 侧)：**
```kotlin
@ArkUINode(type = "ARKUI_NODE_BUTTON")
class HarmonyButton : ArkUIBaseNode() {

    @ArkUIProperty(id = "ARKUI_NODE_ATTR_TEXT_CONTENT")
    var text: String = ""

    @ArkUIProperty(id = "ARKUI_NODE_ATTR_FONT_COLOR")
    var fontColor: UInt = 0xFF000000u

    @ArkUIEvent(id = "ARKUI_NODE_EVENT_ON_CLICK")
    var onClick: (() -> Unit)? = null
}
```

**业务侧使用 (搭配生成的 DSL 或直接调用)：**
```kotlin
val button = HarmonyButton().apply {
    text = "Click Me"
    fontColor = 0xFFFF0000u
    onClick = {
        println("Button clicked via Native API!")
    }
}

// 将 button 挂载到父容器或 XComponent 对应的根节点上
rootColumn.addChild(button)
```

---

## 5. 里程碑与开发路线图 (Roadmap)

- **Phase 1: 基础工程与 Runtime 搭建 (基础连通)**
  - 在鸿蒙 NDK 环境中提取 ArkUI V2 的 `.def` 定义，打通 Kotlin/Native 的 CInterop。
  - 实现 `harmony-arkui-runtime`，封装 ArkUI 接口指针的全局初始化与获取逻辑。

- **Phase 2: KSP 属性生成机制实现 (状态映射)**
  - 实现 `harmony-arkui-ksp-processor`，完成对 UI 注解的 AST 解析。
  - 基于 KotlinPoet 跑通属性 Setter/Getter 的 `memScoped` 内存分配与 `ArkUI_AttributeItem` 组装的自动代码生成。

- **Phase 3: 事件与生命周期打通 (交互与内存)**
  - 攻克跨语言函数指针回调难题，利用 `StableRef` 和 `userData` 实现安全的事件闭环。
  - 完善 `ArkUIBaseNode` 的内存回收机制，确保 C 层节点句柄的安全释放。

- **Phase 4: Ohos 端到端集成验证 (真实上机)**
  - 编写示例组件，将其整合进现有的 `ohos` 测试工程。
  - 利用系统提供的 `XComponent`，将 Kotlin 生成并驱动的 ArkUI Native Node 树直接挂载到真实的鸿蒙界面中，完成端到端的渲染与交互测试验证。