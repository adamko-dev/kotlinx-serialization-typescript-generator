// This file was automatically generated from export-formats.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import io.kotest.matchers.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test
import dev.adamko.kxstsgen.util.*

class TsExportFormatTest {
  @Test
  fun testExampleFormatTuple01() {
    captureOutput("ExampleFormatTuple01") {
      dev.adamko.kxstsgen.example.exampleFormatTuple01.main()
    }.normalizeJoin()
      .shouldBe(
        // language=TypeScript
        """
          |export type SimpleTypes = [string, number, number | null, boolean, string];
        """.trimMargin()
          .normalize()
      )
  }

  @Test
  fun testExampleFormatTuple02() {
    captureOutput("ExampleFormatTuple02") {
      dev.adamko.kxstsgen.example.exampleFormatTuple02.main()
    }.normalizeJoin()
      .shouldBe(
        // language=TypeScript
        """
          |export type OptionalFields = [string, string, string | null, string | null];
        """.trimMargin()
          .normalize()
      )
  }

  @Test
  fun testExampleFormatTuple03() {
    captureOutput("ExampleFormatTuple03") {
      dev.adamko.kxstsgen.example.exampleFormatTuple03.main()
    }.normalizeJoin()
      .shouldBe(
        // language=TypeScript
        """
          |export type Coordinates = [number, number, number];
        """.trimMargin()
          .normalize()
      )
  }

  @Test
  fun testExampleFormatTuple04() {
    captureOutput("ExampleFormatTuple04") {
      dev.adamko.kxstsgen.example.exampleFormatTuple04.main()
    }.normalizeJoin()
      .shouldBe(
        // language=TypeScript
        """
          |export interface GameLocations {
          |  homeLocation: Coordinates;
          |  allLocations: Coordinates[];
          |  namedLocations: { [key: string]: Coordinates };
          |}
          |
          |export type Coordinates = [number, number, number];
        """.trimMargin()
          .normalize()
      )
  }
}
