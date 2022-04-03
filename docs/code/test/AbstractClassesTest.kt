// This file was automatically generated from abstract-classes.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import io.kotest.matchers.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test
import dev.adamko.kxstsgen.util.*

class AbstractClassesTest {
  @Test
  fun testExampleAbstractClassSingleField01() {
    captureOutput("ExampleAbstractClassSingleField01") {
      dev.adamko.kxstsgen.example.exampleAbstractClassSingleField01.main()
    }.normalizeJoin()
      .shouldBe(
        // language=TypeScript
        """
          |export type Color = any;
          |// interface Color {
          |//   rgb: number;
          |// }
        """.trimMargin()
          .normalize()
      )
  }

  @Test
  fun testExampleAbstractClassPrimitiveFields01() {
    captureOutput("ExampleAbstractClassPrimitiveFields01") {
      dev.adamko.kxstsgen.example.exampleAbstractClassPrimitiveFields01.main()
    }.normalizeJoin()
      .shouldBe(
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
  }

  @Test
  fun testExampleAbstractClassAbstractField01() {
    captureOutput("ExampleAbstractClassAbstractField01") {
      dev.adamko.kxstsgen.example.exampleAbstractClassAbstractField01.main()
    }.normalizeJoin()
      .shouldBe(
        // language=TypeScript
        """
          |export type AbstractSimpleTypes = any;
          |// export interface AbstractSimpleTypes {
          |//   rgb: number;
          |// }
        """.trimMargin()
          .normalize()
      )
  }
}
