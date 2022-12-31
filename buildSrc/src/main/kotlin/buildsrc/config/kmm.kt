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


fun KotlinMultiplatformExtension.currentHostTarget(
  targetName: String = "native",
  configure: KotlinNativeTargetWithHostTests.() -> Unit,
): KotlinNativeTargetWithHostTests {
  val hostOs = System.getProperty("os.name")
  val hostTarget = when {
    hostOs == "Mac OS X"         -> macosX64(targetName)
    hostOs == "Linux"            -> linuxX64(targetName)
    hostOs.startsWith("Windows") -> mingwX64(targetName)
    else                         -> throw GradleException("Preset for host OS '$hostOs' is undefined")
  }

  println("Current host target ${hostTarget.targetName}/${hostTarget.preset?.name}")
  hostTarget.configure()
  return hostTarget
}


fun KotlinMultiplatformExtension.publicationsFromMainHost(): List<String> {
  return listOf(jvm(), js()).map { it.name } + "kotlinMultiplatform"
}
