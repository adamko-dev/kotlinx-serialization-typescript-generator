// This file was automatically generated from polymorphism.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import io.kotest.matchers.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test
import dev.adamko.kxstsgen.util.*

class PolymorphismTest {
  @Test
  fun testExamplePolymorphicAbstractClassPrimitiveFields01() {
    captureOutput("ExamplePolymorphicAbstractClassPrimitiveFields01") {
      dev.adamko.kxstsgen.example.examplePolymorphicAbstractClassPrimitiveFields01.main()
    }.normalizeJoin()
      .shouldBe(
        // language=TypeScript
        """
          |export type SimpleTypes = any;
          |// export interface SimpleTypes {
          |//   aString: string;
          |//   anInt: number;
          |//   aDouble: number;
          |//   bool: boolean;
          |//   privateMember: string;
          |// }
        """.trimMargin()
          .normalize()
      )
  }

  @Test
  fun testExamplePolymorphicStaticTypes01() {
    captureOutput("ExamplePolymorphicStaticTypes01") {
      dev.adamko.kxstsgen.example.examplePolymorphicStaticTypes01.main()
    }.normalizeJoin()
      .shouldBe(
        // language=TypeScript
        """
          |export interface Project {
          |  name: string;
          |}
        """.trimMargin()
          .normalize()
      )
  }

  @Test
  fun testExamplePolymorphicStaticTypes02() {
    captureOutput("ExamplePolymorphicStaticTypes02") {
      dev.adamko.kxstsgen.example.examplePolymorphicStaticTypes02.main()
    }.normalizeJoin()
      .shouldBe(
        // language=TypeScript
        """
          |export type Project = any;
          |// export interface Project {
          |//   name: string;
          |// }
          |//
          |// export interface OwnedProject extends Project {
          |//   name: string;
          |//   owner: string;
          |// }
        """.trimMargin()
          .normalize()
      )
  }

  @Test
  fun testExamplePolymorphicSealedClass01() {
    captureOutput("ExamplePolymorphicSealedClass01") {
      dev.adamko.kxstsgen.example.examplePolymorphicSealedClass01.main()
    }.normalizeJoin()
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
          |    type: Project.Type.OProj;
          |    name: string;
          |    owner: string;
          |  }
          |
          |  export interface DeprecatedProject {
          |    type: Project.Type.DeprecatedProject;
          |    name: string;
          |    reason: string;
          |  }
          |}
        """.trimMargin()
          .normalize()
      )
  }

  @Test
  fun testExamplePolymorphicSealedClass02() {
    captureOutput("ExamplePolymorphicSealedClass02") {
      dev.adamko.kxstsgen.example.examplePolymorphicSealedClass02.main()
    }.normalizeJoin()
      .shouldBe(
        // language=TypeScript
        """
          |export type Dog = Dog.Golden | Dog.Mutt | Dog.NovaScotia;
          |
          |export namespace Dog {
          |  export enum Type {
          |    Mutt = "Mutt",
          |    Golden = "Golden",
          |    NovaScotia = "NovaScotia",
          |  }
          |
          |  export interface Mutt {
          |    type: Dog.Type.Mutt;
          |    name: string;
          |    loveable?: boolean;
          |  }
          |
          |  export interface Golden {
          |    type: Dog.Type.Golden;
          |    name: string;
          |    colour: string;
          |    cute?: boolean;
          |  }
          |
          |  export interface NovaScotia {
          |    type: Dog.Type.NovaScotia;
          |    name: string;
          |    colour: string;
          |    adorable?: boolean;
          |  }
          |}
          |// Nested sealed classes don't work at the moment :(
          |// export type Dog = Dog.Mutt | Dog.Retriever
          |//
          |// export namespace Dog {
          |//   export enum Type {
          |//     Mutt = "Mutt",
          |//   }
          |//
          |//   export interface Mutt {
          |//     type: Type.Mutt;
          |//     name: string;
          |//     loveable?: boolean;
          |//   }
          |//
          |//   export type Retriever = Retriever.Golden | Retriever.NovaScotia
          |//
          |//   export namespace Retriever {
          |//     export enum Type {
          |//       Golden = "Golden",
          |//       NovaScotia = "NovaScotia",
          |//     }
          |//
          |//     export interface Golden {
          |//       type: Type.Golden;
          |//       name: string;
          |//       cute?: boolean;
          |//     }
          |//
          |//     export interface NovaScotia {
          |//       type: Type.NovaScotia;
          |//       name: string;
          |//       adorable?: boolean;
          |//     }
          |//   }
          |// }
        """.trimMargin()
          .normalize()
      )
  }

  @Test
  fun testExamplePolymorphicObjects01() {
    captureOutput("ExamplePolymorphicObjects01") {
      dev.adamko.kxstsgen.example.examplePolymorphicObjects01.main()
    }.normalizeJoin()
      .shouldBe(
        // language=TypeScript
        """
          |export type Response = Response.EmptyResponse | Response.TextResponse;
          |
          |export namespace Response {
          |  export enum Type {
          |    EmptyResponse = "EmptyResponse",
          |    TextResponse = "TextResponse",
          |  }
          |
          |  export interface EmptyResponse {
          |    type: Response.Type.EmptyResponse;
          |  }
          |
          |  export interface TextResponse {
          |    type: Response.Type.TextResponse;
          |    text: string;
          |  }
          |}
        """.trimMargin()
          .normalize()
      )
  }

  @Test
  fun testExampleGenerics01() {
    captureOutput("ExampleGenerics01") {
      dev.adamko.kxstsgen.example.exampleGenerics01.main()
    }.normalizeJoin()
      .shouldBe(
        // language=TypeScript
        """
          |export interface Box {
          |  value: number;
          |}
        """.trimMargin()
          .normalize()
      )
  }

  @Test
  fun testExampleJsonPolymorphic01() {
    captureOutput("ExampleJsonPolymorphic01") {
      dev.adamko.kxstsgen.example.exampleJsonPolymorphic01.main()
    }.normalizeJoin()
      .shouldBe(
        // language=TypeScript
        """
          |export type Project = any;
        """.trimMargin()
          .normalize()
      )
  }
}
