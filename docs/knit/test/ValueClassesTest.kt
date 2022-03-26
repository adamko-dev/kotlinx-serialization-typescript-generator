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
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |type AuthToken = string;
        """.trimMargin()
      )
  }

  @Test
  fun testExampleValueClasses02() {
    captureOutput("ExampleValueClasses02") {
      dev.adamko.kxstsgen.example.exampleValueClasses02.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |type UByte = number;
          |
          |type UShort = number;
          |
          |type UInt = number;
          |
          |type ULong = number;
        """.trimMargin()
      )
  }

  @Test
  fun testExampleValueClasses03() {
    captureOutput("ExampleValueClasses03") {
      dev.adamko.kxstsgen.example.exampleValueClasses03.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |type ULong = number & { __ULong__: void };
        """.trimMargin()
      )
  }

  @Test
  fun testExampleValueClasses04() {
    captureOutput("ExampleValueClasses04") {
      dev.adamko.kxstsgen.example.exampleValueClasses04.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |type UserCount = UInt;
          |
          |type UInt = number;
        """.trimMargin()
      )
  }
}
