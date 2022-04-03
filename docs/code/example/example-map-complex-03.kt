// This file was automatically generated from maps.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleMapComplex03

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable(with = ColourAsStringSerializer::class)
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
object ColourAsStringSerializer : KSerializer<Colour> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("Colour", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, value: Colour) {
    encoder.encodeString(
      listOf(
        value.r,
        value.g,
        value.b,
        value.a,
      ).joinToString("") {
        it.toString(16).padStart(2, '0')
      }
    )
  }

  override fun deserialize(decoder: Decoder): Colour {
    val string = decoder.decodeString()
    val (r, g, b, a) = string.chunked(2).map { it.toUByte(16) }
    return Colour(r, g, b, a)
  }
}

@Serializable
data class CanvasProperties(
  val colourNames: Map<Colour, String>
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(CanvasProperties.serializer()))
}
