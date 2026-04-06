@file:HarmonyExtensions(name = "UserUtilsV2")

package com.itime.harmony.sample

import com.itime.harmony.napi.annotations.HarmonyExport
import com.itime.harmony.napi.annotations.HarmonyModule
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

import com.itime.harmony.napi.annotations.HarmonyExtensions

@Serializable
data class User(val name: String, val age: Int) {
    @HarmonyExport
    fun getGreeting(): String {
        return "Hi, I am $name, $age years old"
    }

    @HarmonyExport
    fun sayHi(to: String): String {
        return "Hi $to, I am $name"
    }
}

@Serializable
data class Student<T>(val name: String, val age: Int,val data: T)

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

    @HarmonyExport
    fun showStudent(student: Student<String>)

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

    suspend fun showStudent(student: Student<String>) {
        // 测试复杂协程调度：多次在不同线程池间切换，并在不同时机调用 JS 回调
        
        // 1. 刚进来时还在主线程，先通过回调通知 UI 开始加载
        view?.showUser("Starting process for ${student.name}...")
        
        // 2. 切到后台线程模拟网络请求或繁重计算
        val result = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Default) {
            kotlinx.coroutines.delay(500) // 模拟耗时 0.5s
            
            // 3. 在后台线程中直接回调 JS！(验证我们刚才写的 NapiDispatcher 和 MainThreadRunner 是否真的生效)
            view?.showUser("Still computing in background...")
            
            kotlinx.coroutines.delay(500) // 继续模拟耗时 0.5s
            
            // 返回处理结果
            student.copy(name = student.name.uppercase(), age = student.age + 1)
        }
        
        // 4. withContext 结束，协程回到了主线程，最后一次调用 JS 回调
        view?.showStudent(result)
        view?.showUser("Process finished!")
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
sealed class NetworkResult

@Serializable
@SerialName("Success")
data class Success(val data: String) : NetworkResult()

@Serializable
@SerialName("Error")
data class Error(val message: String) : NetworkResult()

    @HarmonyModule(name = "DataSealed")
@Serializable
sealed class DataSealed {
    @Serializable
    @SerialName("ItemA")
    data class ItemA(val id: Int) : DataSealed()

    @Serializable
    @SerialName("ItemB")
    data class ItemB(val name: String) : DataSealed()
}

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
    fun processResult(result: NetworkResult): NetworkResult {
        return when (result) {
            is Success -> Success(result.data + " processed")
            is Error -> Error(result.message + " processed")
        }
    }

    @HarmonyExport
    suspend fun fetchDataAsync(id: String): String {
        delay(500)
        return "Data for $id"
    }

    @HarmonyExport
    suspend fun executeMultipleTasksAsync(count: Int, delayMs: Int): List<String> {
        // 使用 coroutineScope 并发执行多个耗时任务
        return coroutineScope {
            val deferreds = (1..count).map { i ->
                async(Dispatchers.Default) {
                    delay(delayMs.toLong())
                    "Task $i completed"
                }
            }
            // 等待所有任务完成，由于是并行执行，总耗时应接近单次 delayMs
            deferreds.awaitAll()
        }
    }

    @HarmonyExport
    fun getTestClass(): TestClass {
        return TestClass(42)
    }

    // --- Kotlin 特性测试 (Null Safety, Mutability, Collections) ---

    // 1. 空安全 (Null Safety)
    @HarmonyExport
    fun processNullableString(name: String?): String? {
        return name?.uppercase()
    }

    @HarmonyExport
    fun getNullableList(returnNull: Boolean): List<String>? {
        return if (returnNull) null else listOf("A", "B", "C")
    }

    // 2. 可变与不可变集合 (List vs MutableList, Map vs MutableMap)
    @HarmonyExport
    fun processMutableList(items: MutableList<String>): MutableList<String> {
        // 在 Kotlin 侧直接修改 MutableList
        items.add("Kotlin_Added")
        return items
    }

    @HarmonyExport
    fun processMutableMap(data: MutableMap<String, Int>): MutableMap<String, Int> {
        // 在 Kotlin 侧直接修改 MutableMap
        data["Kotlin_Key"] = 999
        return data
    }

    // 3. var 与 val (Data Class Mutability)
    @HarmonyExport
    fun modifyMutableData(data: MutableData): MutableData {
        // 只能修改 var，不能修改 val
        data.id += 100
        data.name = data.name + "_Modified"
        // data.readOnlyField = "Error" // 编译错误，val 不可变
        return data
    }

    // 4. 复杂嵌套集合 (Nested Collections)
    // 暂不测试泛型的深度嵌套 (如 Map<String, List<...>>) 因为 NapiUtils 目前是根据具体类型显式扩展的

    // 5. 接口与回调 (Interface & Callback)
    @HarmonyExport
    fun callTestInterface(callback: TestInterface): String {
        return "Callback returned: " + callback.sayHello("Kotlin")
    }

    // 6. 枚举与密封类 (Enums & Sealed Classes)
    @HarmonyExport
    fun processDataSealed(data: DataSealed): DataSealed {
        return when (data) {
            is DataSealed.ItemA -> DataSealed.ItemA(data.id * 10)
            is DataSealed.ItemB -> DataSealed.ItemB(data.name.uppercase())
        }
    }

    // 7. 极限测试：递归数据结构 (Recursive Data Structures)
    @HarmonyExport
    fun processTree(node: TreeNode): TreeNode {
        // 递归将所有节点的值转为大写，并反转子节点列表
        val newChildren = node.children.map { processTree(it) }.reversed()
        return TreeNode(node.value.uppercase(), newChildren)
    }

    // 8. 极限测试：多重泛型 (Extreme Generics)
    @HarmonyExport
    fun processTripleBox(box: TripleBox<String, Int, Boolean>): TripleBox<String, Int, Boolean> {
        return TripleBox(box.a.reversed(), box.b * 10, !box.c)
    }

    // 9. 极限测试：异常跨端捕获 (Exception Handling)
    @HarmonyExport
    fun throwKotlinException(message: String): String {
        throw IllegalArgumentException("Kotlin Exception: $message")
    }

    // 10. 极限测试：Any? 混合狂欢 (Extreme Any? / Dynamic)
    @HarmonyExport
    fun processExtremeAny(data: Any?): Any? {
        return when (data) {
            null -> "Was Null"
            is String -> data.reversed()
            is Int -> data * 100
            is Double -> data * 100.0
            is Boolean -> !data
            is List<*> -> data.map { processExtremeAny(it) }
            is Map<*, *> -> data.entries.associate { "Key_${it.key}" to processExtremeAny(it.value) }
            else -> "Unsupported Type: ${data::class.simpleName}"
        }
    }

    // 11. 极限测试：协程挂起异常 (Coroutine Exception & Rejection)
    @HarmonyExport
    suspend fun extremeSuspendThrow(message: String): String {
        delay(100)
        throw IllegalStateException("Suspend Exception: $message")
    }
}

@Serializable
data class TreeNode(val value: String, val children: List<TreeNode> = emptyList())

@Serializable
data class TripleBox<A, B, C>(val a: A, val b: B, val c: C)

@Serializable
data class MutableData(
    var id: Int,
    var name: String,
    val readOnlyField: String = "Immutable"
)

