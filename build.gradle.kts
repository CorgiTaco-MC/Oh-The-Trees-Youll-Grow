plugins {
    // see https://fabricmc.net/develop/ for new versions
    id("net.fabricmc.fabric-loom") version "1.15-SNAPSHOT" apply false
    // see https://projects.neoforged.net/neoforged/moddevgradle for new versions
    id("net.neoforged.moddev") version "2.0.141" apply false

    kotlin("jvm") version "2.3.0"
}

subprojects {
    // Only configure Java dependency buckets after the Java plugin is applied.
    pluginManager.withPlugin("java") {
        dependencies {
            add("compileOnly", "com.google.auto.service:auto-service:1.1.1")
            add("annotationProcessor", "com.google.auto.service:auto-service:1.1.1")
        }
    }
}

repositories {
    mavenCentral()
}