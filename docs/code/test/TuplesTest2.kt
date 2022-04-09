// This file was automatically generated from tuples.md by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")

package dev.adamko.kxstsgen.example.test

import dev.adamko.kxstsgen.util.normalize
import dev.adamko.kxstsgen.util.normalizeJoin
import io.kotest.matchers.shouldBe
import java.nio.file.Path
import java.util.concurrent.TimeUnit
import kotlin.io.path.invariantSeparatorsPathString
import kotlin.io.path.readText
import kotlin.io.path.writeText
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

    val npmInstallDir = System.getenv("NPM_DIR")
    println("NPM_DIR: $npmInstallDir")

//    val x: Process = Runtime.getRuntime().exec("$npmInstallDir/npx cowsay moo")


    val file: Path = kotlin.io.path.createTempFile("kxstsgen", ".d.ts")
    file.writeText(actual.filter { it.isLetter() })

    println("file: ${file.invariantSeparatorsPathString}")
    println("----")
    println(file.readText())
    println("----")

    val p: Process =
      ProcessBuilder(
        "npx.cmd",
        "-p",
        "typescript",
        "tsc",
        file.invariantSeparatorsPathString,
        "--noEmit",
      )
        .inheritIO()
        .start()

    p.waitFor(30, TimeUnit.SECONDS)

    val output = p.inputStream.readAllBytes().decodeToString()

    println("----")
    println(output)
    println("----")

  }

}
