pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "avro4k-build-plugins"

include("gradle-plugin")
include("test-avro4k-schema-gen")
