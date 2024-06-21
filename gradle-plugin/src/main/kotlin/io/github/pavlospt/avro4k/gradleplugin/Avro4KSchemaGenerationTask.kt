package io.github.pavlospt.avro4k.gradleplugin

import com.github.avrokotlin.avro4k.Avro
import kotlinx.serialization.*
import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.specs.Spec
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.Objects.isNull
import javax.inject.Inject
import kotlin.reflect.full.findAnnotation
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

internal abstract class Avro4KSchemaGenerationTask @Inject constructor(
    private val avro4KSchemaGenerationExtension: Avro4KSchemaGenerationExtension
) : DefaultTask() {

    private val logger: Logger = Logging.getLogger(
        "Avro4k:${Avro4KSchemaGenerationTask::class.java.simpleName}"
    )

    companion object {
        const val NAME = "avro4KSchemaGenerationTask"
    }

    @TaskAction
    fun run() {
        runTarget(avro4KSchemaGenerationExtension)
    }

    @OptIn(InternalSerializationApi::class)
    internal fun runTarget(avro4KSchemaGenerationExtension: Avro4KSchemaGenerationExtension) {
        project.mergeOutputClasspath(avro4KSchemaGenerationExtension.scanTestClasses.get())

        val avroClassesDir = project.getTargetOfMergeOutputClasspath()
        val avroModelConicalClassNames = project.getScanPathClassConicalNames(avro4KSchemaGenerationExtension)
        val classLoader = project.classLoader(javaClass.classLoader, avroClassesDir)

        val thread = Thread.currentThread()
        val originalClassLoader = thread.contextClassLoader

        try {
            thread.contextClassLoader = classLoader
            if (avroModelConicalClassNames.isEmpty()) {
                logger.debug("Avro class names are empty!")
                return
            }

            val srcPath = project.getSrcAbsolutePath().path
            val outputPath = avro4KSchemaGenerationExtension.outputDirectory.getOrElse("$srcPath/avro")

            logger.debug("Output path: $outputPath")

            val avroModelClasses = avroModelConicalClassNames
                .map { classLoader.loadClass(it).kotlin }
                .toSet()

            logger.debug("Avro model classes: ${avroModelClasses.size}")

            val avroModelClassesNonNull = avroModelClasses
                .filter { it.findAnnotation<Serializable>() != null }
                .toSet()

            logger.debug("Avro model classes non null: ${avroModelClassesNonNull.size}")

            val classNameToAvro = avroModelClassesNonNull
                .map { it.qualifiedName to Avro.schema(it.serializer().descriptor).toString(true) }
                .toSet()

            logger.debug("Class name to avro: ${classNameToAvro.size}")

            classNameToAvro.forEach { (first, second) ->
                first?.let {
                    writeAvroFiles(first, second, outputPath)
                    logger.debug("Avro file written successfully!")
                }
            }
        } finally {
            thread.contextClassLoader = originalClassLoader
        }
    }

    private fun writeAvroFiles(className: String, avroSchema: String, filePath: String) {
        val relativeClassPath = className.replace(".", "/")
        val avroSchemaFile = File("${filePath}/${relativeClassPath}.avsc")
        val avroSchemaDir = avroSchemaFile.parentFile

        if (!avroSchemaDir.exists()) {
            avroSchemaDir.mkdirs()
        }

        avroSchemaFile.writeText(avroSchema)
    }

    override fun onlyIf(onlyIfReason: String, onlyIfSpec: Spec<in Task>) {
        TODO("Not yet implemented")
    }

    override fun doNotTrackState(reasonNotToTrackState: String) {
        TODO("Not yet implemented")
    }

    override fun notCompatibleWithConfigurationCache(reason: String) {
        TODO("Not yet implemented")
    }

    override fun setOnlyIf(onlyIfReason: String, onlyIfSpec: Spec<in Task>) {
        TODO("Not yet implemented")
    }
}
