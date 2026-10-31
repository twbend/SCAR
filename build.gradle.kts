import godot.entrygenerator.settings.RegisteredNameMode
import godot.entrygenerator.settings.RegistrationFileLayoutMode
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
    godotProjectDirectory.set(file(".")) // only change this if the Godot project root is not this Gradle project directory
    registrationFilesDirectory.set(file("gdj")) // defaults to <godotProjectDirectory>/gdj

    registrationFilesLayoutMode.set(RegistrationFileLayoutMode.FLAT)
    registrationNameMode.set(RegisteredNameMode.SIMPLE_NAME)

    // Enable coroutines integration with Godot signal/lifecycle callbacks.
    isGodotCoroutinesEnabled.set(true)

    // --------- Toolchain ---------
    kotlinVersion.set(libs.versions.kotlin)
}
