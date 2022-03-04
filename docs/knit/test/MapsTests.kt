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

  @Test
  fun testExampleMapPrimitive02() {
    captureOutput("ExampleMapPrimitive02") {
      example.exampleMapPrimitive02.main()
    }.verifyOutputLines(
      "interface Config {",
      "  properties: { [key: string | null]: string | null };",
      "}"
    )
  }

  @Test
  fun testExampleMapComplex01() {
    captureOutput("ExampleMapComplex01") {
      example.exampleMapComplex01.main()
    }.verifyOutputLines(
      "type UByte = number;",
      "",
      "interface Colour {",
      "  r: UByte;",
      "  g: UByte;",
      "  b: UByte;",
      "  a: UByte;",
      "}",
      "",
      "interface CanvasProperties {",
      "  colourNames: { [key: Colour]: string };",
      "}"
    )
  }
}
