package buildsrc.config

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension


/**
 * `kotlin-js` adds a directory in the root-dir for the Yarn lock.
 * That's a bit annoying. It's a little neater if it's in the
 * gradle dir, next to the version-catalog.
 */
fun Project.relocateKotlinJsStore() {
  afterEvaluate {
    rootProject.extensions.findByType<YarnRootExtension>()?.apply {
      lockFileDirectory = project.rootDir.resolve("gradle/kotlin-js-store")
    }
  }
}
