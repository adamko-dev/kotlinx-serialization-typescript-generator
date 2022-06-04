package buildsrc.config

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.PasswordCredentials
import org.gradle.api.file.ProjectLayout
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.kotlin.dsl.findByType
import org.gradle.plugins.ide.idea.model.IdeaModule
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget


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


// https://github.com/gradle/gradle/issues/20925
fun ProviderFactory.credentialsAction(
  repositoryName: String
): Provider<Action<PasswordCredentials>> = zip(
  gradleProperty("${repositoryName}Username"),
  gradleProperty("${repositoryName}Password"),
)  { user, pass ->
  Action<PasswordCredentials> {
    username = user
    password = pass
  }
}


/** Logic from [KotlinJvmTarget.withJava] */
fun Project.isKotlinMultiplatformJavaEnabled(): Boolean {
  val multiplatformExtension: KotlinMultiplatformExtension? =
    extensions.findByType(KotlinMultiplatformExtension::class)

  return multiplatformExtension?.targets
    ?.any { it is KotlinJvmTarget && it.withJavaEnabled }
    ?: false
}
