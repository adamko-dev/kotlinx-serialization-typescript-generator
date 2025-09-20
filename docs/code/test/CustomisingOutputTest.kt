// This file was automatically generated from customising-output.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import dev.adamko.kxstsgen.util.*
import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import kotlinx.knit.test.*

class CustomisingOutputTest : FunSpec({

  tags(Knit)
  context("ExampleCustomisingOutput01") {
    val caseName = testCase.name.name

    val actual = captureOutput(caseName) {
      dev.adamko.kxstsgen.example.exampleCustomisingOutput01.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        """
          |export interface Item {
          |  price: Double;
          |  count: number;
          |}
          |
          |export type Double = double; // assume that 'double' will be provided by another library
        """.trimMargin()
        .normalize()
      )
    }

    // TS_COMPILE_OFF
    // test("expect actual compiles").config(tags = tsCompile) {
    //   actual.shouldTypeScriptCompile(caseName)
    // }
  }

  context("ExampleCustomisingOutput02") {
    val caseName = testCase.name.name

    val actual = captureOutput(caseName) {
      dev.adamko.kxstsgen.example.exampleCustomisingOutput02.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        """
          |export interface Item {
          |  price: customDouble;
          |  count: number;
          |}
        """.trimMargin()
        .normalize()
      )
    }

    // TS_COMPILE_OFF
    // test("expect actual compiles").config(tags = tsCompile) {
    //   actual.shouldTypeScriptCompile(caseName)
    // }
  }

  context("ExampleCustomisingOutput03") {
    val caseName = testCase.name.name

    val actual = captureOutput(caseName) {
      dev.adamko.kxstsgen.example.exampleCustomisingOutput03.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        """
          |export interface ItemHolder {
          |  item: Item;
          |}
          |
          |export interface Item {
          |  count?: UInt | null;
          |  score?: customInt | null;
          |}
          |
          |export type UInt = uint;
        """.trimMargin()
        .normalize()
      )
    }

    // TS_COMPILE_OFF
    // test("expect actual compiles").config(tags = tsCompile) {
    //   actual.shouldTypeScriptCompile(caseName)
    // }
  }

  context("ExampleCustomisingOutput04") {
    val caseName = testCase.name.name

    val actual = captureOutput(caseName) {
      dev.adamko.kxstsgen.example.exampleCustomisingOutput04.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        """
          |export interface ItemHolder {
          |  item: Item;
          |  tick: Tick | null;
          |  phase: Phase | null;
          |}
          |
          |export interface Item {
          |  count?: UInt | null;
          |  score?: customInt | null;
          |}
          |
          |export type Tick = UInt;
          |
          |export type Phase = customInt;
          |
          |export type UInt = uint;
        """.trimMargin()
        .normalize()
      )
    }

    // TS_COMPILE_OFF
    // test("expect actual compiles").config(tags = tsCompile) {
    //   actual.shouldTypeScriptCompile(caseName)
    // }
  }
})
