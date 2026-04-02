import org.gradle.api.attributes.Attribute

plugins {
    id("multiloader-common")
    id("net.neoforged.moddev")
}

val neo_form_version: String by project
val java_version: String by project
val mod_id: String by project
val minecraft_version: String by project
val mod_name: String by project
val mod_author: String by project
val minecraft_version_range: String by project
val fabric_version: String by project
val fabric_loader_version: String by project
val license: String by project
val credits: String by project
val neoforge_version: String by project
val neoforge_loader_version_range: String by project

neoForge {
    neoFormVersion = neo_form_version
    // Automatically enable AccessTransformers if the file exists
    val at = file("src/main/resources/META-INF/accesstransformer.cfg")
    if (at.exists()) {
        accessTransformers.from(at.absolutePath)
    }
}

dependencies {
    add("compileOnly", "org.spongepowered:mixin:0.8.5")
    // fabric and neoforge both bundle mixinextras, so it is safe to use it in common
    add("compileOnly", "io.github.llamalad7:mixinextras-common:0.5.3")
    add("annotationProcessor", "io.github.llamalad7:mixinextras-common:0.5.3")
}

val commonJava by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

val commonResources by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

artifacts {
    add(commonJava.name, sourceSets.named("main").get().java.sourceDirectories.singleFile)
    add(commonResources.name, sourceSets.named("main").get().resources.sourceDirectories.singleFile)
}

// Implement mcgradleconventions loader attribute
val loaderAttribute = Attribute.of("io.github.mcgradleconventions.loader", String::class.java)
listOf("apiElements", "runtimeElements", "sourcesElements", "javadocElements").forEach { variant ->
    configurations.named(variant) {
        attributes {
            attribute(loaderAttribute, "common")
        }
    }
}

sourceSets.configureEach {
    listOf(compileClasspathConfigurationName, runtimeClasspathConfigurationName).forEach { variant ->
        configurations.named(variant) {
            attributes {
                attribute(loaderAttribute, "common")
            }
        }
    }
}


