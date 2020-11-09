package com.magusdevops.avro4k.gradleplugin

import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.stream.Collectors

internal fun Project.classLoader(parent: ClassLoader, target: File): ClassLoader {
   val classURLs = mutableSetOf<URL>()

   classURLs.add(target.toURI().toURL())

   configurations.getByName("runtimeClasspath").forEach {
      classURLs.add(it.toURI().toURL())
   }

   logger.info(buildString {
      appendln("classpath:")
      appendln(classURLs.joinToString("\n  * ", prefix = "  * "))
   })

   return URLClassLoader(classURLs.toTypedArray(), parent)
}

internal fun Project.getTargetOfMergeOutputClasspath(): File {
   return buildDir.resolve("classes-merged")
}

internal fun Project.getSourceSets(scanTestClasses: Boolean? = false): Set<SourceSet> {
   val sourceSetContainer = this.properties["sourceSets"] as SourceSetContainer?

   if (scanTestClasses?: false) {
      return sourceSetContainer?.toSet()?: setOf()
   }

   return sourceSetContainer?.stream()?.filter { !it.name.contains("test", true) }?.collect(Collectors.toSet())?: setOf()
}

internal fun Project.getSrcAbsolutePath(): File {
   return getSourceSets(false).firstOrNull()?.java?.srcDirTrees?.firstOrNull()?.dir?.parentFile?: file("/tmp")
}

internal fun Project.mergeOutputClasspath(scanTestClasses: Boolean? = false) {
    val target = getTargetOfMergeOutputClasspath()
    val sources = mutableSetOf<File>()
    val sourceSets = getSourceSets(scanTestClasses)

    target.deleteRecursively()

    for (sourceSet in sourceSets) {
        sources.addAll(sourceSet.output.classesDirs)
    }

    copy {
        it.from(sources)
        it.into(target)
    }
}

internal fun Project.getClassConicalNamesFormMergeOutput(): Set<String> {
   val mergedClassRoot = getTargetOfMergeOutputClasspath()
   val tree = fileTree(
            mutableMapOf(
               "dir" to mergedClassRoot.path,
               "include" to "**/*.class"))

   return tree.files
      .filter { !it.name.contains("$") }
      .map {
         it.path
            .replace("${mergedClassRoot.path}/", "")
            .replace("/", ".")
            .replace(".class", "")
      }.toSet()
}


internal fun Project.getScanPathClassConicalNames(target: Avro4KSchemaGenerationProperties) : Set<String> {
   val allClasses = getClassConicalNamesFormMergeOutput()
   val scanPackages = target.packageToScan
   return allClasses.stream()
      .filter { allClass ->
         scanPackages.stream()
            .filter { scanPackage ->
                allClass.startsWith(scanPackage, true)
            }.count() > 0
      }.collect(Collectors.toSet())
}