package dev.adamko.kxstsgen.core

import dev.adamko.kxstsgen.KxsTsConfig
import dev.adamko.kxstsgen.KxsTsGenerator
import dev.adamko.kxstsgen.KxsTsRequired
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.Serializable

class KxsTsGeneratorTest : FunSpec({
  val config = KxsTsConfig(indent = "  ")
  val tsGenerator = KxsTsGenerator(config)

  test("Can make default-valued properties required") {
    @Serializable
    data class OptionalTest(
      val optional: Boolean = false,
      @KxsTsRequired
      val required: Boolean = true,
    )

    tsGenerator.generate(OptionalTest.serializer()) shouldBe """
      |export interface OptionalTest {
      |  optional?: boolean;
      |  required: boolean;
      |}
    """.trimMargin()
  }

  test("Can make nullable properties required") {
    @Serializable
    data class OptionalTest(
      @KxsTsRequired
      val required: Boolean? = null,
      val optional: Boolean? = null,
    )

    tsGenerator.generate(OptionalTest.serializer()) shouldBe """
      |export interface OptionalTest {
      |  required: boolean | null;
      |  optional?: boolean | null;
      |}
    """.trimMargin()
  }
})
