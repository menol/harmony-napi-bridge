package com.itime.harmony.sample

import com.itime.harmony.napi.annotations.HarmonyExport
import com.itime.harmony.napi.annotations.HarmonyModule
import kotlinx.serialization.Serializable

@Serializable data class User(val name: String, val age: Int)
enum class Role { ADMIN, USER }

@HarmonyModule(name = "TestInterface")
interface TestInterface {
    @HarmonyExport
    fun sayHello(name: String): String
}

@HarmonyModule(name = "DemoInterface")
interface DemoInterface<T> {
    @HarmonyExport
    fun sayHello(name: T): String
}


@HarmonyModule(name = "DemoAbstract")
abstract class DemoAbstract {
    @HarmonyExport
    abstract fun process(item: List<String>): List<String>
    @HarmonyExport
    fun sayHello(): String {
        return "Hello from TestAbstract"
    }
}

@HarmonyModule(name = "TestAbstract")
abstract class TestAbstract<T> {
    @HarmonyExport
    abstract fun process(item: T): T
    @HarmonyExport
    fun sayHello(): String {
        return "Hello from TestAbstract"
    }
}


/**
 * 这是一个业务开发者的真实示例模块
 * 业务开发者只需要写这样的纯 Kotlin 代码
 */
@HarmonyModule(name = "hello_world_plugin")
object HelloWorldPlugin {

    // KSP 会自动帮我们生成接收 JS 两个 Number 并相加的方法
    @HarmonyExport
    fun add(a: Double, b: Double): Double {
        return a + b
    }

    // KSP 会自动帮我们生成接收 JS 字符串并返回字符串的方法
    @HarmonyExport
    fun greet(name: String): String {
        return "Hello $name from Kotlin Native!"
    }

    // 测试 List 转换
    @HarmonyExport
    fun processList(items: List<String>): List<String> {
        return items.map { "Processed: $it" }
    }

    // 测试 Map 转换
    @HarmonyExport
    fun processMap(data: Map<String, String>): Map<String, String> {
        return data.mapValues { "Value: ${it.value}" }
    }

    // 测试 Any 动态类型转换
    @HarmonyExport
    fun processAny(value: Any?): Any? {
        return when (value) {
            is String -> "String: $value"
            is Double -> value + 10.0
            is Int -> value + 10
            is Boolean -> !value
            is List<*> -> value.map { "Item: $it" }
            is Map<*, *> -> value.mapValues { "Mapped: ${it.value}" }
            else -> "Unknown Type"
        }
    }

    // 测试 Map<String, Any?> 动态结构
    @HarmonyExport
    fun processAnyMap(data: Map<String, Any?>): Map<String, Any?> {
        val result = mutableMapOf<String, Any?>()
        for ((key, value) in data) {
            result["$key-modified"] = when (value) {
                is String -> "$value-modified"
                is Double -> value * 2
                is Int -> value * 2
                is Boolean -> !value
                else -> value
            }
        }
        return result
    }

    // 测试具体基础类型的集合
    @HarmonyExport
    fun processIntList(items: List<Int>): List<Int> {
        return items.map { it * 2 }
    }

    @HarmonyExport
    fun processDoubleList(items: List<Double>): List<Double> {
        return items.map { it * 2.0 }
    }

    @HarmonyExport
    fun processBooleanList(items: List<Boolean>): List<Boolean> {
        return items.map { !it }
    }

    @HarmonyExport
    fun processStringIntMap(data: Map<String, Int>): Map<String, Int> {
        return data.mapValues { it.value + 10 }
    }

    @HarmonyExport
    fun processStringDoubleMap(data: Map<String, Double>): Map<String, Double> {
        return data.mapValues { it.value + 10.0 }
    }

    @HarmonyExport
    fun processStringBooleanMap(data: Map<String, Boolean>): Map<String, Boolean> {
        return data.mapValues { !it.value }
    }

    @HarmonyExport
    fun processUser(user: User, role: Role): User {
        return user.copy(name = user.name + "-" + role.name)
    }
}
