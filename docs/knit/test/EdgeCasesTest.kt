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
    }.joinToString("\n") { it.ifBlank { "" } }
      .shouldBe(
        // language=TypeScript
        """
          |export interface A {
          |  b: B;
          |}
          |
          |export interface B {
          |  a: A;
          |}
        """.trimMargin()
          .lines()
          .joinToString("\n") { it.ifBlank { "" } }
      )
  }

  @Test
  fun testExampleEdgecaseRecursiveReferences02() {
    captureOutput("ExampleEdgecaseRecursiveReferences02") {
      dev.adamko.kxstsgen.example.exampleEdgecaseRecursiveReferences02.main()
    }.joinToString("\n") { it.ifBlank { "" } }
      .shouldBe(
        // language=TypeScript
        """
          |export interface A {
          |  list: B[];
          |}
          |
          |export interface B {
          |  list: A[];
          |}
        """.trimMargin()
          .lines()
          .joinToString("\n") { it.ifBlank { "" } }
      )
  }

  @Test
  fun testExampleEdgecaseRecursiveReferences03() {
    captureOutput("ExampleEdgecaseRecursiveReferences03") {
      dev.adamko.kxstsgen.example.exampleEdgecaseRecursiveReferences03.main()
    }.joinToString("\n") { it.ifBlank { "" } }
      .shouldBe(
        // language=TypeScript
        """
          |export interface A {
          |  map: { [key: string]: B };
          |}
          |
          |export interface B {
          |  map: { [key: string]: A };
          |}
        """.trimMargin()
          .lines()
          .joinToString("\n") { it.ifBlank { "" } }
      )
  }
}
