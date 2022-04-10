// This file was automatically generated from maps.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import dev.adamko.kxstsgen.util.*
import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import kotlinx.knit.test.*

class MapsTests : FunSpec({

  tags(Knit)

  context("ExampleMapPrimitive01") {
    val actual = captureOutput("ExampleMapPrimitive01") {
      dev.adamko.kxstsgen.example.exampleMapPrimitive01.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export interface Config {
          |  properties: { [key: string]: string };
          |}
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile()
    }
  }

  context("ExampleMapPrimitive02") {
    val actual = captureOutput("ExampleMapPrimitive02") {
      dev.adamko.kxstsgen.example.exampleMapPrimitive02.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
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
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile()
    }
  }

  context("ExampleMapPrimitive03") {
    val actual = captureOutput("ExampleMapPrimitive03") {
      dev.adamko.kxstsgen.example.exampleMapPrimitive03.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export interface MapsWithLists {
          |  mapOfLists: { [key: string]: string[] };
          |}
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile()
    }
  }

  context("ExampleMapPrimitive04") {
    val actual = captureOutput("ExampleMapPrimitive04") {
      dev.adamko.kxstsgen.example.exampleMapPrimitive04.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
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
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile()
    }
  }

  context("ExampleMapPrimitive05") {
    val actual = captureOutput("ExampleMapPrimitive05") {
      dev.adamko.kxstsgen.example.exampleMapPrimitive05.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export interface Config {
          |  nullableVals: { [key: string]: string | null };
          |  // [key: string | null] is not allowed
          |  nullableKeys: Map<string | null, string>;
          |}
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile()
    }
  }

  context("ExampleMapPrimitive06") {
    val actual = captureOutput("ExampleMapPrimitive06") {
      dev.adamko.kxstsgen.example.exampleMapPrimitive06.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export interface Example {
          |  complex: Map<ComplexKey, string>;
          |  simple: { [key: SimpleKey]: string };
          |  doubleSimple: { [key: DoubleSimpleKey]: string };
          |}
          |
          |export interface ComplexKey {
          |  complex: string;
          |}
          |
          |export type SimpleKey = string;
          |
          |export type DoubleSimpleKey = SimpleKey;
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile()
    }
  }

  context("ExampleMapComplex01") {
    val actual = captureOutput("ExampleMapComplex01") {
      dev.adamko.kxstsgen.example.exampleMapComplex01.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
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
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile()
    }
  }

  context("ExampleMapComplex02") {
    val actual = captureOutput("ExampleMapComplex02") {
      dev.adamko.kxstsgen.example.exampleMapComplex02.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export interface CanvasProperties {
          |  colourNames: { [key: ColourMapKey]: string };
          |}
          |
          |export type ColourMapKey = string;
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile()
    }
  }

  context("ExampleMapComplex03") {
    val actual = captureOutput("ExampleMapComplex03") {
      dev.adamko.kxstsgen.example.exampleMapComplex03.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export interface CanvasProperties {
          |  colourNames: { [key: string]: string };
          |}
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile()
    }
  }
})
