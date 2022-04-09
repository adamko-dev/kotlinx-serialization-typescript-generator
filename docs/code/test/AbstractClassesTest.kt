// This file was automatically generated from abstract-classes.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import dev.adamko.kxstsgen.util.*
import io.kotest.assertions.*
import io.kotest.matchers.*
import io.kotest.matchers.string.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test

class AbstractClassesTest {
  @Test
  fun testExampleAbstractClassSingleField01() {
    val actual = captureOutput("ExampleAbstractClassSingleField01") {
      dev.adamko.kxstsgen.example.exampleAbstractClassSingleField01.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export type Color = any;
          |// interface Color {
          |//   rgb: number;
          |// }
      """.trimMargin()
      .normalize()
    )

    typescriptCompile(actual).asClue { tscOutput ->
      tscOutput.shouldNotBeEmpty()
      tscOutput shouldNotContain "error"
    }
  }

  @Test
  fun testExampleAbstractClassPrimitiveFields01() {
    val actual = captureOutput("ExampleAbstractClassPrimitiveFields01") {
      dev.adamko.kxstsgen.example.exampleAbstractClassPrimitiveFields01.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export type SimpleTypes = any;
          |// export interface SimpleTypes {
          |//   aString: string;
          |//   anInt: number;
          |//   aDouble: number;
          |//   bool: boolean;
          |//   privateMember: string;
          |// }
      """.trimMargin()
      .normalize()
    )

    typescriptCompile(actual).asClue { tscOutput ->
      tscOutput.shouldNotBeEmpty()
      tscOutput shouldNotContain "error"
    }
  }

  @Test
  fun testExampleAbstractClassAbstractField01() {
    val actual = captureOutput("ExampleAbstractClassAbstractField01") {
      dev.adamko.kxstsgen.example.exampleAbstractClassAbstractField01.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export type AbstractSimpleTypes = any;
          |// export interface AbstractSimpleTypes {
          |//   rgb: number;
          |// }
      """.trimMargin()
      .normalize()
    )

    typescriptCompile(actual).asClue { tscOutput ->
      tscOutput.shouldNotBeEmpty()
      tscOutput shouldNotContain "error"
    }
  }
}
