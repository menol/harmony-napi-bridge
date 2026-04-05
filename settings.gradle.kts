pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "harmony-napi-bridge"

include(":harmony-napi-annotations")
include(":harmony-napi-runtime")
include(":harmony-napi-ksp-processor")
include(":sample-plugin")
