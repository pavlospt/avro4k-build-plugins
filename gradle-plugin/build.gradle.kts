@file:Suppress("UnstableApiUsage")

plugins {
    `kotlin-dsl`
    `maven-publish`
    kotlin("plugin.serialization") version "2.0.0"
    id("com.gradle.plugin-publish") version "1.2.1"
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(gradleApi())
    implementation("com.github.avro-kotlin.avro4k:avro4k-core:2.0.0-RC3")
}

kotlin {
    compilerOptions {
        apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0)
    }
}

gradlePlugin {
    website = "https://github.com/pavlospt/avro4k-build-plugins"
    vcsUrl = "https://github.com/pavlospt/avro4k-build-plugins.git"
    plugins {
        create("avro4kAvroGeneration") {
            id = "io.github.pavlospt.avro4k.gradle-plugin"
            displayName = "Avro4k Avro Generation"
            description = "Generate Avro Schemas from Avro4k model classes"
            tags = listOf("avro", "schema")
            implementationClass = "io.github.pavlospt.avro4k.gradleplugin.Avro4KAvroGenerationPlugin"
        }
    }
}