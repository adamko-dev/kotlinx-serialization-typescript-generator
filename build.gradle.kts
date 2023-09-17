import buildsrc.config.excludeGeneratedGradleDsl

plugins {
  idea
  id("org.jetbrains.kotlinx.kover") version "0.6.1"
  id("buildsrc.convention.base")
}


project.group = "dev.adamko.kxstsgen"
project.version = gitVersion

idea {
  module {
    excludeGeneratedGradleDsl(layout)
    excludeDirs = excludeDirs + layout.files(
      ".idea",
      "gradle/kotlin-js-store",
      "gradle/wrapper",
      "site/.docusaurus",
    )
  }
}

val projectVersion by tasks.registering {
  description = "prints the project version"
  group = "help"
  val version = providers.provider { project.version }
  doLast {
    logger.quiet("${version.orNull}")
  }
}
