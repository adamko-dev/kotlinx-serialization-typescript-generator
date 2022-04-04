// This file was automatically generated from polymorphism.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleJsonPolymorphic01

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

import kotlinx.serialization.json.*

@Serializable
abstract class Project {
  abstract val name: String
}

@Serializable
data class BasicProject(override val name: String) : Project()

@Serializable
data class OwnedProject(override val name: String, val owner: String) : Project()

object ProjectSerializer : JsonContentPolymorphicSerializer<Project>(Project::class) {
  override fun selectDeserializer(element: JsonElement) = when {
    "owner" in element.jsonObject -> OwnedProject.serializer()
    else                          -> BasicProject.serializer()
  }
}

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(ProjectSerializer))
}
