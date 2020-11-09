buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
    }
}

plugins {
    kotlin("jvm").version(Versions.kotlin)
    id(GradlePlugins.ktlint).version(Versions.ktlint)
}

allprojects {
    repositories {
        jcenter()
        mavenCentral()
        gradlePluginPortal()
    }

    group = "com.magusdevops.avro4k"
    version = project.findProperty("projVersion") ?: "0.0.1-SNAPSHOT"
}

dependencies {
    implementation(kotlin("stdlib"))
}