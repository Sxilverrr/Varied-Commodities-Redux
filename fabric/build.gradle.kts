@file:Suppress("UnstableApiUsage")

plugins {
    id("dev.architectury.loom-no-remap")
    id("architectury-plugin")
    id("com.gradleup.shadow")
}

val loader = prop("loom.platform")!!
val minecraft: String = stonecutter.current.version
val common: Project = requireNotNull(stonecutter.node.sibling("")?.project) {
    "No common project for $project"
}

version = "${mod.version}-$minecraft"
base {
    archivesName.set("${mod.id}-$loader")
}
architectury {
    platformSetupLoomIde()
    fabric()
}

val commonBundle: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

val shadowBundle: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

configurations {
    compileClasspath.get().extendsFrom(commonBundle)
    runtimeClasspath.get().extendsFrom(commonBundle)
    get("developmentFabric").extendsFrom(commonBundle)
}

repositories {
    maven("https://maven.architectury.dev")
    maven("https://maven.shedaniel.me/")
    maven("https://api.modrinth.com/maven") { content { includeGroup("maven.modrinth") } }
    maven("https://maven.blamejared.com") { content { includeGroup("mezz.jei"); includeGroup("net.mezzdev.config") } }
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraft")
    implementation("net.fabricmc:fabric-loader:${common.mod.dep("fabric_loader")}")
    implementation("net.fabricmc.fabric-api:fabric-api:${common.mod.dep("fabric_api")}")
    implementation("dev.architectury:architectury-fabric:${common.mod.dep("architectury")}")
    implementation("me.shedaniel.cloth:cloth-config-fabric:${common.mod.dep("cloth_config")}") {
        exclude(group = "net.fabricmc.fabric-api")
    }
    implementation("maven.modrinth:modmenu:${common.mod.dep("mod_menu")}") { isTransitive = false }

    localRuntime("mezz.jei:jei-$minecraft-$loader:${common.mod.dep("jei")}") { isTransitive = false }
    localRuntime("net.mezzdev.config:mezz_config-$minecraft-$loader:${common.mod.dep("mezz_config")}") { isTransitive = false }
    localRuntime("net.mezzdev.config:mezz_config_gui-$minecraft-$loader:${common.mod.dep("mezz_config_gui")}") { isTransitive = false }

    commonBundle(project(common.path)) { isTransitive = false }
    shadowBundle(project(common.path, "transformProductionFabric")) { isTransitive = false }
}

loom {
    runConfigs.all {
        isIdeConfigGenerated = true
        runDir = "../../../run"
    }
}

java {
    withSourcesJar()
    val java = JavaVersion.VERSION_25
    targetCompatibility = java
    sourceCompatibility = java
}

tasks.shadowJar {
    configurations = listOf(shadowBundle)
    archiveClassifier = null
    exclude("architectury.common.json")
}

tasks.jar {
    archiveClassifier = "dev"
}

tasks.processResources {
    properties(listOf("fabric.mod.json"),
        "id" to mod.id,
        "name" to mod.name,
        "version" to mod.version,
        "minecraft" to common.mod.prop("mc_dep_fabric"),
        "architectury" to common.mod.prop("architectury_dep_fabric")
    )
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}

tasks.build {
    group = "versioned"
    description = "Must run through 'chiseledBuild'"
}

tasks.register<Copy>("buildAndCollect") {
    group = "versioned"
    description = "Must run through 'chiseledBuild'"
    from(tasks.shadowJar.get().archiveFile, tasks.named<Jar>("sourcesJar").get().archiveFile)
    into(rootProject.layout.buildDirectory.file("libs/${mod.version}/$loader"))
    dependsOn("build")
}
