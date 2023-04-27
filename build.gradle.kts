group = "net.minevn"
version = "1.0"

plugins {
    `java-library`
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