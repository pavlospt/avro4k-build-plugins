# Avro4k Gradle Plugin

This plugin will automatically generate [Avro](https://avro.apache.org/) schema files using 
[Avro4k](https://github.com/avro-kotlin/avro4k) for the 
[kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) library.

## Usage
```kotlin
plugins {
    kotlin("plugin.serialization") version "2.0.0"
    id("io.github.pavlospt.avro4k.gradle-plugin") version "1.0.0"
}
```

## Configuration

```kotlin
avro4KSchemaGenerationExtension {
    scanTestClasses.set(false)
    packageToScan.set(listOf("com.plugin.test"))
}
```

## Generate the schemas
`./gradlew avro4kAvroGenerationTask`