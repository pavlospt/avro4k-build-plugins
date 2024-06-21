package io.github.pavlospt.avro4k.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.named

class Avro4KAvroGenerationPlugin : Plugin<Project> {

    companion object {
        private const val PLUGIN_CONFIGURATION_NAME = "avro4kAvroGeneration"
    }

    override fun apply(target: Project) {

        val avro4kExt = target
            .extensions
            .create<Avro4KSchemaGenerationExtension>(Avro4KSchemaGenerationExtension.NAME)

        target.tasks
            .register(Avro4KSchemaGenerationTask.NAME, Avro4KSchemaGenerationTask::class.java, avro4kExt)

        target.tasks
            .named<Avro4KSchemaGenerationTask>(Avro4KSchemaGenerationTask.NAME)
            .configure {
                dependsOn(target.tasks.getByPath("classes"))
            }

        target.configurations.create(PLUGIN_CONFIGURATION_NAME)
    }
}