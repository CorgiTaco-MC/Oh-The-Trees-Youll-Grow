import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.language.jvm.tasks.ProcessResources

plugins {
    `java-library`
    `maven-publish`
}

val mod_id: String by project
val minecraft_version: String by project
val java_version: String by project
val mod_name: String by project
val mod_author: String by project
val minecraft_version_range: String by project
val fabric_version: String by project
val fabric_loader_version: String by project
val license: String by project
val credits: String by project
val neoforge_version: String by project
val neoforge_loader_version_range: String by project

base {
    archivesName.set("${mod_id}-${project.name}-${minecraft_version}")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(java_version.toInt()))
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
    // https://docs.gradle.org/current/userguide/declaring_repositories.html#declaring_content_exclusively_found_in_one_repository
    exclusiveContent {
        forRepository {
            maven {
                name = "Sponge"
                url = uri("https://repo.spongepowered.org/repository/maven-public")
            }
        }
        filter { includeGroupAndSubgroups("org.spongepowered") }
    }
    maven {
        name = "BlameJared"
        url = uri("https://maven.blamejared.com")
    }
}

tasks.named<Jar>("sourcesJar") {
    from(rootProject.file("LICENSE")) {
        rename { "${it}_${mod_name}" }
    }
}

tasks.named<Jar>("jar") {
    from(rootProject.file("LICENSE")) {
        rename { "${it}_${mod_name}" }
    }

    manifest {
        attributes(
            mapOf(
                "Specification-Title" to mod_name,
                "Specification-Vendor" to mod_author,
                "Specification-Version" to archiveVersion.get(),
                "Implementation-Title" to project.name,
                "Implementation-Version" to archiveVersion.get(),
                "Implementation-Vendor" to mod_author,
                "Built-On-Minecraft" to minecraft_version,
            ),
        )
    }
}

tasks.named<ProcessResources>("processResources") {
    val expandProps: Map<String, Any?> = mapOf(
        "version" to version,
        "group" to project.group, // Else we target the task's group.
        "minecraft_version" to minecraft_version,
        "minecraft_version_range" to minecraft_version_range,
        "fabric_version" to fabric_version,
        "fabric_loader_version" to fabric_loader_version,
        "mod_name" to mod_name,
        "mod_author" to mod_author,
        "mod_id" to mod_id,
        "license" to license,
        "description" to project.description,
        "neoforge_version" to neoforge_version,
        "neoforge_loader_version_range" to neoforge_loader_version_range,
        "credits" to credits,
        "java_version" to java_version,
    )

    val jsonExpandProps = expandProps.mapValues { (_, value) ->
        if (value is String) value.replace("\n", "\\\\n") else value
    }

    filesMatching(listOf("META-INF/mods.toml", "META-INF/neoforge.mods.toml")) {
        expand(expandProps)
    }

    filesMatching(listOf("pack.mcmeta", "fabric.mod.json", "*.mixins.json")) {
        expand(jsonExpandProps)
    }

    inputs.properties(expandProps)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = base.archivesName.get()
            from(components["java"])
        }
    }
    repositories {
        maven {
            val releasesRepoUrl = "https://maven.jt-dev.tech/releases"
            val snapshotsRepoUrl = "https://maven.jt-dev.tech/snapshots"
            val versionString = project.version.toString()
            val isSnapshot = versionString.endsWith("SNAPSHOT") || versionString.startsWith("0")

            url = uri(if (isSnapshot) snapshotsRepoUrl else releasesRepoUrl)
            name = "JTDev"

            credentials {
                username = project.properties["repoLogin"]?.toString()
                password = project.properties["repoPassword"]?.toString()
            }
        }
    }}


