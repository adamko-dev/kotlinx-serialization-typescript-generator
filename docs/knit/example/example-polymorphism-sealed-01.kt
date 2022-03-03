// This file was automatically generated from polymorphism.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch", "unused")
package example.examplePolymorphismSealed01

import kotlinx.serialization.*
import dev.adamko.kxstsgen.*

@Serializable
sealed class Project {
  abstract val name: String
}

@Serializable
data class OwnedProject(
  override val name: String,
  val owner: String,
) : Project()

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(
    tsGenerator.generate(
      Project.serializer().descriptor,
      OwnedProject.serializer().descriptor,
    )
  )
}
