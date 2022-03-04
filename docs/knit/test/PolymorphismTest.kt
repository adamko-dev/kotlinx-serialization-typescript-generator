// This file was automatically generated from polymorphism.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import io.kotest.matchers.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test

class PolymorphismTest {
  @Test
  fun testExamplePolymorphicAbstractClassPrimitiveFields01() {
    captureOutput("ExamplePolymorphicAbstractClassPrimitiveFields01") {
      dev.adamko.kxstsgen.example.examplePolymorphicAbstractClassPrimitiveFields01.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |interface SimpleTypes {
          |  aString: string;
          |  anInt: number;
          |  aDouble: number;
          |  bool: boolean;
          |  privateMember: string;
          |}
        """.trimMargin()
      )
  }

  @Test
  fun testExamplePolymorphicStaticTypes01() {
    captureOutput("ExamplePolymorphicStaticTypes01") {
      dev.adamko.kxstsgen.example.examplePolymorphicStaticTypes01.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |interface Project {
          |  name: string;
          |}
        """.trimMargin()
      )
  }

  @Test
  fun testExamplePolymorphicStaticTypes02() {
    captureOutput("ExamplePolymorphicStaticTypes02") {
      dev.adamko.kxstsgen.example.examplePolymorphicStaticTypes02.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |interface Project {
          |  name: string;
          |}
          |
          |interface OwnedProject extends Project {
          |  name: string;
          |  owner: string;
          |}
        """.trimMargin()
      )
  }

  @Test
  fun testExamplePolymorphicSealedClass01() {
    captureOutput("ExamplePolymorphicSealedClass01") {
      dev.adamko.kxstsgen.example.examplePolymorphicSealedClass01.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |enum ProjectKind {
          |  OwnedProject,
          |  DeprecatedProject,
          |}
          |
          |interface Project {
          |  type: ProjectKind;
          |}
          |
          |interface OwnedProject extends Project {
          |  type: ProjectKind.OwnedProject;
          |  name: string;
          |  owner: string;
          |}
          |
          |interface DeprecatedProject extends Project {
          |  type: ProjectKind.DeprecatedProject;
          |  name: string;
          |  reason: string;
          |}
        """.trimMargin()
      )
  }

  @Test
  fun testExamplePolymorphicObjects01() {
    captureOutput("ExamplePolymorphicObjects01") {
      dev.adamko.kxstsgen.example.examplePolymorphicObjects01.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |export enum ResponseKind {
          |  EmptyResponse = "EmptyResponse",
          |  TextResponse = "TextResponse",
          |}
          |
          |interface Response {
          |  type: ResponseKind;
          |}
          |
          |interface EmptyResponse extends Response {
          |  type: ResponseKind.EmptyResponse;
          |}
          |
          |interface TextResponse extends Response {
          |  type: ResponseKind.TextResponse;
          |  text: string;
          |}
        """.trimMargin()
      )
  }
}
