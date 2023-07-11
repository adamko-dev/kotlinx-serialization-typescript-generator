plugins {
  `kotlin-dsl`
}

dependencies {
  implementation(platform(libs.kotlin.bom))

  implementation(libs.gradlePlugin.kotlin)
  implementation(libs.gradlePlugin.kotlinSerialization)
  implementation(libs.gradlePlugin.gitVersioning)
  implementation(libs.gradlePlugin.gradleNode)
  implementation(libs.gradlePlugin.kotlinxKover)
  implementation(libs.gradlePlugin.kotlinxKnit)
  implementation(libs.gradlePlugin.kotest)
  implementation(libs.gradlePlugin.kotlinSymbolProcessing)

  implementation("org.jetbrains:markdown:0.3.5")
  implementation("org.jetbrains.kotlinx:kotlinx-html:0.9.0")
}

kotlin {
  jvmToolchain {
    languageVersion.set(
      libs.versions.jvmTarget.map {
        JavaLanguageVersion.of(it)
      }
    )
  }
}
