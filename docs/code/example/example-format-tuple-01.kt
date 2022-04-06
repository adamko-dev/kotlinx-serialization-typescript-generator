// This file was automatically generated from export-formats.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleFormatTuple01

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
@TsExport(format = TsExport.Format.TUPLE)
class SimpleTypes(
  val aString: String,
  var anInt: Int,
  val aDouble: Double,
  val bool: Boolean,
  private val privateMember: String,
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(SimpleTypes.serializer()))
}
