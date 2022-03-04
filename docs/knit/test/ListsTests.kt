// This file was automatically generated from lists.md by Knit tool. Do not edit.
package example.test

import org.junit.jupiter.api.Test
import kotlinx.knit.test.*

class ListsTests {
  @Test
  fun testExampleListPrimitive01() {
    captureOutput("ExampleListPrimitive01") {
      example.exampleListPrimitive01.main()
    }.verifyOutputLines(
      "interface CalendarEvent {",
      "  attendeeNames: string[];",
      "}"
    )
  }
}
