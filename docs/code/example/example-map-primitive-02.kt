// This file was automatically generated from maps.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleMapPrimitive02

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
class Application(
  val settings: Map<SettingKeys, String>
)

@Serializable
enum class SettingKeys {
  SCREEN_SIZE,
  MAX_MEMORY,
}

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Application.serializer()))
}
