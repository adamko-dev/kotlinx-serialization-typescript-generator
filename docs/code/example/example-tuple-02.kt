// This file was automatically generated from tuples.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleTuple02

import dev.adamko.kxstsgen.*
import dev.adamko.kxstsgen.core.experiments.TupleSerializer
import kotlinx.serialization.*

@Serializable(with = PostalAddressUSA.Serializer::class)
data class PostalAddressUSA(
  @SerialName("num") // 'SerialName' will be ignored in 'Tuple' form
  val houseNumber: String,
  val streetName: String,
  val postcode: String,
) {
  object Serializer : TupleSerializer<PostalAddressUSA>(
    "PostalAddressUSA",
    {
      element(PostalAddressUSA::houseNumber)
      // custom labels for 'streetName', 'postcode'
      element("street", PostalAddressUSA::streetName)
      element("zip", PostalAddressUSA::postcode)
    }
  ) {
    override fun tupleConstructor(elements: Iterator<*>): PostalAddressUSA {
      return PostalAddressUSA(
        elements.next() as String,
        elements.next() as String,
        elements.next() as String,
      )
    }
  }
}

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(PostalAddressUSA.serializer()))
}
