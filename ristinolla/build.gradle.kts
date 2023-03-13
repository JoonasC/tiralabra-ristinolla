import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlinx.kover") version "0.6.1"
    kotlin("jvm") version "1.8.0"
    id("org.openjfx.javafxplugin") version "0.0.8"
}

group = "fi.helsinki"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("no.tornado:tornadofx:1.7.20")
    testImplementation(kotlin("test"))
}

javafx {
    version = "11.0.2"
    modules("javafx.controls", "javafx.graphics")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}
