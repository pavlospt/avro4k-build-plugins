package io.github.pavlospt.avro4k.gradleplugin

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import javax.inject.Inject

open class Avro4KSchemaGenerationExtension @Inject constructor(
    objects: ObjectFactory
) {

    companion object {
        const val NAME = "avro4KSchemaGenerationExtension"
    }

    @get:Input
    val skip: Property<Boolean> = objects.property(Boolean::class.java)

    @get:Input
    val packageToScan: ListProperty<String> = objects.listProperty(String::class.java)

    @get:Input
    val outputDirectory: Property<String> = objects.property(String::class.java)

    @get:Input
    val scanTestClasses: Property<Boolean> = objects.property(Boolean::class.java)

    init {
        skip.convention(false)
        scanTestClasses.convention(false)
    }
}