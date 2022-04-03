package buildsrc.convention

import buildsrc.config.relocateKotlinJsStore


plugins {
  id("buildsrc.convention.subproject")
  kotlin("multiplatform")
  `java-library`
}


relocateKotlinJsStore()


kotlin {
  targets.all {
    compilations.all {
      kotlinOptions {
        languageVersion = "1.6"
        apiVersion = "1.6"
      }
    }
  }
}
