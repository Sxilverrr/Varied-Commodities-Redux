plugins {
    id("dev.kikugie.stonecutter")
    id("dev.architectury.loom-no-remap") version "1.17-SNAPSHOT" apply false
    id("architectury-plugin") version "3.5.170" apply false
    id("com.gradleup.shadow") version "9.4.3" apply false
}
stonecutter active "26.1.2" /* [SC] DO NOT EDIT */

stonecutter parameters {
    val v26_3 = eval(metadata.version, ">=26.3")
    replacement(v26_3, "pose.mulPose(", "pose.rotate(")
    replacement(v26_3, "monster.EnderMan", "monster.Enderman")
    replacement(v26_3, "instanceof EnderMan", "instanceof Enderman")
    replacement(v26_3, "PushReaction.DESTROY", "PushReaction.POPPED")
}

stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) {
    group = "project"
    ofTask("buildAndCollect")
}

for (it in stonecutter.tree.branches) {
    if (it.id.isEmpty()) continue
    val loader = it.id.upperCaseFirst()
    stonecutter registerChiseled tasks.register("chiseledBuild$loader", stonecutter.chiseled) {
        group = "project"
        versions { branch, _ -> branch == it.id }
        ofTask("buildAndCollect")
    }
}

for (it in stonecutter.tree.nodes) {
    if (it.metadata != stonecutter.current || it.branch.id.isEmpty()) continue
    val types = listOf("Client", "Server")
    val loader = it.branch.id.upperCaseFirst()
    for (type in types) tasks.register("runActive$type$loader") {
        group = "project"
        dependsOn("${it.hierarchy}:run$type")
    }
}
