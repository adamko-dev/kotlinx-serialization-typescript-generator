// This file was automatically generated from abstract-classes.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import dev.adamko.kxstsgen.util.*
import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import kotlinx.knit.test.*

class AbstractClassesTest : FunSpec({

  tags(Knit)
  context("ExampleAbstractClassSingleField01") {
    val caseName = testCase.name.name

    val actual = captureOutput(caseName) {
      dev.adamko.kxstsgen.example.exampleAbstractClassSingleField01.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export type Color = any;
          |// interface Color {
          |//   rgb: number;
          |// }
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile(caseName)
    }
  }

  context("ExampleAbstractClassPrimitiveFields01") {
    val caseName = testCase.name.name

    val actual = captureOutput(caseName) {
      dev.adamko.kxstsgen.example.exampleAbstractClassPrimitiveFields01.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export type SimpleTypes = any;
          |// export interface SimpleTypes {
          |//   aString: string;
          |//   anInt: number;
          |//   aDouble: number;
          |//   bool: boolean;
          |//   privateMember: string;
          |// }
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile(caseName)
    }
  }

  context("ExampleAbstractClassAbstractField01") {
    val caseName = testCase.name.name

    val actual = captureOutput(caseName) {
      dev.adamko.kxstsgen.example.exampleAbstractClassAbstractField01.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export type AbstractSimpleTypes = any;
          |// export interface AbstractSimpleTypes {
          |//   rgb: number;
          |// }
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile(caseName)
    }
  }
})
