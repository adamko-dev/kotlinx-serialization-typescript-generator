// This file was automatically generated from value-classes.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import io.kotest.matchers.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test

class ValueClassesTest {
  @Test
  fun testExampleValueClasses01() {
    captureOutput("ExampleValueClasses01") {
      dev.adamko.kxstsgen.example.exampleValueClasses01.main()
    }.joinToString("\n") { it.ifBlank { "" } }
      .shouldBe(
        // language=TypeScript
        """
          |export type AuthToken = string;
        """.trimMargin()
          .lines()
          .joinToString("\n") { it.ifBlank { "" } }
      )
  }

  @Test
  fun testExampleValueClasses02() {
    captureOutput("ExampleValueClasses02") {
      dev.adamko.kxstsgen.example.exampleValueClasses02.main()
    }.joinToString("\n") { it.ifBlank { "" } }
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
          .lines()
          .joinToString("\n") { it.ifBlank { "" } }
      )
  }

  @Test
  fun testExampleValueClasses03() {
    captureOutput("ExampleValueClasses03") {
      dev.adamko.kxstsgen.example.exampleValueClasses03.main()
    }.joinToString("\n") { it.ifBlank { "" } }
      .shouldBe(
        // language=TypeScript
        """
          |export type ULong = number & { __ULong__: void };
        """.trimMargin()
          .lines()
          .joinToString("\n") { it.ifBlank { "" } }
      )
  }

  @Test
  fun testExampleValueClasses04() {
    captureOutput("ExampleValueClasses04") {
      dev.adamko.kxstsgen.example.exampleValueClasses04.main()
    }.joinToString("\n") { it.ifBlank { "" } }
      .shouldBe(
        // language=TypeScript
        """
          |export type UserCount = UInt;
          |
          |export type UInt = number;
        """.trimMargin()
          .lines()
          .joinToString("\n") { it.ifBlank { "" } }
      )
  }
}
