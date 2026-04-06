package com.itime.harmony.sample

import com.itime.harmony.napi.annotations.HarmonyExport
import com.itime.harmony.napi.annotations.HarmonyModule
import org.koin.core.component.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.dsl.module

// 1. 定义一个由 ArkTS 侧实现的存储接口
@HarmonyModule(name = "KeyValueStorage")
interface KeyValueStorage {
    @HarmonyExport
    fun saveString(key: String, value: String)
    
    @HarmonyExport
    fun getString(key: String): String
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

// 3. 供 ArkTS 调用的入口插件
@HarmonyModule(name = "storage_plugin")
object StoragePlugin : KoinComponent {

    @HarmonyExport
    fun initArkTsStorage(storageImpl: KeyValueStorage) {
        val storageModule = module {
            // 将 ArkTS 传进来的 JS 代理对象，作为单例注入到 Koin 容器中
            single<KeyValueStorage> { storageImpl }
            single { SettingsRepository(get()) }
        }
        
        // 健壮性处理：如果 Koin 尚未启动则启动，如果已启动则动态加载 Module
        try {
            startKoin { modules(storageModule) }
        } catch (e: Throwable) {
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