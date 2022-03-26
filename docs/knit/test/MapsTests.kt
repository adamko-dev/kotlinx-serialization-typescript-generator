// This file was automatically generated from maps.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import io.kotest.matchers.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test

class MapsTests {
  @Test
  fun testExampleMapPrimitive01() {
    captureOutput("ExampleMapPrimitive01") {
      dev.adamko.kxstsgen.example.exampleMapPrimitive01.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |interface Config {
          |  properties: { [key: string]: string };
          |}
        """.trimMargin()
      )
  }

  @Test
  fun testExampleMapPrimitive02() {
    captureOutput("ExampleMapPrimitive02") {
      dev.adamko.kxstsgen.example.exampleMapPrimitive02.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |interface Application {
          |  settings: { [key in SettingKeys]: string };
          |}
          |
          |export enum SettingKeys {
          |  SCREEN_SIZE = "SCREEN_SIZE",
          |  MAX_MEMORY = "MAX_MEMORY",
          |}
        """.trimMargin()
      )
  }

  @Test
  fun testExampleMapPrimitive03() {
    captureOutput("ExampleMapPrimitive03") {
      dev.adamko.kxstsgen.example.exampleMapPrimitive03.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |interface Config {
          |  properties: { [key: string | null]: string | null };
          |}
        """.trimMargin()
      )
  }

  @Test
  fun testExampleMapComplex01() {
    captureOutput("ExampleMapComplex01") {
      dev.adamko.kxstsgen.example.exampleMapComplex01.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |interface CanvasProperties {
          |  colourNames: Map<Colour, string>;
          |}
          |
          |interface Colour {
          |  r: UByte;
          |  g: UByte;
          |  b: UByte;
          |  a: UByte;
          |}
          |
          |type UByte = number;
        """.trimMargin()
      )
  }
}
