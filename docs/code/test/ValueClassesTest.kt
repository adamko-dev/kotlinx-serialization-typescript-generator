// This file was automatically generated from value-classes.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import dev.adamko.kxstsgen.util.*
import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import kotlinx.knit.test.*

class ValueClassesTest : FunSpec({

  tags(Knit)

  context("ExampleValueClasses01") {
    val actual = captureOutput("ExampleValueClasses01") {
      dev.adamko.kxstsgen.example.exampleValueClasses01.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export type AuthToken = string;
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile()
    }
  }

  context("ExampleValueClasses02") {
    val actual = captureOutput("ExampleValueClasses02") {
      dev.adamko.kxstsgen.example.exampleValueClasses02.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export type UByte = number;
          |
          |export type UShort = number;
          |
          |export type UInt = number;
          |
          |export type ULong = number;
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile()
    }
  }

  context("ExampleValueClasses03") {
    val actual = captureOutput("ExampleValueClasses03") {
      dev.adamko.kxstsgen.example.exampleValueClasses03.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export type ULong = number & { __ULong__: void };
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile()
    }
  }

  context("ExampleValueClasses04") {
    val actual = captureOutput("ExampleValueClasses04") {
      dev.adamko.kxstsgen.example.exampleValueClasses04.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export type UserCount = UInt;
          |
          |export type UInt = number;
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile()
    }
  }
})
