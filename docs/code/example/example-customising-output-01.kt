// This file was automatically generated from customising-output.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleCustomisingOutput01

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

import kotlinx.serialization.builtins.serializer
import dev.adamko.kxstsgen.core.*

@Serializable
data class Item(
  val price: Double,
  val count: Int,
)

fun main() {
  val tsGenerator = KxsTsGenerator()

  tsGenerator.descriptorOverrides +=
    Double.serializer().descriptor to TsDeclaration.TsTypeAlias(
      id = TsElementId("Double"),
      typeRef = TsTypeRef.Declaration(
        id = TsElementId("double"),
        parent = null,
        nullable = false,
      )
    )

  println(tsGenerator.generate(Item.serializer()))
}
