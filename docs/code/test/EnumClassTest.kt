// This file was automatically generated from enums.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import dev.adamko.kxstsgen.util.*
import io.kotest.assertions.*
import io.kotest.matchers.*
import io.kotest.matchers.string.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test

class EnumClassTest {
  @Test
  fun testExampleEnumClass01() {
    val actual = captureOutput("ExampleEnumClass01") {
      dev.adamko.kxstsgen.example.exampleEnumClass01.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export enum SomeType {
          |  Alpha = "Alpha",
          |  Beta = "Beta",
          |  Gamma = "Gamma",
          |}
      """.trimMargin()
      .normalize()
    )

    typescriptCompile(actual).asClue { tscOutput ->
      tscOutput.shouldNotBeEmpty()
      tscOutput shouldNotContain "error"
    }
  }

  @Test
  fun testExampleEnumClass02() {
    val actual = captureOutput("ExampleEnumClass02") {
      dev.adamko.kxstsgen.example.exampleEnumClass02.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export enum SomeType2 {
          |  Alpha = "Alpha",
          |  Beta = "Beta",
          |  Gamma = "Gamma",
          |}
      """.trimMargin()
      .normalize()
    )

    typescriptCompile(actual).asClue { tscOutput ->
      tscOutput.shouldNotBeEmpty()
      tscOutput shouldNotContain "error"
    }
  }
}
