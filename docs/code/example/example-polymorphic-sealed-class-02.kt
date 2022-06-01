// This file was automatically generated from polymorphism.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.examplePolymorphicSealedClass02

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
sealed class Dog {
  abstract val name: String

  @Serializable
  @SerialName("Dog.Mutt")
  class Mutt(override val name: String, val loveable: Boolean = true) : Dog()

  @Serializable
  sealed class Retriever : Dog() {
    abstract val colour: String

    @Serializable
    @SerialName("Dog.Retriever.Golden")
    data class Golden(
      override val name: String,
      override val colour: String,
      val cute: Boolean = true,
    ) : Retriever()

    @Serializable
    @SerialName("Dog.Retriever.NovaScotia")
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
