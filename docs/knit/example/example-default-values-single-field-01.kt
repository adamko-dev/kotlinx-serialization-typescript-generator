// This file was automatically generated from default-values.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package example.exampleDefaultValuesSingleField01

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
class Color(val rgb: Int = 12345)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Color.serializer().descriptor))
}
