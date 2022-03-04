// This file was automatically generated from lists.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleListPrimitive01

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
data class CalendarEvent(
  val attendeeNames: List<String>
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(CalendarEvent.serializer().descriptor))
}
