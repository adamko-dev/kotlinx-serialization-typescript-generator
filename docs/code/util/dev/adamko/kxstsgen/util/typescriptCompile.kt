package dev.adamko.kxstsgen.util

import com.github.pgreze.process.ProcessResult
import com.github.pgreze.process.Redirect
import com.github.pgreze.process.process
import java.nio.file.Path
import kotlin.io.path.invariantSeparatorsPathString
import kotlinx.coroutines.ExperimentalCoroutinesApi


@OptIn(ExperimentalCoroutinesApi::class)
suspend fun typescriptCompile(
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


private val npxCmd: String by lazy {
  // this is untested on non-Windows
  val hostOS = System.getProperty("os.name").lowercase()
  val cmd = when {
    "win" in hostOS -> "npx.cmd"
    else            -> "bin/npx"
  }
  npmInstallDir.resolve(cmd).invariantSeparatorsPathString
}


// must be set by Gradle
private val npmInstallDir: Path by lazy {
  Path.of(
    System.getenv()["NPM_INSTALL_DIR"] ?: error("NPM_INSTALL_DIR is not set")
  )
}
