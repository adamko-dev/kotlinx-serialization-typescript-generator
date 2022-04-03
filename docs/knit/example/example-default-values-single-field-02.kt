// This file was automatically generated from default-values.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleDefaultValuesSingleField02

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
class Colour(val rgb: Int?) // 'rgb' is required, but the value can be null

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Colour.serializer()))
}
