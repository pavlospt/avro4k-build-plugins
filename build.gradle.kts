import java.net.URL
import java.net.URI

buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
    }
}

plugins {
    java
    kotlin("jvm").version(Versions.kotlin)
    id(GradlePlugins.ktlint).version(Versions.ktlint)
}

val releasesRepoUrl: URI = URL("https://oss.sonatype.org/service/local/staging/deploy/maven2").toURI()
val snapshotsRepoUrl: URI = URL("https://oss.sonatype.org/content/repositories/snapshots").toURI()

val isRelease = System.getenv("RELEASE_VERSION") != null
val buildGroupId = "com.magusdevops.avro4k"
val releaseVersion = System.getenv("RELEASE_VERSION") ?: "0.30.0-SNAPSHOT"

allprojects {
    repositories {
        jcenter()
        mavenCentral()
        gradlePluginPortal()
    }

    group = buildGroupId
    version = releaseVersion

    project.ext["isRelease"] = isRelease
    project.ext["buildGroupId"] = buildGroupId
    project.ext["releaseVersion"] = releaseVersion
    project.ext["repoUrl"] = if (isRelease) releasesRepoUrl else snapshotsRepoUrl
}

dependencies {
    implementation(kotlin("stdlib"))
}
