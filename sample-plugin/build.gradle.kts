plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
}

kotlin {
    // 鸿蒙设备使用 linuxArm64 架构
    linuxArm64 {
        binaries.sharedLib {
            baseName = "sample_plugin" // 最终产出 libsample_plugin.so
        }
        
        binaries.all {
            freeCompilerArgs += listOf(
                "-Xoverride-konan-properties=linkerGccFlags=-lgcc",
                "-linker-options", "-as-needed"
            )
        }
    }
    
    // 我们加上 jvm 目标来绕开纯 Native 编译的某些坑，同时 KSP 也能更好地处理
    jvm()
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":harmony-napi-annotations"))
                implementation(libs.kotlinx.serialization.json)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
                implementation(libs.koin.core)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
            }
        }
        val linuxArm64Main by getting {
            dependencies {
                implementation(project(":harmony-napi-runtime"))
            }
        }
        val jvmMain by getting {
            dependencies {
            }
        }
    }
}

dependencies {
    // 依赖我们写的 KSP 插件来自动生成胶水代码
    add("kspLinuxArm64", project(":harmony-napi-ksp-processor"))
}

// 可选：将 ksp 的生成目录加入到源码集中，这样 IDE 能跳转查看生成的代码
kotlin.sourceSets.getByName("linuxArm64Main").kotlin.srcDir("build/generated/ksp/linuxArm64/linuxArm64Main/kotlin")
