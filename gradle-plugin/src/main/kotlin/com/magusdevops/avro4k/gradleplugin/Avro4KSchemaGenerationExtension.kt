package com.magusdevops.avro4k.gradleplugin

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer

open class Avro4KSchemaGenerationExtension: Avro4KSchemaGenerationProperties(null) {
    lateinit var targets: NamedDomainObjectContainer<Avro4KSchemaGenerationProperties>

    fun targets(action: Action<NamedDomainObjectContainer<Avro4KSchemaGenerationProperties>>) {
        action.execute(targets)
    }

    fun extend(other: Avro4KSchemaGenerationProperties?) : Avro4KSchemaGenerationProperties {
        return Avro4KSchemaGenerationProperties(other?.name, merge(this, other))
    }

    companion object {
        fun merge(base: Avro4KSchemaGenerationExtension, target: Avro4KSchemaGenerationProperties? = null): MutableMap<String, Any?> {
            val map = mutableMapOf<String, Any?>()
            val packageToScan = mutableSetOf<String>()
            val schemaProperties = listOfNotNull(base, target)
            val outputDirectory = target?.outputDirectory?:  base.outputDirectory

            for (schemaProperty in schemaProperties) {
                map.putAll(schemaProperty.options.filterKeys { !MERGE_EXCLUDED_PROPERTIES.contains(it) })
                packageToScan.addAll(schemaProperty.packageToScan)
            }

            map.putAll(mapOf("outputDirectory" to outputDirectory , "packageToScan" to packageToScan))

            return map
        }
    }
}