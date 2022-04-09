// This file was automatically generated from tuples.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")

package dev.adamko.kxstsgen.example.test

import dev.adamko.kxstsgen.util.normalize
import dev.adamko.kxstsgen.util.normalizeJoin
import dev.adamko.kxstsgen.util.typescriptCompile
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeEmpty
import io.kotest.matchers.string.shouldNotContain
import kotlinx.knit.test.captureOutput
import org.junit.jupiter.api.Test

class TuplesTest2 {
  @Test
  fun testExampleTuple01() {
    val actual = captureOutput("ExampleTuple01") {
      dev.adamko.kxstsgen.example.exampleTuple01.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
          |export type SimpleTypes = [string, number, number | null, boolean, string];
        """.trimMargin()
        .normalize()
    )

    typescriptCompile(actual).asClue { tscOutput ->
      tscOutput.shouldNotBeEmpty()
      tscOutput shouldNotContain "error"
    }
  }
}
