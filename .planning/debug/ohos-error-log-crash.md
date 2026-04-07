# Debug: ohos-error-log-crash

## 症状
当启动 HarmonyOS 应用进行测试时，程序过一小段时间后无响应（ANR），随后闪退。
`ohos/error.log` 中的日志表明发生了 Application Not Responding:
`Application not responding. pid:39720, anr type:0, eventId:2365`

## 根因分析
根据提示，该问题是在添加 `MemoryLeak` 和 `Performance` 测试之后出现的。检查 `ohos/entry/src/ohosTest/ets/test/MemoryLeak.test.ets` 和 `ohos/entry/src/ohosTest/ets/test/Performance.test.ets` 发现，这两个测试用例包含了 `for (let i = 0; i < 10000; i++)` 的同步长循环。
由于 ArkTS 的测试执行跑在主线程（UI 线程）上，频繁地跨端调用 NAPI 或大规模创建 JS 侧对象完全占据了线程资源。单次同步执行时间过长，主线程无法处理 InputKeyFlow（如触摸事件）及系统渲染任务，达到了系统的 ANR 触发阈值（一般超过 5 秒），从而导致系统强制杀死应用。

## 修复方案
将原本的单次同步长循环改为“分块异步执行（Chunking）”的策略。具体做法是：
1. 将测试块 `it` 改为 `async` 函数。
2. 引入 `chunkSize = 1000` 机制，每执行 1000 次循环后，通过 `await new Promise<void>(resolve => setTimeout(resolve, 0));` 主动让出（Yield）事件循环。
3. 这样，ArkTS 引擎可以在每次分块之间处理 UI 渲染、触摸事件响应以及执行底层垃圾回收（GC），从而避免主线程被长时间阻塞而导致 ANR。
