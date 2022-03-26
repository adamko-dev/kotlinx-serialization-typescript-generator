// This file was automatically generated from edgecases.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import io.kotest.matchers.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test

class EdgeCasesTest {
  @Test
  fun testExampleEdgecaseRecursiveReferences01() {
    captureOutput("ExampleEdgecaseRecursiveReferences01") {
      dev.adamko.kxstsgen.example.exampleEdgecaseRecursiveReferences01.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |interface A {
          |  b: B;
          |}
          |
          |interface B {
          |  a: A;
          |}
        """.trimMargin()
      )
  }

  @Test
  fun testExampleEdgecaseRecursiveReferences02() {
    captureOutput("ExampleEdgecaseRecursiveReferences02") {
      dev.adamko.kxstsgen.example.exampleEdgecaseRecursiveReferences02.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |interface A {
          |  list: B[];
          |}
          |
          |interface B {
          |  list: A[];
          |}
        """.trimMargin()
      )
  }

  @Test
  fun testExampleEdgecaseRecursiveReferences03() {
    captureOutput("ExampleEdgecaseRecursiveReferences03") {
      dev.adamko.kxstsgen.example.exampleEdgecaseRecursiveReferences03.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |interface A {
          |  map: { [key: string]: B };
          |}
          |
          |interface B {
          |  map: { [key: string]: A };
          |}
        """.trimMargin()
      )
  }
}
