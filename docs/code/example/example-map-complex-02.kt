// This file was automatically generated from maps.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleMapComplex02

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
data class Colour(
  val r: UByte,
  val g: UByte,
  val b: UByte,
  val a: UByte,
)

/**
 * Encode a [Colour] as an 8-character string
 *
 * Red, green, blue, and alpha are encoded as base-16 strings.
 */
@Serializable
@JvmInline
value class ColourMapKey(private val rgba: String) {
  constructor(colour: Colour) : this(
    listOf(
      colour.r,
      colour.g,
      colour.b,
      colour.a,
    ).joinToString("") {
      it.toString(16).padStart(2, '0')
    }
  )

  fun toColour(): Colour {
    val (r, g, b, a) = rgba.chunked(2).map { it.toUByte(16) }
    return Colour(r, g, b, a)
  }
}

@Serializable
data class CanvasProperties(
  val colourNames: Map<ColourMapKey, String>
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(CanvasProperties.serializer()))
}
