import buildsrc.config.excludeGeneratedGradleDsl

plugins {
  base
  idea
  id("me.qoomon.git-versioning") version "5.1.2"
  id("org.jetbrains.kotlinx.kover")
}


project.group = "dev.adamko"
project.version = "0.0.0-SNAPSHOT"
gitVersioning.apply {
  refs {
    branch(".+") { version = "\${ref}-SNAPSHOT" }
    tag("v(?<version>.*)") { version = "\${ref.version}" }
  }

  // optional fallback configuration in case of no matching ref configuration
  rev { version = "\${commit}" }
}


tasks.wrapper {
  gradleVersion = "7.4"
  distributionType = Wrapper.DistributionType.ALL
}

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
    excludeGeneratedGradleDsl(layout)
    excludeDirs = excludeDirs + layout.files(
      ".idea",
      "gradle/kotlin-js-store",
      "gradle/wrapper",
    )
  }
}
