// This file was automatically generated from polymorphism.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.examplePolymorphicSealedClass01

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
sealed class Project {
  abstract val name: String
}

@Serializable
@SerialName("owned-project")
class OwnedProject(override val name: String, val owner: String) : Project()

@Serializable
class DeprecatedProject(override val name: String, val reason: String) : Project()

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(OwnedProject.serializer()))
}
