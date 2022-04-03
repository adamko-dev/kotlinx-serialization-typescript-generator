// This file was automatically generated from polymorphism.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.examplePolymorphicSealedClass02

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
sealed class Dog {
  abstract val name: String

  @Serializable
  class Mutt(override val name: String, val loveable: Boolean = true) : Dog()

  @Serializable
  sealed class Retriever : Dog() {
    abstract val colour: String

    @Serializable
    data class Golden(
      override val name: String,
      override val colour: String,
      val cute: Boolean = true,
    ) : Retriever()

    @Serializable
    data class NovaScotia(
      override val name: String,
      override val colour: String,
      val adorable: Boolean = true,
    ) : Retriever()
  }
}

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Dog.serializer()))
}
