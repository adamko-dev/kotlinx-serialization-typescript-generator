// This file was automatically generated from lists.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import io.kotest.matchers.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test

class ListsTests {
  @Test
  fun testExampleListPrimitive01() {
    captureOutput("ExampleListPrimitive01") {
      dev.adamko.kxstsgen.example.exampleListPrimitive01.main()
    }.joinToString("\n") { it.ifBlank { "" } }
      .shouldBe(
        // language=TypeScript
        """
          |export interface MyLists {
          |  strings: string[];
          |  ints: number[];
          |  longs: number[];
          |}
        """.trimMargin()
          .lines()
          .joinToString("\n") { it.ifBlank { "" } }
      )
  }
}
