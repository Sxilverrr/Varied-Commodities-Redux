pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev")
        maven("https://maven.minecraftforge.net")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.kikugie.dev/snapshots")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.6"
}

stonecutter {
    centralScript = "build.gradle.kts"
    kotlinController = true
    create(rootProject) {
        versions("26.1.2", "26.2", "26.3")
        branch("neoforge") { versions("26.1.2", "26.2", "26.3") }
        branch("fabric")   { versions("26.1.2", "26.2", "26.3") }
    }
}

rootProject.name = "VariedCommoditiesRedux"
