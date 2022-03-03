// This file was automatically generated from basic-classes.md by Knit tool. Do not edit.
package example.test

import org.junit.jupiter.api.Test
import kotlinx.knit.test.*

class BasicClassesTest {
  @Test
  fun testExamplePlainClassSingleField01() {
    captureOutput("ExamplePlainClassSingleField01") {
      example.examplePlainClassSingleField01.main()
    }.verifyOutputLines(
      "interface Color {",
      "  rgb: number;",
      "}"
    )
  }

  @Test
  fun testExamplePlainClassPrimitiveFields01() {
    captureOutput("ExamplePlainClassPrimitiveFields01") {
      example.examplePlainClassPrimitiveFields01.main()
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
  fun testExamplePlainDataClass01() {
    captureOutput("ExamplePlainDataClass01") {
      example.examplePlainDataClass01.main()
    }.verifyOutputLines(
      "interface SomeDataClass {",
      "  aString: string;",
      "  anInt: number;",
      "  aDouble: number;",
      "  bool: boolean;",
      "  privateMember: string;",
      "}"
    )
  }
}
