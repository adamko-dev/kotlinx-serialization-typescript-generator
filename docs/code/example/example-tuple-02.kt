// This file was automatically generated from tuples.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleTuple02

import dev.adamko.kxstsgen.*
import dev.adamko.kxstsgen.core.experiments.TupleSerializer
import kotlinx.serialization.*

@Serializable(with = OptionalFields.Serializer::class)
data class OptionalFields(
  val requiredString: String,
  val optionalString: String = "",
  val nullableString: String?,
  val nullableOptionalString: String? = "",
) {
  object Serializer : TupleSerializer<OptionalFields>(
    "OptionalFields",
    {
      element(OptionalFields::requiredString)
      element(OptionalFields::optionalString)
      element(OptionalFields::nullableString)
      element(OptionalFields::nullableOptionalString)
    }
  ) {
    override fun tupleConstructor(elements: Iterator<*>): OptionalFields {
      val iter = elements.iterator()
      return OptionalFields(
        iter.next() as String,
        iter.next() as String,
        iter.next() as String,
        iter.next() as String,
      )
    }
  }
}

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(OptionalFields.serializer()))
}
