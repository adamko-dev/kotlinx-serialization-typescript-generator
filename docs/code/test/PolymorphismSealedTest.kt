// This file was automatically generated from polymorphism-sealed.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package dev.adamko.kxstsgen.example.test

import dev.adamko.kxstsgen.util.*
import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import kotlinx.knit.test.*

class PolymorphismSealedTest : FunSpec({

  tags(Knit)
  context("ExamplePolymorphicStaticTypes01") {
    val caseName = testCase.name.testName

    val actual = captureOutput(caseName) {
      dev.adamko.kxstsgen.example.examplePolymorphicStaticTypes01.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export interface Project {
          |  name: string;
          |}
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile(caseName)
    }
  }

  context("ExamplePolymorphicStaticTypes02") {
    val caseName = testCase.name.testName

    val actual = captureOutput(caseName) {
      dev.adamko.kxstsgen.example.examplePolymorphicStaticTypes02.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
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

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile(caseName)
    }
  }

  context("ExamplePolymorphicSealedClass01") {
    val caseName = testCase.name.testName

    val actual = captureOutput(caseName) {
      dev.adamko.kxstsgen.example.examplePolymorphicSealedClass01.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export type Project =
          |  | Project.DeprecatedProject
          |  | Project.OProj;
          |
          |export namespace Project {
          |  export enum Type {
          |    DeprecatedProject = "dev.adamko.kxstsgen.example.examplePolymorphicSealedClass01.DeprecatedProject",
          |    OProj = "OProj",
          |  }
          |
          |  export interface DeprecatedProject {
          |    type: Project.Type.DeprecatedProject;
          |    name: string;
          |    reason: string;
          |  }
          |
          |  export interface OProj {
          |    type: Project.Type.OProj;
          |    name: string;
          |    owner: string;
          |  }
          |}
        """.trimMargin()
        .normalize()
      )
    }

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile(caseName)
    }
  }

  context("ExamplePolymorphicSealedClass02") {
    val caseName = testCase.name.testName

    val actual = captureOutput(caseName) {
      dev.adamko.kxstsgen.example.examplePolymorphicSealedClass02.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export type Dog =
          |  | Dog.Golden
          |  | Dog.Mutt
          |  | Dog.NovaScotia;
          |
          |export namespace Dog {
          |  export enum Type {
          |    Mutt = "Dog.Mutt",
          |    Golden = "Dog.Retriever.Golden",
          |    NovaScotia = "Dog.Retriever.NovaScotia",
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

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile(caseName)
    }
  }

  context("ExamplePolymorphicObjects01") {
    val caseName = testCase.name.testName

    val actual = captureOutput(caseName) {
      dev.adamko.kxstsgen.example.examplePolymorphicObjects01.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        // language=TypeScript
        """
          |export type Response =
          |  | Response.EmptyResponse
          |  | Response.TextResponse;
          |
          |export namespace Response {
          |  export enum Type {
          |    EmptyResponse = "dev.adamko.kxstsgen.example.examplePolymorphicObjects01.EmptyResponse",
          |    TextResponse = "dev.adamko.kxstsgen.example.examplePolymorphicObjects01.TextResponse",
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

    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile(caseName)
    }
  }
})
