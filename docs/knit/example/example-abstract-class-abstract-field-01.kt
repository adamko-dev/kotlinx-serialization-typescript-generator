// This file was automatically generated from abstract-classes.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleAbstractClassAbstractField01

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
abstract class AbstractSimpleTypes {
  abstract val aString: String
  abstract var anInt: Int
  abstract val aDouble: Double
  abstract val bool: Boolean
}

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(AbstractSimpleTypes.serializer().descriptor))
}
