package com.itime.harmony.sample

import com.itime.harmony.napi.annotations.HarmonyExport
import com.itime.harmony.napi.annotations.HarmonyModule
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.util.date.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable

// 定义一个跨端可序列化的请求对象
@Serializable
data class ArkTsHttpRequest(
    val url: String,
    val method: String,
    val body: String?
)

// 定义一个跨端可序列化的响应对象
@Serializable
data class ArkTsHttpResponse(
    val status: Int,
    val body: String
)

// 1. 定义一个由 ArkTS 侧实现的 HTTP 请求接口 (同步调用，异步回调)
@HarmonyModule(name = "ArkTsHttpFetcher")
interface ArkTsHttpFetcher {
    @HarmonyExport
    fun fetch(requestId: String, request: ArkTsHttpRequest)
}

private object CurrentArkTsFetcher : ArkTsHttpFetcher {
    private var delegateRef: ArkTsHttpFetcher? = null

    fun update(fetcher: ArkTsHttpFetcher) {
        delegateRef = fetcher
    }

    override fun fetch(requestId: String, request: ArkTsHttpRequest) {
        val delegate = delegateRef
            ?: error("ArkTS fetcher not initialized. Call initKtor first.")
        delegate.fetch(requestId, request)
    }
}

class ArkTsEngineConfig : HttpClientEngineConfig()

// 2. 自定义 HttpClientEngineBase，将 Ktor 请求委托给 ArkTsHttpFetcher
@OptIn(InternalAPI::class)
class ArkTsEngine(
    private val fetcher: ArkTsHttpFetcher,
    override val config: ArkTsEngineConfig = ArkTsEngineConfig()
) : HttpClientEngineBase("ArkTsEngine") {

    override val dispatcher: CoroutineDispatcher = Dispatchers.Default
    override val supportedCapabilities = emptySet<HttpClientEngineCapability<*>>()

    override suspend fun execute(data: HttpRequestData): HttpResponseData {
        val callContext = callContext()
        // 解析 Ktor 的请求数据
        val url = data.url.toString()
        val method = data.method.value
        
        val bodyContent = when (val body = data.body) {
            is io.ktor.http.content.TextContent -> body.text
            else -> null 
        }
        
        val arkRequest = ArkTsHttpRequest(url, method, bodyContent)
        
        // 生成唯一 Request ID
        val requestId = KtorPlugin.generateRequestId()
        
        // 创建 Deferred 以挂起当前协程
        val deferred = CompletableDeferred<ArkTsHttpResponse>()
        KtorPlugin.addPendingRequest(requestId, deferred)
        
        println("[ArkTsEngine] Executing request on Ktor: $requestId -> $url")
        
        // 委托给 ArkTS 发起真实网络请求 (触发后立即返回，不阻塞)
        fetcher.fetch(requestId, arkRequest)
        
        println("[ArkTsEngine] Waiting for ArkTS callback: $requestId")
        
        // 挂起等待 ArkTS 回调
        val arkResponse = deferred.await()
        
        println("[ArkTsEngine] Received callback for: $requestId, status: ${arkResponse.status}")
        
        // 构造 Ktor 的 HttpResponseData
        val responseBodyBytes = arkResponse.body.encodeToByteArray()
        
        val responseHeaders = Headers.Empty
        
        return HttpResponseData(
            statusCode = HttpStatusCode(arkResponse.status, "OK"),
            requestTime = GMTDate(),
            headers = responseHeaders,
            version = HttpProtocolVersion.HTTP_1_1,
            body = ByteReadChannel(responseBodyBytes),
            callContext = callContext
        )
    }
}

// 3. 供 ArkTS 调用的入口插件
@HarmonyModule(name = "ktor_plugin")
object KtorPlugin {
    
    private var httpClient: HttpClient? = null
    private var counter = 0
    
    // 暴露给内部引擎使用
    internal var pendingRequestsRef: Map<String, CompletableDeferred<ArkTsHttpResponse>> = emptyMap()
    
    internal fun addPendingRequest(requestId: String, deferred: CompletableDeferred<ArkTsHttpResponse>) {
        pendingRequestsRef = pendingRequestsRef + (requestId to deferred)
    }

    internal fun removePendingRequest(requestId: String): CompletableDeferred<ArkTsHttpResponse>? {
        val deferred = pendingRequestsRef[requestId]
        pendingRequestsRef = pendingRequestsRef - requestId
        return deferred
    }
    
    internal fun generateRequestId(): String {
        counter++
        return "req_$counter"
    }
    
    @OptIn(DelicateCoroutinesApi::class)
    @HarmonyExport
    fun initKtor(fetcher: ArkTsHttpFetcher) {
        CurrentArkTsFetcher.update(fetcher)

        val oldClient = httpClient
        httpClient = HttpClient(ArkTsEngine(CurrentArkTsFetcher)) {
            // 可以配置 json 等等
        }
        
        if (oldClient != null) {
            // Ktor HttpClient.close() 可能会在 Kotlin/Native 主线程阻塞等待所有协程完成
            // 导致 NapiDispatcher 事件循环卡死 (THREAD_BLOCK_6S)，因此我们异步关闭它
            GlobalScope.launch(Dispatchers.Default) {
                try {
                    oldClient.close()
                } catch (e: Throwable) {
                    // Ignore exceptions during close
                }
            }
        }
    }
    
    // ArkTS 网络请求成功后的回调入口
    @HarmonyExport
    fun onFetchSuccess(requestId: String, status: Int, body: String) {
        println("[KtorPlugin] onFetchSuccess invoked for $requestId with status $status")
        val deferred = removePendingRequest(requestId)
        if (deferred != null) {
            println("[KtorPlugin] Found deferred for $requestId, completing...")
            deferred.complete(ArkTsHttpResponse(status, body))
        } else {
            println("[KtorPlugin] Warning: Received success for unknown requestId: $requestId.")
        }
    }

    // ArkTS 网络请求失败后的回调入口
    @HarmonyExport
    fun onFetchError(requestId: String, error: String) {
        println("[KtorPlugin] onFetchError invoked for $requestId with error $error")
        val deferred = removePendingRequest(requestId)
        if (deferred != null) {
            deferred.completeExceptionally(Exception(error))
        } else {
            println("[KtorPlugin] Warning: Received error for unknown requestId: $requestId")
        }
    }
    
    @HarmonyExport
    suspend fun fetchFromUrl(url: String): String {
        val client = httpClient ?: throw IllegalStateException("Ktor not initialized. Call initKtor first.")
        
        // 发起自定义 URL 请求
        val response: HttpResponse = client.get(url)
        
        return "Status: ${response.status.value}, Body: ${response.bodyAsText()}"
    }

    @HarmonyExport
    suspend fun fetchUserFromApi(userId: String): String {
        val client = httpClient ?: throw IllegalStateException("Ktor not initialized. Call initKtor first.")
        
        // 使用 Ktor 官方 API 发起请求，底层会走我们注入的 ArkTsEngine！
        val response: HttpResponse = client.get("https://jsonplaceholder.typicode.com/users/$userId")
        
        return "Status: ${response.status.value}, Body: ${response.bodyAsText()}"
    }
}
