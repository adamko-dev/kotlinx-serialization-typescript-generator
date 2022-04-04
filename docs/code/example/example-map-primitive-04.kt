// This file was automatically generated from maps.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleMapPrimitive04

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
@JvmInline
value class Data(val content: String)

@Serializable
class MyDataClass(
  val mapOfLists: Map<String, Data>
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(MyDataClass.serializer()))
}
