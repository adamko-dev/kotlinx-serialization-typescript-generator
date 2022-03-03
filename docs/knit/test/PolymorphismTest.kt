// This file was automatically generated from polymorphism.md by Knit tool. Do not edit.
package example.test

import org.junit.jupiter.api.Test
import kotlinx.knit.test.*

class PolymorphismTest {
  @Test
  fun testExamplePolymorphismSealed01() {
    captureOutput("ExamplePolymorphismSealed01") {
      example.examplePolymorphismSealed01.main()
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
}
