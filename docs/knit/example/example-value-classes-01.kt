// This file was automatically generated from value-classes.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleValueClasses01

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
@JvmInline
value class AuthToken(private val token: String)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(AuthToken.serializer()))
}
