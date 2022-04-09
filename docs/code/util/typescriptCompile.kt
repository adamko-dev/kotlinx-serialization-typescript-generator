package dev.adamko.kxstsgen.util

import java.nio.file.Path
import java.util.concurrent.TimeUnit
import kotlin.io.path.createTempFile
import kotlin.io.path.invariantSeparatorsPathString
import kotlin.io.path.writeText


fun typescriptCompile(
  typeScriptSrcCode: String
): String {
  val file: Path = createTempFile("kxstsgen", ".d.ts")
  file.writeText(typeScriptSrcCode)

  val process: Process = ProcessBuilder(
    npxCmd,
    "-p",
    "typescript",
    "tsc",
    file.invariantSeparatorsPathString,
    "--noEmit",
    "--listFiles",
    "--target", "esnext", // required for Map<>
  )
    .start()

  process.waitFor(5, TimeUnit.SECONDS)

  return process.inputStream.readAllBytes().decodeToString() +
    process.errorStream.readAllBytes().decodeToString()
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
