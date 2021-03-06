// This file was automatically generated from maps.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleMapPrimitive03

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
class MapsWithLists(
  val mapOfLists: Map<String, List<String>>
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(MapsWithLists.serializer()))
}
