import org.gradle.api.attributes.Attribute
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.language.jvm.tasks.ProcessResources

plugins {
    id("multiloader-common")
}

val loaderAttribute = Attribute.of("io.github.mcgradleconventions.loader", String::class.java)

configurations {
    create("commonJava") {
        isCanBeResolved = true
    }
    create("commonResources") {
        isCanBeResolved = true
    }
}

dependencies {
    compileOnly(project(":common")) {
        attributes {
            attribute(loaderAttribute, "common")
        }
    }
    add("commonJava", project(mapOf("path" to ":common", "configuration" to "commonJava")))
    add("commonResources", project(mapOf("path" to ":common", "configuration" to "commonResources")))
}

tasks.named<JavaCompile>("compileJava") {
    val commonJava = configurations.named("commonJava")
    dependsOn(commonJava)
    source(commonJava.get())
}

tasks.named<ProcessResources>("processResources") {
    val commonResources = configurations.named("commonResources")
    dependsOn(commonResources)
    from(commonResources.get())
}

tasks.named<Javadoc>("javadoc") {
    val commonJava = configurations.named("commonJava")
    dependsOn(commonJava)
    source(commonJava.get())
}

tasks.named<Jar>("sourcesJar") {
    val commonJava = configurations.named("commonJava")
    val commonResources = configurations.named("commonResources")
    dependsOn(commonJava)
    from(commonJava.get())
    dependsOn(commonResources)
    from(commonResources.get())
}

