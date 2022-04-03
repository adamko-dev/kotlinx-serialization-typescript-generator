// This file was automatically generated from basic-classes.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import io.kotest.matchers.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test
import dev.adamko.kxstsgen.util.*

class BasicClassesTest {
  @Test
  fun testExamplePlainClassSingleField01() {
    captureOutput("ExamplePlainClassSingleField01") {
      dev.adamko.kxstsgen.example.examplePlainClassSingleField01.main()
    }.normalizeJoin()
      .shouldBe(
        // language=TypeScript
        """
          |export interface Color {
          |  rgb: number;
          |}
        """.trimMargin()
          .normalize()
      )
  }

  @Test
  fun testExamplePlainClassPrimitiveFields01() {
    captureOutput("ExamplePlainClassPrimitiveFields01") {
      dev.adamko.kxstsgen.example.examplePlainClassPrimitiveFields01.main()
    }.normalizeJoin()
      .shouldBe(
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
  }

  @Test
  fun testExamplePlainDataClass01() {
    captureOutput("ExamplePlainDataClass01") {
      dev.adamko.kxstsgen.example.examplePlainDataClass01.main()
    }.normalizeJoin()
      .shouldBe(
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
  }

  @Test
  fun testExamplePlainClassPrimitiveFields02() {
    captureOutput("ExamplePlainClassPrimitiveFields02") {
      dev.adamko.kxstsgen.example.examplePlainClassPrimitiveFields02.main()
    }.normalizeJoin()
      .shouldBe(
        // language=TypeScript
        """
          |export interface SimpleTypes {
          |}
        """.trimMargin()
          .normalize()
      )
  }
}
