// This file was automatically generated from maps.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import dev.adamko.kxstsgen.util.*
import io.kotest.assertions.*
import io.kotest.matchers.*
import io.kotest.matchers.string.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test

class MapsTests {
  @Test
  fun testExampleMapPrimitive01() {
    val actual = captureOutput("ExampleMapPrimitive01") {
      dev.adamko.kxstsgen.example.exampleMapPrimitive01.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export interface Config {
          |  properties: { [key: string]: string };
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
  fun testExampleMapPrimitive02() {
    val actual = captureOutput("ExampleMapPrimitive02") {
      dev.adamko.kxstsgen.example.exampleMapPrimitive02.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export interface Application {
          |  settings: { [key in SettingKeys]: string };
          |}
          |
          |export enum SettingKeys {
          |  SCREEN_SIZE = "SCREEN_SIZE",
          |  MAX_MEMORY = "MAX_MEMORY",
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
  fun testExampleMapPrimitive03() {
    val actual = captureOutput("ExampleMapPrimitive03") {
      dev.adamko.kxstsgen.example.exampleMapPrimitive03.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export interface MapsWithLists {
          |  mapOfLists: { [key: string]: string[] };
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
  fun testExampleMapPrimitive04() {
    val actual = captureOutput("ExampleMapPrimitive04") {
      dev.adamko.kxstsgen.example.exampleMapPrimitive04.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export interface MyDataClass {
          |  mapOfLists: { [key: string]: Data };
          |}
          |
          |export type Data = string;
      """.trimMargin()
      .normalize()
    )

    typescriptCompile(actual).asClue { tscOutput ->
      tscOutput.shouldNotBeEmpty()
      tscOutput shouldNotContain "error"
    }
  }

  @Test
  fun testExampleMapPrimitive05() {
    val actual = captureOutput("ExampleMapPrimitive05") {
      dev.adamko.kxstsgen.example.exampleMapPrimitive05.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export interface Config {
          |  properties: { [key: string | null]: string | null };
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
  fun testExampleMapComplex01() {
    val actual = captureOutput("ExampleMapComplex01") {
      dev.adamko.kxstsgen.example.exampleMapComplex01.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export interface CanvasProperties {
          |  colourNames: Map<Colour, string>;
          |}
          |
          |export interface Colour {
          |  r: UByte;
          |  g: UByte;
          |  b: UByte;
          |  a: UByte;
          |}
          |
          |export type UByte = number;
      """.trimMargin()
      .normalize()
    )

    typescriptCompile(actual).asClue { tscOutput ->
      tscOutput.shouldNotBeEmpty()
      tscOutput shouldNotContain "error"
    }
  }

  @Test
  fun testExampleMapComplex02() {
    val actual = captureOutput("ExampleMapComplex02") {
      dev.adamko.kxstsgen.example.exampleMapComplex02.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export interface CanvasProperties {
          |  colourNames: Map<ColourMapKey, string>;
          |}
          |
          |export type ColourMapKey = string;
      """.trimMargin()
      .normalize()
    )

    typescriptCompile(actual).asClue { tscOutput ->
      tscOutput.shouldNotBeEmpty()
      tscOutput shouldNotContain "error"
    }
  }

  @Test
  fun testExampleMapComplex03() {
    val actual = captureOutput("ExampleMapComplex03") {
      dev.adamko.kxstsgen.example.exampleMapComplex03.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export interface CanvasProperties {
          |  colourNames: { [key: string]: string };
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
