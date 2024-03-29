// This file was automatically generated from value-classes.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleValueClasses03

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

import dev.adamko.kxstsgen.KxsTsConfig.TypeAliasTypingConfig.BrandTyping
import kotlinx.serialization.builtins.serializer

fun main() {

  val tsConfig = KxsTsConfig(typeAliasTyping = BrandTyping)

  val tsGenerator = KxsTsGenerator(config = tsConfig)
  println(
    tsGenerator.generate(
      ULong.serializer(),
    )
  )
}
