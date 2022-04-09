// This file was automatically generated from edgecases.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import dev.adamko.kxstsgen.util.*
import io.kotest.assertions.*
import io.kotest.matchers.*
import io.kotest.matchers.string.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test

class EdgeCasesTest {
  @Test
  fun testExampleEdgecaseRecursiveReferences01() {
    val actual = captureOutput("ExampleEdgecaseRecursiveReferences01") {
      dev.adamko.kxstsgen.example.exampleEdgecaseRecursiveReferences01.main()
    }.normalizeJoin()

    actual.shouldBe(
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

    typescriptCompile(actual).asClue { tscOutput ->
      tscOutput.shouldNotBeEmpty()
      tscOutput shouldNotContain "error"
    }
  }

  @Test
  fun testExampleEdgecaseRecursiveReferences02() {
    val actual = captureOutput("ExampleEdgecaseRecursiveReferences02") {
      dev.adamko.kxstsgen.example.exampleEdgecaseRecursiveReferences02.main()
    }.normalizeJoin()

    actual.shouldBe(
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

    typescriptCompile(actual).asClue { tscOutput ->
      tscOutput.shouldNotBeEmpty()
      tscOutput shouldNotContain "error"
    }
  }

  @Test
  fun testExampleEdgecaseRecursiveReferences03() {
    val actual = captureOutput("ExampleEdgecaseRecursiveReferences03") {
      dev.adamko.kxstsgen.example.exampleEdgecaseRecursiveReferences03.main()
    }.normalizeJoin()

    actual.shouldBe(
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

    typescriptCompile(actual).asClue { tscOutput ->
      tscOutput.shouldNotBeEmpty()
      tscOutput shouldNotContain "error"
    }
  }
}
