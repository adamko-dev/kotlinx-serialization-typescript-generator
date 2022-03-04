// This file was automatically generated from value-classes.md by Knit tool. Do not edit.
package example.test

import org.junit.jupiter.api.Test
import kotlinx.knit.test.*

class ValueClassesTest {
  @Test
  fun testExampleValueClasses01() {
    captureOutput("ExampleValueClasses01") {
      example.exampleValueClasses01.main()
    }.verifyOutputLines(
      "type AuthToken = string;"
    )
  }

  @Test
  fun testExampleValueClasses02() {
    captureOutput("ExampleValueClasses02") {
      example.exampleValueClasses02.main()
    }.verifyOutputLines(
      "type UByte = number;",
      "",
      "type UShort = number;",
      "",
      "type UInt = number;",
      "",
      "type ULong = number;"
    )
  }

  @Test
  fun testExampleValueClasses03() {
    captureOutput("ExampleValueClasses03") {
      example.exampleValueClasses03.main()
    }.verifyOutputLines(
      "type ULong = number & { __ULong__: void };"
    )
  }

  @Test
  fun testExampleValueClasses04() {
    captureOutput("ExampleValueClasses04") {
      example.exampleValueClasses04.main()
    }.verifyOutputLines(
      "type UInt = number;",
      "",
      "type UserCount = UInt;"
    )
  }
}
