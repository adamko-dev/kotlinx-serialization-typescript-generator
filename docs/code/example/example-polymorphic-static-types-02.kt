// This file was automatically generated from polymorphism-sealed.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.examplePolymorphicStaticTypes02

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

import kotlinx.serialization.modules.*

@Serializable
abstract class Project {
  abstract val name: String
}

@Serializable
class OwnedProject(override val name: String, val owner: String) : Project()

val module = SerializersModule {
  polymorphic(Project::class) {
    subclass(OwnedProject::class)
  }
}

fun main() {
  val config = KxsTsConfig(serializersModule = module)

  val tsGenerator = KxsTsGenerator(config)

  println(tsGenerator.generate(Project.serializer()))
}
