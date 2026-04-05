@file:HarmonyExtensions(name = "UserUtilsV2")

package com.itime.harmony.sample

import com.itime.harmony.napi.annotations.HarmonyExport
import com.itime.harmony.napi.annotations.HarmonyModule
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

import com.itime.harmony.napi.annotations.HarmonyExtensions

@Serializable data class User(val name: String, val age: Int) {
    @HarmonyExport
    fun getGreeting(): String {
        return "Hi, I am $name, $age years old"
    }

    @HarmonyExport
    fun sayHi(to: String): String {
        return "Hi $to, I am $name"
    }
}

@HarmonyExport
fun User.getFullName(): String {
    return "${this.name} (${this.age})"
}
enum class Role { ADMIN, USER }

@HarmonyModule(name = "BasePageState")
interface BasePageState {

}

@HarmonyModule(name = "BaseView")
interface BaseView {

}

@HarmonyModule(name = "IndexView")
interface IndexView: BaseView {

    @HarmonyExport
    fun showUser(name: String)

}


@HarmonyModule(name = "IndexPresenter")
class IndexPresenter {

    private var view: IndexView? = null

    fun attach(view: IndexView) {
        this.view = view
    }

    fun showUser(user: User) {
        view?.showUser(user.getFullName())
    }

    fun detach() {
        this.view = null
    }

}


@HarmonyModule(name = "PageState")
@Serializable
sealed class PageState : BasePageState {
    @Serializable
    @SerialName("Loading")
    data class Loading(val isRefreshing: Boolean) : PageState()

    @Serializable
    @SerialName("Success")
    data class Success<T>(val data: T) : PageState()

    @Serializable
    @SerialName("Error")
    data class Error(val message: String) : PageState()
}

@Serializable
sealed class NetworkResult<T>

@Serializable
@SerialName("Success")
data class Success<T>(val data: T) : NetworkResult<T>()

@Serializable
@SerialName("Error")
data class Error<T>(val message: String) : NetworkResult<T>()

@HarmonyModule(name = "TestSealed")
sealed class TestSealed<T> {
    @HarmonyExport
    abstract fun process(item: T): T
}

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
@HarmonyModule(name = "TestClass")
class TestClass(private var value: Int) {
    @HarmonyExport
    fun fetchValue(): Int {
        return value
    }

    @HarmonyExport
    fun increment() {
        value++
    }
}

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

    @HarmonyExport
    fun processResult(result: NetworkResult<String>): NetworkResult<String> {
        return when (result) {
            is Success -> Success(result.data + " processed")
            is Error -> Error(result.message + " processed")
        }
    }

    @HarmonyExport
    fun getTestClass(): TestClass {
        return TestClass(42)
    }
}
