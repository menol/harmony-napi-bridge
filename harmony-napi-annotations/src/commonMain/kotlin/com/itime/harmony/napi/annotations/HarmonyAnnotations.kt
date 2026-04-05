package com.itime.harmony.napi.annotations

@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.SOURCE)
annotation class HarmonyModule(val name: String)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class HarmonyExport(val name: String = "")

@Target(AnnotationTarget.FILE)
@Retention(AnnotationRetention.SOURCE)
annotation class HarmonyExtensions(val name: String = "Extensions")
