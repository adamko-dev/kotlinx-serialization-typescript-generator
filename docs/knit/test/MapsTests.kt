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
    }.joinToString("\n") { it.ifBlank { "" } }
      .shouldBe(
        // language=TypeScript
        """
          |export interface Config {
          |  properties: { [key: string]: string };
          |}
        """.trimMargin()
          .lines()
          .joinToString("\n") { it.ifBlank { "" } }
      )
  }

  @Test
  fun testExampleMapPrimitive02() {
    captureOutput("ExampleMapPrimitive02") {
      dev.adamko.kxstsgen.example.exampleMapPrimitive02.main()
    }.joinToString("\n") { it.ifBlank { "" } }
      .shouldBe(
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
          .lines()
          .joinToString("\n") { it.ifBlank { "" } }
      )
  }

  @Test
  fun testExampleMapPrimitive03() {
    captureOutput("ExampleMapPrimitive03") {
      dev.adamko.kxstsgen.example.exampleMapPrimitive03.main()
    }.joinToString("\n") { it.ifBlank { "" } }
      .shouldBe(
        // language=TypeScript
        """
          |export interface Config {
          |  properties: { [key: string | null]: string | null };
          |}
        """.trimMargin()
          .lines()
          .joinToString("\n") { it.ifBlank { "" } }
      )
  }

  @Test
  fun testExampleMapComplex01() {
    captureOutput("ExampleMapComplex01") {
      dev.adamko.kxstsgen.example.exampleMapComplex01.main()
    }.joinToString("\n") { it.ifBlank { "" } }
      .shouldBe(
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
          .lines()
          .joinToString("\n") { it.ifBlank { "" } }
      )
  }
}
