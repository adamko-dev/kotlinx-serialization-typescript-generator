package dev.adamko.kxstsgen.util

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldContainIgnoringCase
import io.kotest.matchers.string.shouldNotBeEmpty
import io.kotest.matchers.string.shouldNotContain
import java.nio.file.Path
import kotlin.coroutines.CoroutineContext
import kotlin.io.path.createDirectory
import kotlin.io.path.createTempDirectory
import kotlin.io.path.createTempFile
import kotlin.io.path.exists
import kotlin.io.path.invariantSeparatorsPathString
import kotlin.io.path.writeText
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext


private val processContext: CoroutineContext =
  Dispatchers.IO + SupervisorJob() + CoroutineName("TscCompile")


private val tempDir: Path by lazy {
  createTempDirectory().resolveSibling("kxstsgen").apply {
    if (!exists()) createDirectory()
    println("""Test output dir file:///${this.invariantSeparatorsPathString}""")
  }
}


suspend fun String?.shouldTypeScriptCompile(
  testName: String
): String = withContext(processContext) {
  val src = this@shouldTypeScriptCompile
  src.shouldNotBeNull()

  val file: Path = createTempFile(
    directory = tempDir,
    prefix = testName,
    suffix = ".ts",
  )
  file.writeText(src)

  val (process, output) = typescriptCompile(file)

  val outputPretty = output.joinToString("\n").prependIndent(" > ")

  outputPretty.asClue { log ->
    withClue("exit code should be 0") { process shouldBeExactly 0 }
    withClue("log should not be empty") { log.shouldNotBeEmpty() }
//    log shouldContainIgnoringCase "npx: installed"
    log shouldNotContain "error TS"
  }

  src
}
