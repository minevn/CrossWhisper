group = "net.minevn"
version = "1.0"

plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

dependencies {
    implementation(project(":CrossWhisperBukkit"))
    implementation(project(":CrossWhisperBungee"))
}

tasks {
    val jarName = "CrossWhisper"

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



allprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
        mavenLocal()

        maven ("https://hub.spigotmc.org/nexus/content/repositories/snapshots/" )
        maven ("https://oss.sonatype.org/content/repositories/snapshots")
    }

    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(8))
        }
    }
}