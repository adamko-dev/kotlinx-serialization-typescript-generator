// This file was automatically generated from maps.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import io.kotest.matchers.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test
import dev.adamko.kxstsgen.util.*

class MapsTests {
  @Test
  fun testExampleMapPrimitive01() {
    captureOutput("ExampleMapPrimitive01") {
      dev.adamko.kxstsgen.example.exampleMapPrimitive01.main()
    }.normalizeJoin()
      .shouldBe(
        // language=TypeScript
        """
          |export interface Config {
          |  properties: { [key: string]: string };
          |}
        """.trimMargin()
          .normalize()
      )
  }

  @Test
  fun testExampleMapPrimitive02() {
    captureOutput("ExampleMapPrimitive02") {
      dev.adamko.kxstsgen.example.exampleMapPrimitive02.main()
    }.normalizeJoin()
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
          .normalize()
      )
  }

  @Test
  fun testExampleMapPrimitive03() {
    captureOutput("ExampleMapPrimitive03") {
      dev.adamko.kxstsgen.example.exampleMapPrimitive03.main()
    }.normalizeJoin()
      .shouldBe(
        // language=TypeScript
        """
          |export interface Config {
          |  properties: { [key: string | null]: string | null };
          |}
        """.trimMargin()
          .normalize()
      )
  }

  @Test
  fun testExampleMapComplex01() {
    captureOutput("ExampleMapComplex01") {
      dev.adamko.kxstsgen.example.exampleMapComplex01.main()
    }.normalizeJoin()
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
          .normalize()
      )
  }

  @Test
  fun testExampleMapComplex02() {
    captureOutput("ExampleMapComplex02") {
      dev.adamko.kxstsgen.example.exampleMapComplex02.main()
    }.normalizeJoin()
      .shouldBe(
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
  }

  @Test
  fun testExampleMapComplex03() {
    captureOutput("ExampleMapComplex03") {
      dev.adamko.kxstsgen.example.exampleMapComplex03.main()
    }.normalizeJoin()
      .shouldBe(
        // language=TypeScript
        """
          |export interface CanvasProperties {
          |  colourNames: { [key: string]: string };
          |}
        """.trimMargin()
          .normalize()
      )
  }
}
