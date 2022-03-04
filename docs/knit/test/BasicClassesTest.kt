// This file was automatically generated from basic-classes.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import io.kotest.matchers.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test

class BasicClassesTest {
  @Test
  fun testExamplePlainClassSingleField01() {
    captureOutput("ExamplePlainClassSingleField01") {
      dev.adamko.kxstsgen.example.examplePlainClassSingleField01.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |interface Color {
          |  rgb: number;
          |}
        """.trimMargin()
      )
  }

  @Test
  fun testExamplePlainClassPrimitiveFields01() {
    captureOutput("ExamplePlainClassPrimitiveFields01") {
      dev.adamko.kxstsgen.example.examplePlainClassPrimitiveFields01.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |interface SimpleTypes {
          |  aString: string;
          |  anInt: number;
          |  aDouble: number;
          |  bool: boolean;
          |  privateMember: string;
          |}
        """.trimMargin()
      )
  }

  @Test
  fun testExamplePlainDataClass01() {
    captureOutput("ExamplePlainDataClass01") {
      dev.adamko.kxstsgen.example.examplePlainDataClass01.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |interface SomeDataClass {
          |  aString: string;
          |  anInt: number;
          |  aDouble: number;
          |  bool: boolean;
          |  privateMember: string;
          |}
        """.trimMargin()
      )
  }

  @Test
  fun testExamplePlainClassPrimitiveFields02() {
    captureOutput("ExamplePlainClassPrimitiveFields02") {
      dev.adamko.kxstsgen.example.examplePlainClassPrimitiveFields02.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |interface SimpleTypes {
          |}
        """.trimMargin()
      )
  }
}
