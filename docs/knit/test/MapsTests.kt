// This file was automatically generated from maps.md by Knit tool. Do not edit.
package example.test

import org.junit.jupiter.api.Test
import kotlinx.knit.test.*

class MapsTests {
  @Test
  fun testExampleMapPrimitive01() {
    captureOutput("ExampleMapPrimitive01") {
      example.exampleMapPrimitive01.main()
    }.verifyOutputLines(
      "interface Config {",
      "  properties: { [key: string]: string };",
      "}"
    )
  }
}
