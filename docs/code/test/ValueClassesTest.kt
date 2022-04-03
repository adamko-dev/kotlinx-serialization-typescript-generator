// This file was automatically generated from value-classes.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import io.kotest.matchers.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test
import dev.adamko.kxstsgen.util.*

class ValueClassesTest {
  @Test
  fun testExampleValueClasses01() {
    captureOutput("ExampleValueClasses01") {
      dev.adamko.kxstsgen.example.exampleValueClasses01.main()
    }.normalizeJoin()
      .shouldBe(
        // language=TypeScript
        """
          |export type AuthToken = string;
        """.trimMargin()
          .normalize()
      )
  }

  @Test
  fun testExampleValueClasses02() {
    captureOutput("ExampleValueClasses02") {
      dev.adamko.kxstsgen.example.exampleValueClasses02.main()
    }.normalizeJoin()
      .shouldBe(
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
  }

  @Test
  fun testExampleValueClasses03() {
    captureOutput("ExampleValueClasses03") {
      dev.adamko.kxstsgen.example.exampleValueClasses03.main()
    }.normalizeJoin()
      .shouldBe(
        // language=TypeScript
        """
          |export type ULong = number & { __ULong__: void };
        """.trimMargin()
          .normalize()
      )
  }

  @Test
  fun testExampleValueClasses04() {
    captureOutput("ExampleValueClasses04") {
      dev.adamko.kxstsgen.example.exampleValueClasses04.main()
    }.normalizeJoin()
      .shouldBe(
        // language=TypeScript
        """
          |export type UserCount = UInt;
          |
          |export type UInt = number;
        """.trimMargin()
          .normalize()
      )
  }
}
