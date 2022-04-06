// This file was automatically generated from export-formats.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import io.kotest.matchers.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test
import dev.adamko.kxstsgen.util.*

class TsExportFormatTest {
  @Test
  fun testExampleFormatTuple01() {
    captureOutput("ExampleFormatTuple01") {
      dev.adamko.kxstsgen.example.exampleFormatTuple01.main()
    }.normalizeJoin()
      .shouldBe(
        // language=TypeScript
        """
          |export type SimpleTypes = [string, number, number, boolean, string];
        """.trimMargin()
          .normalize()
      )
  }
}
