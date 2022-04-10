// This file was automatically generated from enums.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import dev.adamko.kxstsgen.util.*
import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import kotlinx.knit.test.*

class EnumClassTest : FunSpec({

  tags(Knit)

  context("ExampleEnumClass01") {
    val actual = captureOutput("ExampleEnumClass01") {
      dev.adamko.kxstsgen.example.exampleEnumClass01.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export enum SomeType {
          |  Alpha = "Alpha",
          |  Beta = "Beta",
          |  Gamma = "Gamma",
          |}
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile()
    }
  }

  context("ExampleEnumClass02") {
    val actual = captureOutput("ExampleEnumClass02") {
      dev.adamko.kxstsgen.example.exampleEnumClass02.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export enum SomeType2 {
          |  Alpha = "Alpha",
          |  Beta = "Beta",
          |  Gamma = "Gamma",
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
