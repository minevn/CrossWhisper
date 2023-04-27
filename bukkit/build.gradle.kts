plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

dependencies {
    implementation(project(":CrossWhisperModels"))
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
}

tasks {
    val jarName = "CrossWhisperBukkit"

    register("customCopy") {
        dependsOn(shadowJar)

        val path = project.properties["shadowPath"]
        if (path != null) {
            doLast {
                println(path)
                copy {
                    from("build/libs/$jarName.jar")
                    into(path)
                }
                println("Copied")
            }
        }
    }

    shadowJar {
        archiveFileName.set("$jarName.jar")
    }

    assemble {
        dependsOn(shadowJar, get("customCopy"))
    }
}

