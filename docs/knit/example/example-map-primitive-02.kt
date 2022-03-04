// This file was automatically generated from maps.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleMapPrimitive02

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
enum class SettingKeys {
  SCREEN_SIZE,
  MAX_MEMORY,
}

@Serializable
class Application(
  val settings: Map<SettingKeys, String>
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Application.serializer().descriptor))
}
