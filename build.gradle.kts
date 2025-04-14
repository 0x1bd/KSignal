plugins {
    kotlin("jvm") version "2.1.10"
    id("io.deepmedia.tools.deployer") version "0.16.0"
}

group = "io.github.0x1bd"
version = "1.0.1"
description = "A simple, lightweight and fully extensible signal system"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

java {
    withJavadocJar()
    withSourcesJar()
}

deployer {
    content {
        component {
            fromJava()
        }
    }

    projectInfo {
        description = project.description.toString()
        url = "https://github.com/0x1bd/KSignal"
        scm.fromGithub("0x1bd", "KSignal")
        license("GNU GPL 3.0", "https://www.gnu.org/licenses/gpl-3.0.txt")
        developer("0x1bd", "0x1bd@proton.me")
        groupId = project.group.toString()
    }

    centralPortalSpec {
        signing.key = secret("SIGNING_KEY")
        signing.password = secret("SIGNING_PASSPHRASE")

        auth.user = secret("CENTRAL_USERNAME")
        auth.password = secret("CENTRAL_PASSWORD")
    }
}