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
    }.joinToString("\n") { it.ifBlank { "" } }
      .shouldBe(
        // language=TypeScript
        """
          |export interface SimpleTypes {
          |  aString: string;
          |  anInt: number;
          |  aDouble: number;
          |  bool: boolean;
          |  privateMember: string;
          |}
        """.trimMargin()
          .lines()
          .joinToString("\n") { it.ifBlank { "" } }
      )
  }

  @Test
  fun testExamplePolymorphicStaticTypes01() {
    captureOutput("ExamplePolymorphicStaticTypes01") {
      dev.adamko.kxstsgen.example.examplePolymorphicStaticTypes01.main()
    }.joinToString("\n") { it.ifBlank { "" } }
      .shouldBe(
        // language=TypeScript
        """
          |export interface Project {
          |  name: string;
          |}
        """.trimMargin()
          .lines()
          .joinToString("\n") { it.ifBlank { "" } }
      )
  }

  @Test
  fun testExamplePolymorphicStaticTypes02() {
    captureOutput("ExamplePolymorphicStaticTypes02") {
      dev.adamko.kxstsgen.example.examplePolymorphicStaticTypes02.main()
    }.joinToString("\n") { it.ifBlank { "" } }
      .shouldBe(
        // language=TypeScript
        """
          |export interface Project {
          |  name: string;
          |}
          |
          |export interface OwnedProject extends Project {
          |  name: string;
          |  owner: string;
          |}
        """.trimMargin()
          .lines()
          .joinToString("\n") { it.ifBlank { "" } }
      )
  }

  @Test
  fun testExamplePolymorphicSealedClass01() {
    captureOutput("ExamplePolymorphicSealedClass01") {
      dev.adamko.kxstsgen.example.examplePolymorphicSealedClass01.main()
    }.joinToString("\n") { it.ifBlank { "" } }
      .shouldBe(
        // language=TypeScript
        """
          |export type Project = Project.DeprecatedProject | Project.OProj;
          |
          |export namespace Project {
          |  export enum Type {
          |    OProj = "OProj",
          |    DeprecatedProject = "DeprecatedProject",
          |  }
          |
          |  export interface OProj {
          |    type: Type.OProj;
          |    name: string;
          |    owner: string;
          |  }
          |
          |  export interface DeprecatedProject {
          |    type: Type.DeprecatedProject;
          |    name: string;
          |    reason: string;
          |  }
          |}
        """.trimMargin()
          .lines()
          .joinToString("\n") { it.ifBlank { "" } }
      )
  }

  @Test
  fun testExamplePolymorphicSealedClass02() {
    captureOutput("ExamplePolymorphicSealedClass02") {
      dev.adamko.kxstsgen.example.examplePolymorphicSealedClass02.main()
    }.joinToString("\n") { it.ifBlank { "" } }
      .shouldBe(
        // language=TypeScript
        """
          |export type Dog = Dog.Mutt | Dog.Retriever
          |
          |export namespace Dog {
          |
          |  export enum Type {
          |    Mutt = "Mutt",
          |  }
          |
          |  export interface Mutt {
          |    type: Type.Mutt;
          |    name: string;
          |    loveable?: boolean;
          |  }
          |
          |  export type Retriever = Retriever.Golden | Retriever.NovaScotia
          |
          |  export namespace Retriever {
          |
          |    export enum Type {
          |      Golden = "Golden",
          |      NovaScotia = "NovaScotia",
          |    }
          |
          |    export interface Golden {
          |      type: Type.Golden;
          |      name: string;
          |      cute?: boolean;
          |    }
          |
          |    export interface NovaScotia {
          |      type: Type.NovaScotia;
          |      name: string;
          |      adorable?: boolean;
          |    }
          |  }
          |
          |}
        """.trimMargin()
          .lines()
          .joinToString("\n") { it.ifBlank { "" } }
      )
  }

  @Test
  fun testExamplePolymorphicObjects01() {
    captureOutput("ExamplePolymorphicObjects01") {
      dev.adamko.kxstsgen.example.examplePolymorphicObjects01.main()
    }.joinToString("\n") { it.ifBlank { "" } }
      .shouldBe(
        // language=TypeScript
        """
          |export enum ResponseKind {
          |  EmptyResponse = "EmptyResponse",
          |  TextResponse = "TextResponse",
          |}
          |
          |export type Response = EmptyResponse | TextResponse
          |
          |export interface EmptyResponse {
          |  type: ResponseKind.EmptyResponse;
          |}
          |
          |export interface TextResponse {
          |  type: ResponseKind.TextResponse;
          |  text: string;
          |}
        """.trimMargin()
          .lines()
          .joinToString("\n") { it.ifBlank { "" } }
      )
  }
}
