import buildsrc.config.excludeGeneratedGradleDsl

plugins {
  idea
  id("org.jetbrains.kotlinx.kover")
  buildsrc.convention.base
}


project.group = "dev.adamko.kxstsgen"
project.version = object {
  private val gitVersion = project.gitVersion
  override fun toString(): String = gitVersion.get()
}

idea {
  module {
    excludeGeneratedGradleDsl(layout)
    excludeDirs = excludeDirs + layout.files(
      ".idea",
      ".kotlin",
      "buildSrc/.kotlin",
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
  inputs.property("version", version)
  outputs.cacheIf("logging task, it should always run") { false }
  doLast {
    logger.quiet("${version.orNull}")
  }
}
