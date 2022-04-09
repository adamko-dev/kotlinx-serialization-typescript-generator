// This file was automatically generated from default-values.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import dev.adamko.kxstsgen.util.*
import io.kotest.assertions.*
import io.kotest.matchers.*
import io.kotest.matchers.string.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test

class DefaultValuesTest {
  @Test
  fun testExampleDefaultValuesSingleField01() {
    val actual = captureOutput("ExampleDefaultValuesSingleField01") {
      dev.adamko.kxstsgen.example.exampleDefaultValuesSingleField01.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export interface Colour {
          |  rgb?: number;
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
  fun testExampleDefaultValuesSingleField02() {
    val actual = captureOutput("ExampleDefaultValuesSingleField02") {
      dev.adamko.kxstsgen.example.exampleDefaultValuesSingleField02.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export interface Colour {
          |  rgb: number | null;
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
  fun testExampleDefaultValuesPrimitiveFields01() {
    val actual = captureOutput("ExampleDefaultValuesPrimitiveFields01") {
      dev.adamko.kxstsgen.example.exampleDefaultValuesPrimitiveFields01.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export interface ContactDetails {
          |  name: string;
          |  email: string | null;
          |  active?: boolean;
          |  phoneNumber?: string | null;
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
