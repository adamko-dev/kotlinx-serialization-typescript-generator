// This file was automatically generated from edgecases.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package dev.adamko.kxstsgen.example.exampleEdgecaseRecursiveReferences01

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
class A(val b: B)

@Serializable
class B(val a: A)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(A.serializer(), B.serializer()))
}
