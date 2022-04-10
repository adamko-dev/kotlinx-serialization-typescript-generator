// This file was automatically generated from tuples.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleTuple05

import dev.adamko.kxstsgen.*
import dev.adamko.kxstsgen.core.experiments.TupleSerializer
import kotlinx.serialization.*

import dev.adamko.kxstsgen.example.exampleTuple04.Coordinates

@Serializable
class GameLocations(
  val homeLocation: Coordinates,
  val allLocations: List<Coordinates>,
  val namedLocations: Map<String, Coordinates>,
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(GameLocations.serializer()))
}
