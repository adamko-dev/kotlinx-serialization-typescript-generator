// This file was automatically generated from basic-classes.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package example.examplePlainDataClass01

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
data class SomeDataClass(
  val aString: String,
  var anInt: Int,
  val aDouble: Double,
  val bool: Boolean,
  private val privateMember: String,
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(SomeDataClass.serializer().descriptor))
}
