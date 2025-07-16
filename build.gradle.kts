import xyz.jpenilla.runpaper.task.RunServer

plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("net.kyori.blossom") version "2.1.0"
    id("com.diffplug.spotless") version "7.0.2"
}

group = "net.strokkur"
version = "1.0.0-DEV"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://eldonexus.de/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.7-R0.1-SNAPSHOT")

    // StrokkCommands
    compileOnly("net.strokkur:strokk-commands-annotations:1.2.4-SNAPSHOT")
    annotationProcessor("net.strokkur:strokk-commands-processor:1.2.4-SNAPSHOT")

    // StrokkConfig
    compileOnly("net.strokkur:strokk-config-annotations:1.0.2")
    annotationProcessor("net.strokkur:strokk-config-processor:1.0.2")

    // Configurate
    compileOnly("org.spongepowered:configurate-yaml:${libs.versions.configurate.get()}")
}

fun TaskProvider<RunServer>.configureTask() {
    this.configure {
        dependsOn(tasks.spotlessCheck)
        
        minecraftVersion("1.21.7")
        jvmArgs("-Xmx4G", "-Xms4G", "-Dcom.mojang.eula.agree=true")
        
        downloadPlugins.url("https://download.luckperms.net/1594/bukkit/loader/LuckPerms-Bukkit-5.5.9.jar")
    }
}

tasks {
    processResources {
        filesMatching("paper-plugin.yml") {
            expand(
                "version" to version,
                "mcVersion" to "1.21.7"
            )
        }
    }

    runServer.configureTask()
    runPaper.folia.registerTask().configureTask()
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

spotless {
    java {
        licenseHeaderFile(rootProject.file("HEADER"))
        removeUnusedImports()
        target("**/*.java")
    }
}

sourceSets.main {
    blossom.javaSources {
        property("configurate", libs.versions.configurate.get())
    }
}