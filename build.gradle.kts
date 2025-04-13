plugins {
    kotlin("jvm") version "2.1.10"
    `maven-publish`
    signing
}

group = "io.github.0x1bd"
version = "1.0.1"
description = "A simple, lightweight and fully extensible signal system "

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

publishing {
    publications {
        create<MavenPublication>("mavenJava") {

            artifactId = "ksignal"
            from(components["java"])

            pom {
                name.set("KSignal")
                description.set(project.description)
                url.set("https://github.com/0x1bd/KSignal")

                licenses {
                    license {
                        name.set("GNU GPLv3")
                        url.set("https://www.gnu.org/licenses/gpl-3.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("0x1bd")
                        name.set("kvxd")
                        email.set("0x1bd@proton.me")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/0x1bd/KSignal.git")
                    developerConnection.set("scm:git:ssh://github.com/0x1bd/KSignal.git")
                    url.set("https://github.com/0x1bd/KSignal")
                }
            }

        }
    }

    repositories {
        maven {
            val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl

            credentials {
                username = project.findProperty("ossrhUsername") as String? ?: System.getenv("OSSRH_USERNAME")
                password = project.findProperty("ossrhPassword") as String? ?: System.getenv("OSSRH_PASSWORD")
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["mavenJava"])
}