// This file was automatically generated from basic-classes.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.examplePlainClassSingleField01

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
class Color(val rgb: Int)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Color.serializer().descriptor))
}
