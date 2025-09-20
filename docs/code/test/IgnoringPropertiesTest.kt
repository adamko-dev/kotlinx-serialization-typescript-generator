// This file was automatically generated from ignoring-properties.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import dev.adamko.kxstsgen.util.*
import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import kotlinx.knit.test.*

class IgnoringPropertiesTest : FunSpec({

  tags(Knit)
  context("ExamplePlainClassIgnoredProperty01") {
    val caseName = testCase.name.name

    val actual = captureOutput(caseName) {
      dev.adamko.kxstsgen.example.examplePlainClassIgnoredProperty01.main()
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
      actual.shouldTypeScriptCompile(caseName)
    }
  }
})
