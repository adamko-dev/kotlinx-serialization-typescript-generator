// This file was automatically generated from value-classes.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import dev.adamko.kxstsgen.util.*
import io.kotest.assertions.*
import io.kotest.matchers.*
import io.kotest.matchers.string.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test

class ValueClassesTest {
  @Test
  fun testExampleValueClasses01() {
    val actual = captureOutput("ExampleValueClasses01") {
      dev.adamko.kxstsgen.example.exampleValueClasses01.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export type AuthToken = string;
      """.trimMargin()
      .normalize()
    )

    typescriptCompile(actual).asClue { tscOutput ->
      tscOutput.shouldNotBeEmpty()
      tscOutput shouldNotContain "error"
    }
  }

  @Test
  fun testExampleValueClasses02() {
    val actual = captureOutput("ExampleValueClasses02") {
      dev.adamko.kxstsgen.example.exampleValueClasses02.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export type UByte = number;
          |
          |export type UShort = number;
          |
          |export type UInt = number;
          |
          |export type ULong = number;
      """.trimMargin()
      .normalize()
    )

    typescriptCompile(actual).asClue { tscOutput ->
      tscOutput.shouldNotBeEmpty()
      tscOutput shouldNotContain "error"
    }
  }

  @Test
  fun testExampleValueClasses03() {
    val actual = captureOutput("ExampleValueClasses03") {
      dev.adamko.kxstsgen.example.exampleValueClasses03.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export type ULong = number & { __ULong__: void };
      """.trimMargin()
      .normalize()
    )

    typescriptCompile(actual).asClue { tscOutput ->
      tscOutput.shouldNotBeEmpty()
      tscOutput shouldNotContain "error"
    }
  }

  @Test
  fun testExampleValueClasses04() {
    val actual = captureOutput("ExampleValueClasses04") {
      dev.adamko.kxstsgen.example.exampleValueClasses04.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export type UserCount = UInt;
          |
          |export type UInt = number;
      """.trimMargin()
      .normalize()
    )

    typescriptCompile(actual).asClue { tscOutput ->
      tscOutput.shouldNotBeEmpty()
      tscOutput shouldNotContain "error"
    }
  }
}
