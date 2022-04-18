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
    val caseName = testCase.name.testName

    val actual = captureOutput(caseName) {
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
      actual.shouldTypeScriptCompile(caseName)
    }
  }

  context("ExampleMapPrimitive02") {
    val caseName = testCase.name.testName

    val actual = captureOutput(caseName) {
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
      actual.shouldTypeScriptCompile(caseName)
    }
  }

  context("ExampleMapPrimitive03") {
    val caseName = testCase.name.testName

    val actual = captureOutput(caseName) {
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
      actual.shouldTypeScriptCompile(caseName)
    }
  }

  context("ExampleMapPrimitive04") {
    val caseName = testCase.name.testName

    val actual = captureOutput(caseName) {
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
      actual.shouldTypeScriptCompile(caseName)
    }
  }

  context("ExampleMapPrimitive05") {
    val caseName = testCase.name.testName

    val actual = captureOutput(caseName) {
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
      actual.shouldTypeScriptCompile(caseName)
    }
  }

  context("ExampleMapPrimitive06") {
    val caseName = testCase.name.testName

    val actual = captureOutput(caseName) {
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
          |  enum: { [key in EnumKey]: string };
          |  doubleEnum: { [key in DoubleEnumKey]: string };
          |}
          |
          |export interface ComplexKey {
          |  complex: string;
          |}
          |
          |export type SimpleKey = string;
          |
          |export type DoubleSimpleKey = SimpleKey;
          |
          |export type EnumKey = ExampleEnum;
          |
          |export type DoubleEnumKey = ExampleEnum;
          |
          |export enum ExampleEnum {
          |  A = "A",
          |  B = "B",
          |  C = "C",
          |}
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile(caseName)
    }
  }

  context("ExampleMapComplex01") {
    val caseName = testCase.name.testName

    val actual = captureOutput(caseName) {
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
      actual.shouldTypeScriptCompile(caseName)
    }
  }

  context("ExampleMapComplex02") {
    val caseName = testCase.name.testName

    val actual = captureOutput(caseName) {
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
      actual.shouldTypeScriptCompile(caseName)
    }
  }

  context("ExampleMapComplex03") {
    val caseName = testCase.name.testName

    val actual = captureOutput(caseName) {
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
      actual.shouldTypeScriptCompile(caseName)
    }
  }
})
