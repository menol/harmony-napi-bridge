import io.ktor.client.*
import io.ktor.client.engine.*
import kotlinx.coroutines.*

class DummyEngine : HttpClientEngineBase("Dummy") {
    override val dispatcher = Dispatchers.Default
    override val supportedCapabilities = emptySet<HttpClientEngineCapability<*>>()
    override val config = HttpClientEngineConfig()
    override suspend fun execute(data: HttpRequestData): HttpResponseData {
        throw NotImplementedError()
    }
}

fun main() {
    val client = HttpClient(DummyEngine())
    println("Closing client...")
    client.close()
    println("Client closed.")
}
