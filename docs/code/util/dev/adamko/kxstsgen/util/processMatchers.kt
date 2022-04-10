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


suspend fun String?.shouldTypeScriptCompile(): String = withContext(processContext) {
  val src = this@shouldTypeScriptCompile
  src.shouldNotBeNull()

  val file: Path = createTempFile(
    directory = tempDir,
    prefix = src.filter { it.isLetterOrDigit() }.take(20),
    suffix = ".ts",
  )
  file.writeText(src)

  val (process, output) = tsc2(file)
  output.joinToString("\n").asClue { log ->
    withClue("exit code should be 0") { process shouldBeExactly 0 }
    log.shouldNotBeEmpty()
    log shouldContainIgnoringCase "npx: installed"
    log shouldNotContain "error TS"
  }

  src
}

//
//private fun Process?.shouldHaveSuccessfulExitCode(): Process {
//  this.shouldNotBeNull()
//  this.should { haveSuccessfulExitCode() }
//  return this
//}
//
//// process should...
//private fun haveSuccessfulExitCode(): Matcher<Process> = Matcher { process ->
//  MatcherResult(
//    process.exitValue() != 0,
//    { "process did not exit successfully ${process.exitValue()}" },
//    { "process should not exit successfully, but exit code was ${process.exitValue()}" },
//  )
//}
//
//
//fun Process?.shouldHaveFinished(
//  timeout: Duration = 5.seconds,
//): Process {
//  this.shouldNotBeNull()
//  this.should { haveFinished(timeout) }
//  return this
//}
//
//// process should...
//private fun haveFinished(
//  timeout: Duration = 5.seconds,
//): Matcher<Process> = Matcher { process ->
//
//  process.waitFor(timeout.inWholeMilliseconds, TimeUnit.MILLISECONDS)
//  process.destroy()
//
//  MatcherResult(
//    !process.isAlive,
//    { "process is still alive" },
//    { "process should not be alive" },
//  )
//}
