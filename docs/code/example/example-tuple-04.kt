// This file was automatically generated from tuples.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleTuple04

import dev.adamko.kxstsgen.*
import dev.adamko.kxstsgen.core.experiments.TupleSerializer
import kotlinx.serialization.*

@Serializable(with = Coordinates.Serializer::class)
data class Coordinates(
  val x: Int,
  val y: Int,
  val z: Int,
) {
  object Serializer : TupleSerializer<Coordinates>(
    "Coordinates",
    {
      element(Coordinates::x)
      element(Coordinates::y)
      element(Coordinates::z)
    }
  ) {
    override fun tupleConstructor(elements: Iterator<*>): Coordinates {
      return Coordinates(
        elements.next() as Int,
        elements.next() as Int,
        elements.next() as Int,
      )
    }
  }
}

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Coordinates.serializer()))
}
