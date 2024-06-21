buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
    }
}

plugins {
    kotlin("jvm") version "2.0.0"
}

val buildGroupId = "io.github.pavlospt.avro4k"
val releaseVersion = "1.0.0"

allprojects {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    group = buildGroupId
    version = releaseVersion
}

dependencies {
    implementation(kotlin("stdlib"))
}
