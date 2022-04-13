// This file was automatically generated from customising-output.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleCustomisingOutput02

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

import kotlinx.serialization.builtins.serializer
import dev.adamko.kxstsgen.core.*

@Serializable
data class ItemHolder(
  val item: Item,
)

@Serializable
data class Item(
  val count: UInt? = 0u,
)

fun main() {
  val tsGenerator = KxsTsGenerator()

  tsGenerator.descriptorOverrides +=
    UInt.serializer().descriptor to TsDeclaration.TsTypeAlias(
      id = TsElementId("kotlin.UInt"),
      typeRef = TsTypeRef.Declaration(id = TsElementId("uint"), parent = null, nullable = false)
    )

  println(tsGenerator.generate(ItemHolder.serializer()))
}

