// This file was automatically generated from polymorphism.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package example.exampleClosedPolymorphicStaticTypes02

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
abstract class Project {
  abstract val name: String
}

@Serializable
class OwnedProject(override val name: String, val owner: String) : Project()

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(OwnedProject.serializer().descriptor))
}
