package buildsrc.convention

import buildsrc.config.relocateKotlinJsStore

plugins {
  id("buildsrc.convention.subproject")
  kotlin("multiplatform")
  `java-library`
}


relocateKotlinJsStore()
