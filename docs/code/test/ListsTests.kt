// This file was automatically generated from lists.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import dev.adamko.kxstsgen.util.*
import io.kotest.assertions.*
import io.kotest.matchers.*
import io.kotest.matchers.string.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test

class ListsTests {
  @Test
  fun testExampleListPrimitive01() {
    val actual = captureOutput("ExampleListPrimitive01") {
      dev.adamko.kxstsgen.example.exampleListPrimitive01.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export interface MyLists {
          |  strings: string[];
          |  ints: number[];
          |  longs: number[];
          |}
      """.trimMargin()
      .normalize()
    )

    typescriptCompile(actual).asClue { tscOutput ->
      tscOutput.shouldNotBeEmpty()
      tscOutput shouldNotContain "error"
    }
  }

  @Test
  fun testExampleListObjects01() {
    val actual = captureOutput("ExampleListObjects01") {
      dev.adamko.kxstsgen.example.exampleListObjects01.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export interface MyLists {
          |  colours: Colour[];
          |  colourGroups: Colour[][];
          |  colourGroupGroups: Colour[][][];
          |}
          |
          |export interface Colour {
          |  rgb: string;
          |}
      """.trimMargin()
      .normalize()
    )

    typescriptCompile(actual).asClue { tscOutput ->
      tscOutput.shouldNotBeEmpty()
      tscOutput shouldNotContain "error"
    }
  }

  @Test
  fun testExampleListObjects02() {
    val actual = captureOutput("ExampleListObjects02") {
      dev.adamko.kxstsgen.example.exampleListObjects02.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export interface MyLists {
          |  listOfMaps: { [key: string]: number }[];
          |  listOfColourMaps: { [key: string]: Colour }[];
          |}
          |
          |export interface Colour {
          |  rgb: string;
          |}
      """.trimMargin()
      .normalize()
    )

    typescriptCompile(actual).asClue { tscOutput ->
      tscOutput.shouldNotBeEmpty()
      tscOutput shouldNotContain "error"
    }
  }
}
