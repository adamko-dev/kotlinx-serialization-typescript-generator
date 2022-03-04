// This file was automatically generated from value-classes.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleValueClasses04

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
@JvmInline
value class UserCount(private val count: UInt)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(UserCount.serializer().descriptor))
}
