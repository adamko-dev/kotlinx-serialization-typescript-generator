// This file was automatically generated from default-values.md by Knit tool. Do not edit.
package example.test

import org.junit.jupiter.api.Test
import kotlinx.knit.test.*

class DefaultValuesTest {
  @Test
  fun testExampleDefaultValuesSingleField01() {
    captureOutput("ExampleDefaultValuesSingleField01") {
      example.exampleDefaultValuesSingleField01.main()
    }.verifyOutputLines(
      "interface Color {",
      "  rgb?: number;",
      "}"
    )
  }

  @Test
  fun testExampleDefaultValuesPrimitiveFields01() {
    captureOutput("ExampleDefaultValuesPrimitiveFields01") {
      example.exampleDefaultValuesPrimitiveFields01.main()
    }.verifyOutputLines(
      "interface ContactDetails {",
      "  email: string | null;",
      "  phoneNumber?: string | null;",
      "  active?: boolean | null;",
      "}"
    )
  }
}
