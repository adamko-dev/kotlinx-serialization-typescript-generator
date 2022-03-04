// This file was automatically generated from lists.md by Knit tool. Do not edit.
package example.test

import io.kotest.matchers.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test

class ListsTests {
  @Test
  fun testExampleListPrimitive01() {
    captureOutput("ExampleListPrimitive01") {
      example.exampleListPrimitive01.main()
    }.joinToString("\n")
      .shouldBe(
        // language=TypeScript
        """
          |interface CalendarEvent {
          |  attendeeNames: string[];
          |}
        """.trimMargin()
      )
  }
}
