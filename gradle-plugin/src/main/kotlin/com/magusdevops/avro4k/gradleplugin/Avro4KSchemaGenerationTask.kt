package com.magusdevops.avro4k.gradleplugin

import com.sksamuel.avro4k.Avro
import kotlinx.serialization.*
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.Objects.isNull
import java.util.stream.Collectors
import kotlin.reflect.full.findAnnotation

open class Avro4KSchemaGenerationTask : DefaultTask() {

    @get:Input
    val extension: Avro4KSchemaGenerationExtension by lazy {
        project.extensions.getByName(EXTENSION_NAME) as Avro4KSchemaGenerationExtension
    }

    private fun getTargets(): Iterable<Avro4KSchemaGenerationProperties> {
        val list = extension.targets.map { extension.extend(it) }
        if (list.isEmpty()) {
            return listOf(extension.extend(null))
        }
        return list
    }

    @TaskAction
    fun run() {
        getTargets().filter { !(it.skip?:false) }
                    .forEach(this::runTarget)
    }

    internal fun writeAvroFiles(className: String, avroSchema: String, filePath: String) {
       val relativeClassPath = className.replace(".", "/")
       val avroSchemaFile = File("${filePath}/${relativeClassPath}.avsc")
       val avroSchemaDir = avroSchemaFile.parentFile

       if( !avroSchemaDir.exists()) {
          avroSchemaDir.mkdirs()
       }

       avroSchemaFile.writeText(avroSchema)
    }


    @OptIn(ImplicitReflectionSerializer::class)
    internal fun runTarget(target: Avro4KSchemaGenerationProperties) {
       project.mergeOutputClasspath(target.scanTestClasses)

       val avroClassesDir = project.getTargetOfMergeOutputClasspath()
       val avroModelConicalClassNames = project.getScanPathClassConicalNames(target)
       val classLoader = project.classLoader(javaClass.classLoader, avroClassesDir)

       val thread = Thread.currentThread()
       val originalClassLoader = thread.contextClassLoader

       try  {
          thread.contextClassLoader = classLoader
          if(avroModelConicalClassNames.isEmpty()) { return }

          val srcPath = project.getSrcAbsolutePath().path
          val outputPath =  target.outputDirectory?: "$srcPath/avro"

          val avroModelClasses = avroModelConicalClassNames.stream().map { classLoader.loadClass(it).kotlin }.collect(Collectors.toSet())
          val avroModelClassesNonNull = avroModelClasses.stream().filter { it.findAnnotation<Serializable>() != null }.collect(Collectors.toSet())
          val classNameToAvro = avroModelClassesNonNull.stream().map { Pair(it.qualifiedName, Avro.default.schema(it.serializer()).toString(true)) }.collect(Collectors.toSet())

          classNameToAvro.stream().parallel().forEach { pair: Pair<String?, String>? ->
             if (!isNull(pair)) writeAvroFiles(pair!!.first!!, pair.second, outputPath)
          }

       } finally {
           thread.contextClassLoader = originalClassLoader
       }
    }
}

