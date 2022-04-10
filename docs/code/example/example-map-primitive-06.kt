// This file was automatically generated from maps.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleMapPrimitive06

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
data class ComplexKey(val complex: String)

@Serializable
@JvmInline
value class SimpleKey(val simple: String)

@Serializable
@JvmInline
value class DoubleSimpleKey(val simple: SimpleKey)

@Serializable
data class Example(
  val complex: Map<ComplexKey, String>,
  val simple: Map<SimpleKey, String>,
  val doubleSimple: Map<DoubleSimpleKey, String>,
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Example.serializer()))
}
