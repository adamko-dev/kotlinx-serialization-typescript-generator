import buildsrc.config.excludeGeneratedGradleDsl

plugins {
  idea
  id("me.qoomon.git-versioning")
  id("org.jetbrains.kotlinx.kover")
  buildsrc.convention.base
}


project.group = "dev.adamko.kxstsgen"
project.version = "0.0.0-SNAPSHOT"
gitVersioning.apply {
  refs {
    considerTagsOnBranches = true
    branch(".+") { version = "\${ref}-SNAPSHOT" }
    tag("v(?<version>.*)") { version = "\${ref.version}" }
  }

  // optional fallback configuration in case of no matching ref configuration
  rev { version = "\${commit}" }
}

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
