// This file was automatically generated from enums.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import io.kotest.matchers.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test

class EnumClassTest {
  @Test
  fun testExampleEnumClass01() {
    captureOutput("ExampleEnumClass01") {
      dev.adamko.kxstsgen.example.exampleEnumClass01.main()
    }.joinToString("\n") { it.ifBlank { "" } }
      .shouldBe(
        // language=TypeScript
        """
          |export enum SomeType {
          |  Alpha = "Alpha",
          |  Beta = "Beta",
          |  Gamma = "Gamma",
          |}
        """.trimMargin()
          .lines()
          .joinToString("\n") { it.ifBlank { "" } }
      )
  }

  @Test
  fun testExampleEnumClass02() {
    captureOutput("ExampleEnumClass02") {
      dev.adamko.kxstsgen.example.exampleEnumClass02.main()
    }.joinToString("\n") { it.ifBlank { "" } }
      .shouldBe(
        // language=TypeScript
        """
          |export enum SomeType2 {
          |  Alpha = "Alpha",
          |  Beta = "Beta",
          |  Gamma = "Gamma",
          |}
        """.trimMargin()
          .lines()
          .joinToString("\n") { it.ifBlank { "" } }
      )
  }
}
