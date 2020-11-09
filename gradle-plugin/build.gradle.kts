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
            id = "avro4kAvroGeneration"
            implementationClass = "com.magusdevops.avro4k.gradleplugin.Avro4KPlugin"
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}
