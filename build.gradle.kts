import xyz.jpenilla.runpaper.task.RunServer

plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.3.1"
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
}

fun TaskProvider<RunServer>.configureTask() {
    this.configure {
        minecraftVersion("1.21.7")
        jvmArgs("-Xmx4G", "-Xms4G", "-Dcom.mojang.eula.agree=true")
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