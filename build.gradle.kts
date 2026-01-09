plugins {
    kotlin("jvm") version "2.2.21"
}

group = "cat.montilivi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("io.github.aakira:napier:2.7.1")


    testImplementation(kotlin("test"))


}

kotlin {
    jvmToolchain(24)
}

tasks.test {
    useJUnitPlatform()
}