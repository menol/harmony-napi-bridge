package com.realtech.harmony.sample

import com.realtech.harmony.napi.annotations.HarmonyExport
import com.realtech.harmony.napi.annotations.HarmonyModule
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.unloadKoinModules
import org.koin.mp.KoinPlatformTools
import org.koin.dsl.module

// 1. Define a service interface and implementation
interface GreetingService {
    fun greet(name: String): String
}

class GreetingServiceImpl : GreetingService {
    override fun greet(name: String): String {
        return "Hello $name, injected by Koin!"
    }
}

// 2. Define Koin Module
val appModule = module {
    single<GreetingService> { GreetingServiceImpl() }
}

// 3. Export to HarmonyOS
@HarmonyModule(name = "koin_plugin")
object KoinPlugin : KoinComponent {

    // Lazy inject the service
    private val greetingService: GreetingService by inject()

    @HarmonyExport
    fun initKoin() {
        if (KoinPlatformTools.defaultContext().getOrNull() == null) {
            startKoin {
                modules(appModule)
            }
        } else {
            unloadKoinModules(appModule)
            loadKoinModules(appModule)
        }
    }

    @HarmonyExport
    fun getGreeting(name: String): String {
        return greetingService.greet(name)
    }
}
