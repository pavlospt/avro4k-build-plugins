# Avro4k Gradle Plugin

This plugin will automatically generate [Avro](https://avro.apache.org/) schema files using 
[Avro4k](https://github.com/avro-kotlin/avro4k) for the 
[kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) library.

## Usage
Add the following to your build.gradle file
```groovy
buildscript {
    repositories {
        mavenCentral()
    }
    
    dependencies {
        implementation 'com.magusdevops.avro4k:gradle-plugin:0.30.0.RC4'
    }

}
repositories {
    mavenCentral()
}
apply plugin: 'com.magusdevops.avro4k.gradle-plugin'
```

alternatively, you can use the new plugin syntax for gradle `2.1+`

```groovy
plugins {
    id 'com.magusdevops.avro4k.gradle-plugin'
}
```

## Configuration

```groovy
avro4kAvroGeneration {
    packageToScan = setOf("com.magusdevops")
}
```

## Generate the schemas
`./gradlew avro4kAvroGeneration`