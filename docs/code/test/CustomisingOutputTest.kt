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
    val actual = captureOutput("ExampleCustomisingOutput01") {
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
    //   actual.shouldTypeScriptCompile()
    // }
  }
})
