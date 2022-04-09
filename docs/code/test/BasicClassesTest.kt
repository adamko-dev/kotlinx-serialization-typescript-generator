// This file was automatically generated from basic-classes.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import dev.adamko.kxstsgen.util.*
import io.kotest.assertions.*
import io.kotest.matchers.*
import io.kotest.matchers.string.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test

class BasicClassesTest {
  @Test
  fun testExamplePlainClassSingleField01() {
    val actual = captureOutput("ExamplePlainClassSingleField01") {
      dev.adamko.kxstsgen.example.examplePlainClassSingleField01.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export interface Color {
          |  rgb: number;
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
  fun testExamplePlainClassPrimitiveFields01() {
    val actual = captureOutput("ExamplePlainClassPrimitiveFields01") {
      dev.adamko.kxstsgen.example.examplePlainClassPrimitiveFields01.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export interface SimpleTypes {
          |  aString: string;
          |  anInt: number;
          |  aDouble: number;
          |  bool: boolean;
          |  privateMember: string;
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
  fun testExamplePlainDataClass01() {
    val actual = captureOutput("ExamplePlainDataClass01") {
      dev.adamko.kxstsgen.example.examplePlainDataClass01.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export interface SomeDataClass {
          |  aString: string;
          |  anInt: number;
          |  aDouble: number;
          |  bool: boolean;
          |  privateMember: string;
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
  fun testExamplePlainClassPrimitiveFields02() {
    val actual = captureOutput("ExamplePlainClassPrimitiveFields02") {
      dev.adamko.kxstsgen.example.examplePlainClassPrimitiveFields02.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export interface SimpleTypes {
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
