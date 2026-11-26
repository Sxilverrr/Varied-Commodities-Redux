plugins {
    id("dev.architectury.loom-no-remap")
    id("architectury-plugin")
}

val minecraft = stonecutter.current.version

version = "${mod.version}-$minecraft"
base {
    archivesName.set("${mod.id}-common")
}

architectury.common(stonecutter.tree.branches.mapNotNull {
    if (stonecutter.current.project !in it) null
    else it.project.prop("loom.platform")
})

repositories {
    maven("https://maven.architectury.dev")
    maven("https://maven.blamejared.com") { content { includeGroup("mezz.jei") } }
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraft")
    implementation("net.fabricmc:fabric-loader:${mod.dep("fabric_loader")}")
    implementation("dev.architectury:architectury:${mod.dep("architectury")}")
    compileOnly("mezz.jei:jei-$minecraft-common-api:${mod.dep("jei")}")
}

java {
    withSourcesJar()
    val java = JavaVersion.VERSION_25
    targetCompatibility = java
    sourceCompatibility = java
}

tasks.processResources {
    inputs.property("minecraft", minecraft)
    if (stonecutter.eval(minecraft, ">=26.3")) filesMatching("data/**/*.json") {
        filter { it.replace(Regex("\"(condition|function)\"\\s*:"), "\"type\":") }
    }
}

tasks.build {
    group = "versioned"
    description = "Must run through 'chiseledBuild'"
}
