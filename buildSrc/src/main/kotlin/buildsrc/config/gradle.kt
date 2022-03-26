package buildsrc.config

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.ProjectLayout
import org.gradle.kotlin.dsl.findByType
import org.gradle.plugins.ide.idea.model.IdeaModule
import org.jetbrains.kotlin.gradle.dsl.KotlinTargetContainerWithPresetFunctions
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension


/** exclude generated Gradle code, so it doesn't clog up search results */
fun IdeaModule.excludeGeneratedGradleDsl(layout: ProjectLayout) {
  excludeDirs.addAll(
    layout.files(
      "buildSrc/build/generated-sources/kotlin-dsl-accessors",
      "buildSrc/build/generated-sources/kotlin-dsl-accessors/kotlin",
      "buildSrc/build/generated-sources/kotlin-dsl-accessors/kotlin/gradle",
      "buildSrc/build/generated-sources/kotlin-dsl-external-plugin-spec-builders",
      "buildSrc/build/generated-sources/kotlin-dsl-external-plugin-spec-builders/kotlin",
      "buildSrc/build/generated-sources/kotlin-dsl-external-plugin-spec-builders/kotlin/gradle",
      "buildSrc/build/generated-sources/kotlin-dsl-plugins",
      "buildSrc/build/generated-sources/kotlin-dsl-plugins/kotlin",
      "buildSrc/build/generated-sources/kotlin-dsl-plugins/kotlin/dev",
      "buildSrc/build/pluginUnderTestMetadata",
    )
  )
}


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

fun KotlinTargetContainerWithPresetFunctions.currentHostTarget(
  targetName: String = "native",
  configure: KotlinNativeTargetWithHostTests.() -> Unit,
): KotlinNativeTargetWithHostTests {
  val hostOs = System.getProperty("os.name")
  val isMingwX64 = hostOs.startsWith("Windows")
  val hostTarget = when {
    hostOs == "Mac OS X" -> macosX64(targetName)
    hostOs == "Linux"    -> linuxX64(targetName)
    isMingwX64           -> mingwX64(targetName)
    else                 -> throw GradleException("Preset for host OS '$hostOs' is undefined")
  }
  println("Current host target ${hostTarget.targetName}/${hostTarget.preset?.name}")
  hostTarget.configure()
  return hostTarget
}
