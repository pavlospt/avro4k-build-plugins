import java.net.URI

plugins {
    java
    kotlin("jvm")
    id(GradlePlugins.kotlinSerialization).version(Versions.kotlin)
    id(GradlePlugins.javaGradlePlugin)
    id(GradlePlugins.gradlePluginPublisher).version(Versions.gradlePluginPublish)
    id(GradlePlugins.mavenPublish).apply(true)
    id(GradlePlugins.signing)
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(Dependencies.avro4kCore)
}

publishing {
    repositories {
        maven {
            name = "deploy"
             url = project.ext["repoUrl"]!! as URI
            credentials {
                username = System.getenv("OSSRH_USERNAME") ?: System.getProperty("ossrhUsername")
                password = System.getenv("OSSRH_PASSWORD") ?: System.getProperty("ossrhPassword")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            groupId = project.ext["buildGroupId"]!! as String
            version = project.ext["releaseVersion"]!! as String
        }
    }
}

tasks.withType<Sign>().configureEach {
    onlyIf { project.ext["isRelease"]!! as Boolean }
}

signing {
    setRequired({project.ext["isRelease"]!! as Boolean})

    val signingKeyId: String? = System.getenv("SIGNING_KEY_ID")
    val signingKey: String? = System.getenv("SIGNING_KEY")
    val signingPassword: String? = System.getenv("SIGNING_PASSWORD")

    @Suppress("UnstableApiUsage")
    useInMemoryPgpKeys(signingKey, signingPassword)

    sign(publishing.publications.getByName("maven"))
}

gradlePlugin{
    plugins {
        create("avro4kAvroGeneration") {
            id = "com.magusdevops.avro4k.gradle-plugin.Avro4KAvroGeneration"
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
