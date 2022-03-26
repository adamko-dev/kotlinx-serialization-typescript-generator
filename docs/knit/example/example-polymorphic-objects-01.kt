// This file was automatically generated from polymorphism.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.examplePolymorphicObjects01

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
sealed class Response

@Serializable
object EmptyResponse : Response()

@Serializable
class TextResponse(val text: String) : Response()

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(
    tsGenerator.generate(
      EmptyResponse.serializer(),
      TextResponse.serializer(),
    )
  )
}
