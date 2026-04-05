plugins {
    kotlin("multiplatform") version "1.9.24" apply false
    kotlin("jvm") version "1.9.24" apply false
    kotlin("plugin.serialization") version "1.9.24" apply false
    id("com.google.devtools.ksp") version "1.9.24-1.0.20" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
