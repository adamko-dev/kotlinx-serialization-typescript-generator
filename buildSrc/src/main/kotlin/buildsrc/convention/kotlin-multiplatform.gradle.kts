package buildsrc.convention

import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension


plugins {
  id("buildsrc.convention.subproject")
  kotlin("multiplatform")
  `java-library`
}

// `kotlin-js` adds a directory in the root-dir for the Yarn lock.
// That's a bit annoying. It's a little neater if it's in the
// gradle dir, next to the version-catalog.
afterEvaluate {
  rootProject.extensions.configure<YarnRootExtension> {
    lockFileDirectory = project.rootDir.resolve("gradle/kotlin-js-store")
  }
}
