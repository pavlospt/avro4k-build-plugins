plugins {
    kotlin("jvm")
    id(GradlePlugins.gradlePluginPublisher).version(Versions.gradlePluginPublish)
    id(GradlePlugins.javaGradlePlugin)
    id(GradlePlugins.kotlinSerialization).version(Versions.kotlin)
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(Dependencies.avro4kCore)
}

gradlePlugin{
    plugins {
        create("avro4kAvroGeneration") {
            id = "com.magusdevops.avro4k.gradleplugin.Avro4KAvroGeneration"
            displayName = "Avro4k Avro Generation"
            description = "Generate Avro Schemas from Avro4k model classes"
            implementationClass = "com.magusdevops.avro4k.gradleplugin.Avro4KAvroGenerationPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/MagusDevOps/avro4k-build-plugins"
    vcsUrl = "https://github.com/MagusDevOps/avro4k-build-plugins.git"
    tags = listOf("avro", "avro4k", "generation")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}
