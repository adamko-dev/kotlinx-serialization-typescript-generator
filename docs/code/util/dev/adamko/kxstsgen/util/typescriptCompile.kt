package dev.adamko.kxstsgen.util

import com.github.pgreze.process.ProcessResult
import com.github.pgreze.process.Redirect
import com.github.pgreze.process.process
import java.io.InputStream
import java.nio.file.Path
import kotlin.io.path.createTempFile
import kotlin.io.path.inputStream
import kotlin.io.path.invariantSeparatorsPathString
import kotlin.io.path.nameWithoutExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext


@OptIn(ExperimentalCoroutinesApi::class)
suspend fun tsc2(
  typeScriptFile: Path,
): ProcessResult {
  return process(
    npxCmd,
    "-p", "typescript",
    "tsc",
    typeScriptFile.invariantSeparatorsPathString,
    "--noEmit",
    "--listFiles", // so we can check $file is compiled
    "--pretty", "false", // doesn't work?
    "--strict",
    "--target", "esnext", // required for Map<>

    stdout = Redirect.CAPTURE,
    stderr = Redirect.CAPTURE,
  )
}

suspend fun typescriptCompile(
  typeScriptFile: Path,
): Pair<Process, InputStream> {

  val logFile = createTempFile(
    typeScriptFile.parent,
    prefix = typeScriptFile.fileName.nameWithoutExtension.dropLastWhile { it.isDigit() } + "_",
    suffix = ".log",
  )

  val processBuilder = ProcessBuilder(
    npxCmd,
    "-p", "typescript",
    "tsc",
    typeScriptFile.invariantSeparatorsPathString,
    "--noEmit",
    "--listFiles", // so we can check $file is compiled
    "--pretty", "false", // doesn't work?
    "--strict",
    "--target", "esnext", // required for Map<>
  ).redirectErrorStream(true)
    .redirectOutput(logFile.toFile())

  return withContext(Dispatchers.IO) {
    processBuilder.start() to logFile.inputStream()
  }
}


private val npxCmd: String by lazy {
  // this is untested on non-Windows
  val hostOS = System.getProperty("os.name").lowercase()
  val cmd = when {
    "windows" in hostOS -> "npx.cmd"
    else                -> "npx"
  }
  npmInstallDir.resolve(cmd).invariantSeparatorsPathString
}


// must be set by Gradle
private val npmInstallDir: Path by lazy {
  Path.of(System.getenv("NPM_INSTALL_DIR"))
}
