// This file was automatically generated from default-values.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleDefaultValuesPrimitiveFields01

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
data class ContactDetails(
  val email: String?,
  val phoneNumber: String? = null,
  val active: Boolean? = true,
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(ContactDetails.serializer()))
}
