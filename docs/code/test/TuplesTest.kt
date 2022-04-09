// This file was automatically generated from tuples.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import dev.adamko.kxstsgen.util.*
import io.kotest.assertions.*
import io.kotest.matchers.*
import io.kotest.matchers.string.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test

class TuplesTest {
  @Test
  fun testExampleTuple01() {
    val actual = captureOutput("ExampleTuple01") {
      dev.adamko.kxstsgen.example.exampleTuple01.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export type SimpleTypes = [string, number, number | null, boolean, string];
      """.trimMargin()
      .normalize()
    )

    typescriptCompile(actual).asClue { tscOutput ->
      tscOutput.shouldNotBeEmpty()
      tscOutput shouldNotContain "error"
    }
  }

  @Test
  fun testExampleTuple02() {
    val actual = captureOutput("ExampleTuple02") {
      dev.adamko.kxstsgen.example.exampleTuple02.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export type OptionalFields = [string, string, string | null, string | null];
      """.trimMargin()
      .normalize()
    )

    typescriptCompile(actual).asClue { tscOutput ->
      tscOutput.shouldNotBeEmpty()
      tscOutput shouldNotContain "error"
    }
  }

  @Test
  fun testExampleTuple03() {
    val actual = captureOutput("ExampleTuple03") {
      dev.adamko.kxstsgen.example.exampleTuple03.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export type Coordinates = [number, number, number];
      """.trimMargin()
      .normalize()
    )

    typescriptCompile(actual).asClue { tscOutput ->
      tscOutput.shouldNotBeEmpty()
      tscOutput shouldNotContain "error"
    }
  }

  @Test
  fun testExampleTuple04() {
    val actual = captureOutput("ExampleTuple04") {
      dev.adamko.kxstsgen.example.exampleTuple04.main()
    }.normalizeJoin()

    actual.shouldBe(
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

    typescriptCompile(actual).asClue { tscOutput ->
      tscOutput.shouldNotBeEmpty()
      tscOutput shouldNotContain "error"
    }
  }
}
