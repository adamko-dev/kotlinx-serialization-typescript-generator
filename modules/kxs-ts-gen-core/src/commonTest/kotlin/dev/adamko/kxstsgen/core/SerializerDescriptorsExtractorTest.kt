package dev.adamko.kxstsgen.core

import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

class SerializerDescriptorsExtractorTest : FunSpec({

  test("given parent class, expect subclass property descriptor extracted") {

    val expected = listOf(
      Parent.serializer().descriptor,
      Nested.serializer().descriptor,
      String.serializer().descriptor,
    )

    val actual = SerializerDescriptorsExtractor.Default(Parent.serializer())

    withClue(
      """
        expected: ${expected.map { it.serialName }.sorted().joinToString()}
        actual:   ${actual.map { it.serialName }.sorted().joinToString()}
      """.trimIndent()
    ) {
      actual shouldContainExactlyInAnyOrder expected
    }

  }

})

@Serializable
class Nested(val x: String)

@Serializable
private sealed class Parent

@Serializable
private class SubClass(val n: Nested) : Parent()
