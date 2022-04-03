// This file was automatically generated from default-values.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleDefaultValuesPrimitiveFields01

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
data class ContactDetails(
  // nullable: ❌, optional: ❌
  val name: String,
  // nullable: ✅, optional: ❌
  val email: String?,
  // nullable: ❌, optional: ✅
  val active: Boolean = true,
  // nullable: ✅, optional: ✅
  val phoneNumber: String? = null,
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(ContactDetails.serializer()))
}
