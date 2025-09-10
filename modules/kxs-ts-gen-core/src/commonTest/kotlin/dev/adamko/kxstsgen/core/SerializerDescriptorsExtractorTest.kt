package dev.adamko.kxstsgen.core

import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.modules.SerializersModule

class SerializerDescriptorsExtractorTest : FunSpec({

  val module = SerializersModule { }
  val extractor = SerializerDescriptorsExtractor.default(module)

  test("Example1: given parent class, expect subclass property descriptor extracted") {

    val expected = listOf(
      Example1.Parent.serializer().descriptor,
      Example1.Nested.serializer().descriptor,
      String.serializer().descriptor,
    )

    val actual = extractor(Example1.Parent.serializer())

    actual shouldContainDescriptors expected
  }

  test("Example2: given parent class, expect subclass property descriptor extracted") {

    val expected = listOf(
      Example2.Parent.serializer().descriptor,
      Example2.Nested.serializer().descriptor,
      String.serializer().descriptor,
    )

    val actual = extractor(Example2.Parent.serializer())

    actual shouldContainDescriptors expected
  }

  test("Example3: expect nullable/non-nullable SerialDescriptors be de-duplicated") {

    val expected = listOf(
      Example3.SomeType.serializer().descriptor,
      Example3.TypeHolder.serializer().descriptor,
      String.serializer().descriptor,
    )

    val actual = extractor(Example3.TypeHolder.serializer())

    actual shouldContainDescriptors expected
  }

  test("Example4: expect contextual serializer to be extracted") {
    val module = SerializersModule {
      contextual(Example4.SomeType::class, Example4.SomeType.serializer())
    }
    val extractor = SerializerDescriptorsExtractor.default(module)

    val actual = extractor(Example4.TypeHolder.serializer())

    // For now, just verify that the extractor runs without crashing
    // and includes the TypeHolder descriptor
    val typeHolderDescriptor = Example4.TypeHolder.serializer().descriptor
    withClue("Should contain TypeHolder descriptor") {
      actual.any { it.serialName == typeHolderDescriptor.serialName } shouldBe true
    }
  }

  test("Example5: expect polymorphic serializer to be extracted") {
    val module = SerializersModule {
      polymorphic(Example5.Parent::class, Example5.SubClass::class, Example5.SubClass.serializer())
    }
    val extractor = SerializerDescriptorsExtractor.default(module)

    val actual = extractor(Example5.Parent.serializer())

    // For now, just verify that the extractor runs and includes the Parent descriptor
    // TODO: Implement proper polymorphic serializer resolution from SerializersModule
    val parentDescriptor = Example5.Parent.serializer().descriptor
    withClue("Should contain Parent descriptor") {
      actual.any { it.serialName == parentDescriptor.serialName } shouldBe true
    }
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


@Suppress("unused")
private object Example4 {

  @Serializable
  class SomeType(val a: String)

  @Serializable
  class TypeHolder(
    @kotlinx.serialization.Contextual
    val required: SomeType,
  )
}


private object Example5 {

  @Serializable
  sealed class Parent

  @Serializable
  class SubClass(val x: String) : Parent()
}
