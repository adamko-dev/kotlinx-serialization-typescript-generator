// This file was automatically generated from polymorphism-open.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleAbstractClassSingleField01

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
abstract class Color(val rgb: Int)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Color.serializer()))
}
