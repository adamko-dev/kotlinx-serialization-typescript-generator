// This file was automatically generated from tuples.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import dev.adamko.kxstsgen.util.*
import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import kotlinx.knit.test.*

class TuplesTest : FunSpec({

  tags(Knit)

  context("ExampleTuple01") {
    val actual = captureOutput("ExampleTuple01") {
      dev.adamko.kxstsgen.example.exampleTuple01.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export type SimpleTypes = [string, number, number | null, boolean, string];
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile()
    }
  }

  context("ExampleTuple02") {
    val actual = captureOutput("ExampleTuple02") {
      dev.adamko.kxstsgen.example.exampleTuple02.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export type OptionalFields = [string, string, string | null, string | null];
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile()
    }
  }

  context("ExampleTuple03") {
    val actual = captureOutput("ExampleTuple03") {
      dev.adamko.kxstsgen.example.exampleTuple03.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export type Coordinates = [number, number, number];
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile()
    }
  }

  context("ExampleTuple04") {
    val actual = captureOutput("ExampleTuple04") {
      dev.adamko.kxstsgen.example.exampleTuple04.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export interface GameLocations {
          |  homeLocation: Coordinates;
          |  allLocations: Coordinates[];
          |  namedLocations: { [key: string]: Coordinates };
          |}
          |
          |export type Coordinates = [number, number, number];
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile()
    }
  }
})
