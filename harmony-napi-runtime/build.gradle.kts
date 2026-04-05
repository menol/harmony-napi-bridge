import java.util.Properties

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

// 尝试从 local.properties 或环境变量中获取鸿蒙 SDK 路径
val ohosSdk: String = run {
    val localProps = rootProject.file("local.properties")
    if (localProps.exists()) {
        val props = Properties()
        props.load(localProps.inputStream())
        props.getProperty("ohos.sdk.dir")
    } else {
        null
    }
} ?: System.getenv("OHOS_SDK_HOME") 
  ?: "/Applications/DevEco-Studio.app/Contents/sdk/default/openharmony" // Mac 默认安装路径

val sysroot = "$ohosSdk/native/sysroot"

kotlin {
    // 鸿蒙设备使用 linuxArm64 架构
    linuxArm64 {
        compilations.getByName("main") {
            cinterops {
                val napi by creating {
                    defFile(project.file("src/nativeInterop/cinterop/napi.def"))
                    packageName("napi")
                    // 注入 sysroot 的 include 路径
                    compilerOpts(
                        "-I$sysroot/usr/include",
                        "-I$sysroot/usr/include/aarch64-linux-ohos"
                    )
                    // 注入 NAPI 的动态库依赖
                    linkerOpts(
                        "-L$sysroot/usr/lib",
                        "-L$sysroot/usr/lib/aarch64-linux-ohos",
                        "-lace_napi.z"
                    )
                }
                
                val hilog by creating {
                    defFile(project.file("src/nativeInterop/cinterop/hilog.def"))
                    packageName("hilog")
                    compilerOpts(
                        "-I$sysroot/usr/include",
                        "-I$sysroot/usr/include/aarch64-linux-ohos"
                    )
                    linkerOpts(
                        "-L$sysroot/usr/lib",
                        "-L$sysroot/usr/lib/aarch64-linux-ohos",
                        "-lhilog_ndk.z"
                    )
                }
            }
        }
        
        binaries.all {
            // 剔除鸿蒙不支持的 libgcc_s.so.1 依赖 (非常关键！)
            freeCompilerArgs += listOf(
                "-Xoverride-konan-properties=linkerGccFlags=-lgcc",
                "-linker-options", "-as-needed"
            )
        }
    }
    
    // 我们也给运行时加一个 commonMain
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":harmony-napi-annotations"))
                implementation(libs.kotlinx.serialization.json)
            }
        }
        val linuxArm64Main by getting {
            dependencies {
                // 这里将来可以引入 Kotlinx Coroutines 供 NAPI 协程桥接使用
                // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
            }
        }
    }
}
