// This file was automatically generated from default-values.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import dev.adamko.kxstsgen.util.*
import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import kotlinx.knit.test.*

class DefaultValuesTest : FunSpec({

  tags(Knit)
  context("ExampleDefaultValuesSingleField01") {
    val caseName = testCase.name.testName

    val actual = captureOutput(caseName) {
      dev.adamko.kxstsgen.example.exampleDefaultValuesSingleField01.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export interface Colour {
          |  rgb?: number;
          |}
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile(caseName)
    }
  }

  context("ExampleDefaultValuesSingleField02") {
    val caseName = testCase.name.testName

    val actual = captureOutput(caseName) {
      dev.adamko.kxstsgen.example.exampleDefaultValuesSingleField02.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export interface Colour {
          |  rgb: number | null;
          |}
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile(caseName)
    }
  }

  context("ExampleDefaultValuesPrimitiveFields01") {
    val caseName = testCase.name.testName

    val actual = captureOutput(caseName) {
      dev.adamko.kxstsgen.example.exampleDefaultValuesPrimitiveFields01.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export interface ContactDetails {
          |  name: string;
          |  email: string | null;
          |  active?: boolean;
          |  phoneNumber?: string | null;
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
