// This file was automatically generated from maps.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package example.test

import io.kotest.matchers.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test

class MapsTests {
  @Test
  fun testExampleMapPrimitive01() {
    captureOutput("ExampleMapPrimitive01") {
      example.exampleMapPrimitive01.main()
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
      example.exampleMapPrimitive02.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |export enum SettingKeys {
          |  SCREEN_SIZE = "SCREEN_SIZE",
          |  MAX_MEMORY = "MAX_MEMORY",
          |}
          |
          |interface Application {
          |  settings: { [key in SettingKeys]: string };
          |}
        """.trimMargin()
      )
  }

  @Test
  fun testExampleMapPrimitive03() {
    captureOutput("ExampleMapPrimitive03") {
      example.exampleMapPrimitive03.main()
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
      example.exampleMapComplex01.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |type UByte = number;
          |
          |interface Colour {
          |  r: UByte;
          |  g: UByte;
          |  b: UByte;
          |  a: UByte;
          |}
          |
          |interface CanvasProperties {
          |  colourNames: Map<Colour, string>;
          |}
        """.trimMargin()
      )
  }
}
