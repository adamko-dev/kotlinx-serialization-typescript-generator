// This file was automatically generated from maps.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleMapPrimitive06

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
data class Example(
  val complex: Map<ComplexKey, String>,
  val simple: Map<SimpleKey, String>,
  val doubleSimple: Map<DoubleSimpleKey, String>,
  val enum: Map<EnumKey, String>,
  val doubleEnum: Map<DoubleEnumKey, String>,
)

@Serializable
data class ComplexKey(val complex: String)

@Serializable
@JvmInline
value class SimpleKey(val simple: String)

@Serializable
@JvmInline
value class DoubleSimpleKey(val simple: SimpleKey)

@Serializable
enum class ExampleEnum { A, B, C, }

@Serializable
@JvmInline
value class EnumKey(val e: ExampleEnum)

@Serializable
@JvmInline
value class DoubleEnumKey(val e: ExampleEnum)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Example.serializer()))
}
