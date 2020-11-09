package com.magusdevops.avro4k.gradleplugin

open class Avro4KSchemaGenerationProperties(val name: String?, val options: MutableMap<String, Any?>) {
    constructor(name: String?):
            this(name,
                    mutableMapOf<String, Any?>(
                            "properties" to mapOf<String, Any?>(),
                            "packageToScan" to setOf<String>(),
                            "outputDirectory" to null,
                            "scanTestClasses" to false,
                            "skip" to false)
            )

    var skip: Boolean? by options
    var packageToScan: Set<String> by options
    var outputDirectory: String? by options
    var scanTestClasses: Boolean? by options
}