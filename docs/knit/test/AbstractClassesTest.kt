// This file was automatically generated from abstract-classes.md by Knit tool. Do not edit.
package example.test

import io.kotest.matchers.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test

class AbstractClassesTest {
  @Test
  fun testExampleAbstractClassSingleField01() {
    captureOutput("ExampleAbstractClassSingleField01") {
      example.exampleAbstractClassSingleField01.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |interface Color {
          |  rgb: number;
          |}
        """.trimMargin()
      )
  }

  @Test
  fun testExampleAbstractClassPrimitiveFields01() {
    captureOutput("ExampleAbstractClassPrimitiveFields01") {
      example.exampleAbstractClassPrimitiveFields01.main()
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
  fun testExampleAbstractClassAbstractField01() {
    captureOutput("ExampleAbstractClassAbstractField01") {
      example.exampleAbstractClassAbstractField01.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |interface Color {
          |  rgb: number;
          |}
        """.trimMargin()
      )
  }
}
