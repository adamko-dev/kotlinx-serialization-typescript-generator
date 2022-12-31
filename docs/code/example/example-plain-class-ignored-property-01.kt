// This file was automatically generated from ignoring-properties.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.examplePlainClassIgnoredProperty01

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

import kotlinx.serialization.Transient

@Serializable
class SimpleTypes(
  @Transient
  val aString: String = "default-value"
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(SimpleTypes.serializer()))
}
