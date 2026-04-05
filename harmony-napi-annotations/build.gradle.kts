plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    linuxArm64() // HarmonyOS uses linuxArm64 target
    // other targets if needed...
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                // annotations don't need heavy dependencies
            }
        }
    }
}
