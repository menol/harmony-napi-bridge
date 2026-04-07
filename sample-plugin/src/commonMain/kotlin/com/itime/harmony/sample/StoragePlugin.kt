package com.itime.harmony.sample

import com.itime.harmony.napi.annotations.HarmonyExport
import com.itime.harmony.napi.annotations.HarmonyModule
import org.koin.core.component.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.unloadKoinModules
import org.koin.mp.KoinPlatformTools
import org.koin.dsl.module

// 1. 定义一个由 ArkTS 侧实现的存储接口
@HarmonyModule(name = "KeyValueStorage")
interface KeyValueStorage {
    @HarmonyExport
    fun saveString(key: String, value: String)
    
    @HarmonyExport
    fun getString(key: String): String
}

private object CurrentArkTsStorage : KeyValueStorage {
    private var delegateRef: KeyValueStorage? = null

    fun update(storage: KeyValueStorage) {
        delegateRef = storage
    }

    override fun saveString(key: String, value: String) {
        requireDelegate().saveString(key, value)
    }

    override fun getString(key: String): String {
        return requireDelegate().getString(key)
    }

    private fun requireDelegate(): KeyValueStorage {
        return delegateRef ?: error("ArkTS storage not initialized. Call initArkTsStorage first.")
    }
}

// 2. Kotlin 侧的业务逻辑类，它依赖于 KeyValueStorage 接口（不知道具体实现是啥）
class SettingsRepository(private val storage: KeyValueStorage) {
    fun saveTheme(theme: String) {
        storage.saveString("theme", theme)
    }
    
    fun getTheme(): String {
        return storage.getString("theme")
    }
}

private val storageModule = module {
    // Keep a stable Koin singleton and swap the ArkTS delegate behind it between test runs.
    single<KeyValueStorage> { CurrentArkTsStorage }
    single { SettingsRepository(get()) }
}

// 3. 供 ArkTS 调用的入口插件
@HarmonyModule(name = "storage_plugin")
object StoragePlugin : KoinComponent {

    @HarmonyExport
    fun initArkTsStorage(storageImpl: KeyValueStorage) {
        CurrentArkTsStorage.update(storageImpl)

        if (KoinPlatformTools.defaultContext().getOrNull() == null) {
            startKoin { modules(storageModule) }
        } else {
            unloadKoinModules(storageModule)
            loadKoinModules(storageModule)
        }
    }

    @HarmonyExport
    fun testStorageRoundTrip(): String {
        // 从 Koin 容器中取出 SettingsRepository
        val repo = getKoin().get<SettingsRepository>()
        
        // 调用业务方法：这会触发对 ArkTS 侧 saveString 的调用
        repo.saveTheme("Dark Mode from Kotlin")
        
        // 再次调用业务方法：这会触发对 ArkTS 侧 getString 的调用，并返回结果
        return repo.getTheme()
    }
}
