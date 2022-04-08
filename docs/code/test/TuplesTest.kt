// This file was automatically generated from tuples.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import io.kotest.matchers.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test
import dev.adamko.kxstsgen.util.*

class TuplesTest {
  @Test
  fun testExampleTuple01() {
    captureOutput("ExampleTuple01") {
      dev.adamko.kxstsgen.example.exampleTuple01.main()
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
  fun testExampleTuple02() {
    captureOutput("ExampleTuple02") {
      dev.adamko.kxstsgen.example.exampleTuple02.main()
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
  fun testExampleTuple03() {
    captureOutput("ExampleTuple03") {
      dev.adamko.kxstsgen.example.exampleTuple03.main()
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
  fun testExampleTuple04() {
    captureOutput("ExampleTuple04") {
      dev.adamko.kxstsgen.example.exampleTuple04.main()
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
