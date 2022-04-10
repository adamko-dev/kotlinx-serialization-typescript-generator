// This file was automatically generated from basic-classes.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import dev.adamko.kxstsgen.util.*
import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import kotlinx.knit.test.*

class BasicClassesTest : FunSpec({

  tags(Knit)

  context("ExamplePlainClassSingleField01") {
    val actual = captureOutput("ExamplePlainClassSingleField01") {
      dev.adamko.kxstsgen.example.examplePlainClassSingleField01.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export interface Color {
          |  rgb: number;
          |}
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile()
    }
  }

  context("ExamplePlainClassPrimitiveFields01") {
    val actual = captureOutput("ExamplePlainClassPrimitiveFields01") {
      dev.adamko.kxstsgen.example.examplePlainClassPrimitiveFields01.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export interface SimpleTypes {
          |  aString: string;
          |  anInt: number;
          |  aDouble: number;
          |  bool: boolean;
          |  privateMember: string;
          |}
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile()
    }
  }

  context("ExamplePlainDataClass01") {
    val actual = captureOutput("ExamplePlainDataClass01") {
      dev.adamko.kxstsgen.example.examplePlainDataClass01.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export interface SomeDataClass {
          |  aString: string;
          |  anInt: number;
          |  aDouble: number;
          |  bool: boolean;
          |  privateMember: string;
          |}
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile()
    }
  }

  context("ExamplePlainClassPrimitiveFields02") {
    val actual = captureOutput("ExamplePlainClassPrimitiveFields02") {
      dev.adamko.kxstsgen.example.examplePlainClassPrimitiveFields02.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export interface SimpleTypes {
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
