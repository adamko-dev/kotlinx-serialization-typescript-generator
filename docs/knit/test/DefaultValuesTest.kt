// This file was automatically generated from default-values.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import io.kotest.matchers.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test
import dev.adamko.kxstsgen.util.*

class DefaultValuesTest {
  @Test
  fun testExampleDefaultValuesSingleField01() {
    captureOutput("ExampleDefaultValuesSingleField01") {
      dev.adamko.kxstsgen.example.exampleDefaultValuesSingleField01.main()
    }.normalizeJoin()
      .shouldBe(
        // language=TypeScript
        """
          |export interface Colour {
          |  rgb?: number;
          |}
        """.trimMargin()
          .normalize()
      )
  }

  @Test
  fun testExampleDefaultValuesSingleField02() {
    captureOutput("ExampleDefaultValuesSingleField02") {
      dev.adamko.kxstsgen.example.exampleDefaultValuesSingleField02.main()
    }.normalizeJoin()
      .shouldBe(
        // language=TypeScript
        """
          |export interface Colour {
          |  rgb: number | null;
          |}
        """.trimMargin()
          .normalize()
      )
  }

  @Test
  fun testExampleDefaultValuesPrimitiveFields01() {
    captureOutput("ExampleDefaultValuesPrimitiveFields01") {
      dev.adamko.kxstsgen.example.exampleDefaultValuesPrimitiveFields01.main()
    }.normalizeJoin()
      .shouldBe(
        // language=TypeScript
        """
          |export interface ContactDetails {
          |  name: string;
          |  email: string | null;
          |  active?: boolean;
          |  phoneNumber?: string | null;
          |}
        """.trimMargin()
          .normalize()
      )
  }
}
