import org.koin.core.context.startKoin
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module
import org.koin.mp.KoinPlatformTools

val m1 = module { single { "Hello" } }
fun test3() {
    println("Starting Koin...")
    startKoin { modules(m1) }
    println("Koin Started. Now Unloading...")
    unloadKoinModules(m1)
    println("Unloaded. Now Loading...")
    loadKoinModules(m1)
    println("Loaded.")
}
