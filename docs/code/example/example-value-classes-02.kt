// This file was automatically generated from value-classes.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleValueClasses02

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

import kotlinx.serialization.builtins.serializer

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(
    tsGenerator.generate(
      UByte.serializer(),
      UShort.serializer(),
      UInt.serializer(),
      ULong.serializer(),
    )
  )
}
