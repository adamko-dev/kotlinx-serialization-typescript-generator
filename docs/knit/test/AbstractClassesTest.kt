// This file was automatically generated from abstract-classes.md by Knit tool. Do not edit.
package example.test

import org.junit.jupiter.api.Test
import kotlinx.knit.test.*

class AbstractClassesTest {
  @Test
  fun testExampleAbstractClassSingleField01() {
    captureOutput("ExampleAbstractClassSingleField01") {
      example.exampleAbstractClassSingleField01.main()
    }.verifyOutputLines(
      "interface Color {",
      "  rgb: number;",
      "}"
    )
  }

  @Test
  fun testExampleAbstractClassPrimitiveFields01() {
    captureOutput("ExampleAbstractClassPrimitiveFields01") {
      example.exampleAbstractClassPrimitiveFields01.main()
    }.verifyOutputLines(
      "interface SimpleTypes {",
      "  aString: string;",
      "  anInt: number;",
      "  aDouble: number;",
      "  bool: boolean;",
      "  privateMember: string;",
      "}"
    )
  }

  @Test
  fun testExampleAbstractClassAbstractField01() {
    captureOutput("ExampleAbstractClassAbstractField01") {
      example.exampleAbstractClassAbstractField01.main()
    }.verifyOutputLines(
      "interface Color {",
      "  rgb: number;",
      "}"
    )
  }
}
