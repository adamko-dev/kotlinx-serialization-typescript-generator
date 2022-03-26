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
          |export type Project = Project.Owned | Project.Deprecated
          |
          |export namespace Project {
          |
          |  export enum Type {
          |    Owned = "owned-project",
          |    Deprecated = "DeprecatedProject",
          |  }
          |
          |  export interface Owned {
          |    type: Type.Owned;
          |    name: string;
          |    owner: string;
          |  }
          |
          |  export interface Deprecated {
          |    type: Type.Deprecated;
          |    name: string;
          |    reason: string;
          |  }
          |
          |}
        """.trimMargin()
      )
  }

  @Test
  fun testExamplePolymorphicSealedClass02() {
    captureOutput("ExamplePolymorphicSealedClass02") {
      dev.adamko.kxstsgen.example.examplePolymorphicSealedClass02.main()
    }.joinToString("\n")
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
          |type Response = EmptyResponse | TextResponse
          |
          |interface EmptyResponse {
          |  type: ResponseKind.EmptyResponse;
          |}
          |
          |interface TextResponse {
          |  type: ResponseKind.TextResponse;
          |  text: string;
          |}
        """.trimMargin()
      )
  }
}
