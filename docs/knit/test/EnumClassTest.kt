// This file was automatically generated from enums.md by Knit tool. Do not edit.
package example.test

import org.junit.jupiter.api.Test
import kotlinx.knit.test.*

class EnumClassTest {
  @Test
  fun testExampleEnumClass01() {
    captureOutput("ExampleEnumClass01") {
      example.exampleEnumClass01.main()
    }.verifyOutputLines(
      "enum SomeType {",
      "  Alpha = \"Alpha\",",
      "  Beta = \"Beta\",",
      "  Gamma = \"Gamma\",",
      "}"
    )
  }

  @Test
  fun testExampleEnumClass02() {
    captureOutput("ExampleEnumClass02") {
      example.exampleEnumClass02.main()
    }.verifyOutputLines(
      "enum SomeType2 {",
      "  Alpha = \"Alpha\",",
      "  Beta = \"Beta\",",
      "  Gamma = \"Gamma\",",
      "}"
    )
  }
}
