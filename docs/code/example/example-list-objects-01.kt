// This file was automatically generated from lists.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleListObjects01

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
data class Colour(
  val rgb: String
)

@Serializable
data class MyLists(
  val colours: List<Colour>,
  val colourGroups: Set<List<Colour>>,
  val colourGroupGroups: LinkedHashSet<List<List<Colour>>>,
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(MyLists.serializer()))
}
