architectury {
    common("forge", "fabric")
    platformSetupLoomIde()
}

loom.accessWidenerPath.set(file("src/main/resources/ohthetreesyoullgrow.accesswidener"))

sourceSets.main.get().resources.srcDir("src/main/generated/resources")

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${project.properties["fabric_loader_version"]}")
}