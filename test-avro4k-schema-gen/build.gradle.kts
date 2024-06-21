plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
    id("io.github.pavlospt.avro4k.gradle-plugin") version "1.0.0"
}

group = "io.github.pavlospt.avro4k.test"
version = "0.1"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("com.github.avro-kotlin.avro4k:avro4k-core:2.0.0-RC3")
}

avro4KSchemaGenerationExtension {
    scanTestClasses.set(false)
    packageToScan.set(listOf("com.plugin.test"))
}