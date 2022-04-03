package dev.adamko.kxstsgen.util

/**
 * * filter out lines that are `//` comments
 * * convert whitespace-only lines to an empty string
 * * remove `//` comments
 * * remove trailing whitespace
 */
fun String.normalize(): String =
  lines()
    .filterNot { it.trimStart().startsWith("//") }
    .joinToString("\n") {
      it.substringBeforeLast("//")
        .trimEnd()
        .ifBlank { "" }
    }

/** [normalize] each String, then [join][joinToString] them */
fun Iterable<String>.normalizeJoin(): String =
  joinToString("\n") { it.normalize() }
