package dev.adamko.kxstsgen.core

import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor

class SerializerDescriptorsExtractorTest : FunSpec({

  test("Example1: given parent class, expect subclass property descriptor extracted") {

    val expected = listOf(
      Example1.Parent.serializer().descriptor,
      Example1.Nested.serializer().descriptor,
      String.serializer().descriptor,
    )

    val actual = SerializerDescriptorsExtractor.Default(Example1.Parent.serializer())

    actual shouldContainDescriptors expected
  }

  test("Example2: given parent class, expect subclass property descriptor extracted") {

    val expected = listOf(
      Example2.Parent.serializer().descriptor,
      Example2.Nested.serializer().descriptor,
      String.serializer().descriptor,
    )

    val actual = SerializerDescriptorsExtractor.Default(Example2.Parent.serializer())

    actual shouldContainDescriptors expected
  }

  test("Example3: expect nullable/non-nullable SerialDescriptors be de-duplicated") {

    val expected = listOf(
      Example3.SomeType.serializer().descriptor,
      Example3.TypeHolder.serializer().descriptor,
      String.serializer().descriptor,
    )

    val actual = SerializerDescriptorsExtractor.Default(Example3.TypeHolder.serializer())

    actual shouldContainDescriptors expected
  }
}) {
  companion object {
    private infix fun Collection<SerialDescriptor>.shouldContainDescriptors(expected: Collection<SerialDescriptor>) {
      val actual = this
      withClue(
        """
          expected: ${expected.map { it.serialName }.sorted().joinToString()}
          actual:   ${actual.map { it.serialName }.sorted().joinToString()}
        """.trimIndent()
      ) {
        actual shouldContainExactlyInAnyOrder expected
      }
    }
  }
}


@Suppress("unused")
private object Example1 {
  @Serializable
  class Nested(val x: String)

  @Serializable
  sealed class Parent

  @Serializable
  class SubClass(val n: Nested) : Parent()
}


@Suppress("unused")
private object Example2 {
  @Serializable
  class Nested(val x: String)

  @Serializable
  sealed class Parent

  @Serializable
  sealed class SealedSub : Parent()

  @Serializable
  class SubClass1(val n: Nested) : SealedSub()
}


@Suppress("unused")
private object Example3 {

  @Serializable
  class SomeType(val a: String)

  @Serializable
  class TypeHolder(
    val required: SomeType,
    val optional: SomeType?,
  )
}
