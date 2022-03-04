// This file was automatically generated from polymorphism.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package example.examplePolymorphicStaticTypes01

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
open class Project(val name: String)

class OwnedProject(name: String, val owner: String) : Project(name)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Project.serializer().descriptor))
}
