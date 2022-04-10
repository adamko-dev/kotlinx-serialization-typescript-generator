// This file was automatically generated from tuples.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")

package dev.adamko.kxstsgen.example.test

import dev.adamko.kxstsgen.util.TSCompile
import dev.adamko.kxstsgen.util.normalize
import dev.adamko.kxstsgen.util.normalizeJoin
import dev.adamko.kxstsgen.util.shouldTypeScriptCompile
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.knit.test.captureOutput

class TuplesTest2 : FunSpec({

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

    test("expect actual compiles").config(tags = setOf(TSCompile)) {
      actual.shouldTypeScriptCompile()
    }
  }
})
