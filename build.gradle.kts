plugins {
    kotlin("jvm") version "2.1.10"
}

group = "de.kvxd"
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