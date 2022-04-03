// This file was automatically generated from lists.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleListPrimitive01

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
data class MyLists(
  val strings: List<String>,
  val ints: List<Int>,
  val longs: List<Long>,
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(MyLists.serializer()))
}
