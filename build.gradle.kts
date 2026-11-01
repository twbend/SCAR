import godot.annotation.processor.classgraph.AnnotationProcessingMode
import godot.gradle.GodotLanguage

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.godot.jvm.lib)
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

godot {
    // --------- Setup ---------
    languages.set(setOf(GodotLanguage.KOTLIN))

    registration.annotationProcessingMode.set(AnnotationProcessingMode.Inferred)

    // Enable coroutines integration with Godot signal/lifecycle callbacks.
    isGodotCoroutinesEnabled.set(true)

    // --------- Toolchain ---------
    toolchain.kotlinVersion.set(libs.versions.kotlin)
}

dependencies {
    testImplementation(libs.j.unit.api)
}

tasks.test {
    useJUnitPlatform()
}
