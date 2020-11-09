package com.magusdevops.avro4k.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

class Avro4KPlugin : Plugin<Project>{
    override fun apply(project: Project) {
        project.plugins.apply(JavaPlugin::class.java)

        project.extensions.create(EXTENSION_NAME, Avro4KSchemaGenerationExtension::class.java).apply {
            targets = project.container(Avro4KSchemaGenerationProperties::class.java)
        }

        project.tasks.create(PLUGIN_NAME, Avro4KSchemaGenerationTask::class.java) {
           it.dependsOn(project.tasks.getByPath("classes"))
        }

        project.configurations.create(CONFIGURATION_NAME)
        project.dependencies.add(CONFIGURATION_NAME, "com.sksamuel.avro4k:avro4k-core")
        project.dependencies.add(CONFIGURATION_NAME, "org.jetbrains.kotlin:kotlin-serialization")
    }
}