// 因为 C++ addon (libkhn.so) 本身并不包含 TS 类型，所以我们需要手动定义或依赖生成的 d.ts
import { hello_world_plugin } from 'libkhn.so'
// 我们将所有的纯 TS 声明从 d.ts 里手动暴露出来给使用者
import type { User, Role, TestInterface, DemoInterface, DemoAbstract, TestAbstract } from 'libkhn.so'

export * from './src/main/ets/components/LogicKn'

export { hello_world_plugin }
export type { User, Role, TestInterface, DemoInterface, DemoAbstract, TestAbstract }
