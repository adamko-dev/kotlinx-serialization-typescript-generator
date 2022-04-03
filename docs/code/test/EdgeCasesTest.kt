// This file was automatically generated from edgecases.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import io.kotest.matchers.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test
import dev.adamko.kxstsgen.util.*

class EdgeCasesTest {
  @Test
  fun testExampleEdgecaseRecursiveReferences01() {
    captureOutput("ExampleEdgecaseRecursiveReferences01") {
      dev.adamko.kxstsgen.example.exampleEdgecaseRecursiveReferences01.main()
    }.normalizeJoin()
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
          .normalize()
      )
  }

  @Test
  fun testExampleEdgecaseRecursiveReferences02() {
    captureOutput("ExampleEdgecaseRecursiveReferences02") {
      dev.adamko.kxstsgen.example.exampleEdgecaseRecursiveReferences02.main()
    }.normalizeJoin()
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
          .normalize()
      )
  }

  @Test
  fun testExampleEdgecaseRecursiveReferences03() {
    captureOutput("ExampleEdgecaseRecursiveReferences03") {
      dev.adamko.kxstsgen.example.exampleEdgecaseRecursiveReferences03.main()
    }.normalizeJoin()
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
          .normalize()
      )
  }
}
