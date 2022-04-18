// This file was automatically generated from edgecases.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import dev.adamko.kxstsgen.util.*
import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import kotlinx.knit.test.*

class EdgeCasesTest : FunSpec({

  tags(Knit)
  context("ExampleEdgecaseRecursiveReferences01") {
    val caseName = testCase.name.testName

    val actual = captureOutput(caseName) {
      dev.adamko.kxstsgen.example.exampleEdgecaseRecursiveReferences01.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
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
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile(caseName)
    }
  }

  context("ExampleEdgecaseRecursiveReferences02") {
    val caseName = testCase.name.testName

    val actual = captureOutput(caseName) {
      dev.adamko.kxstsgen.example.exampleEdgecaseRecursiveReferences02.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
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
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile(caseName)
    }
  }

  context("ExampleEdgecaseRecursiveReferences03") {
    val caseName = testCase.name.testName

    val actual = captureOutput(caseName) {
      dev.adamko.kxstsgen.example.exampleEdgecaseRecursiveReferences03.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
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
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile(caseName)
    }
  }
})
