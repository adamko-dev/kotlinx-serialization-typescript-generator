// This file was automatically generated from abstract-classes.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package example.exampleAbstractClassAbstractField01

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
abstract class Color {
  abstract val rgb: Int
}

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Color.serializer().descriptor))
}
