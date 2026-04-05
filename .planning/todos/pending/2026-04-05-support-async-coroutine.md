---
created: 2026-04-05T11:05:58.073Z
title: 支持协程异步返回
area: api
files: []
---

## Problem

在当前的 NAPI 桥接实现中，如果 Kotlin 端的方法是 `suspend` 挂起函数或者需要异步返回结果，直接的同步 C++ NAPI 包装会导致阻塞或无法正确返回值给鸿蒙 ETS 侧，导致应用无响应或崩溃。

## Solution

在 C 侧生成的胶水代码中：
1. 调用 `napi_create_promise` 为 ETS 端创建一个 Promise 对象。
2. 使用 `napi_threadsafe_function` 将回调函数包装为线程安全函数。
3. 当 Kotlin 协程/异步任务完成时，通过 `napi_call_threadsafe_function` 跨线程安全地调用 NAPI 接口，解析结果并使用 `napi_resolve_deferred`（或 `napi_reject_deferred`）完成 Promise，从而在 ETS 侧实现完美的异步调用体验。