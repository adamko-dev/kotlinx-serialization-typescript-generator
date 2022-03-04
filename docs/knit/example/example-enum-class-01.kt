// This file was automatically generated from enums.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleEnumClass01

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*
import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
enum class SomeType {
  Alpha,
  Beta,
  Gamma
}

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(SomeType.serializer().descriptor))
}
