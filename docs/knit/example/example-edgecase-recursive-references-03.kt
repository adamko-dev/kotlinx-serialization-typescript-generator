// This file was automatically generated from edgecases.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleEdgecaseRecursiveReferences03

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
class A(
  val map: Map<String, B>
)

@Serializable
class B(
  val map: Map<String, A>
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(A.serializer(), B.serializer()))
}
