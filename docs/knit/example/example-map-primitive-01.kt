// This file was automatically generated from maps.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package example.exampleMapPrimitive01

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
data class Config(
  val properties: Map<String, String>
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Config.serializer().descriptor))
}
