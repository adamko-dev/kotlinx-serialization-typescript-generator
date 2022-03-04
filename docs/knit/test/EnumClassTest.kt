// This file was automatically generated from enums.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package example.test

import io.kotest.matchers.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test

class EnumClassTest {
  @Test
  fun testExampleEnumClass01() {
    captureOutput("ExampleEnumClass01") {
      example.exampleEnumClass01.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |export enum SomeType {
          |  Alpha = "Alpha",
          |  Beta = "Beta",
          |  Gamma = "Gamma",
          |}
        """.trimMargin()
      )
  }

  @Test
  fun testExampleEnumClass02() {
    captureOutput("ExampleEnumClass02") {
      example.exampleEnumClass02.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |export enum SomeType2 {
          |  Alpha = "Alpha",
          |  Beta = "Beta",
          |  Gamma = "Gamma",
          |}
        """.trimMargin()
      )
  }
}
