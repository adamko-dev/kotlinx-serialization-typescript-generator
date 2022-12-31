import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
  implementation("org.jetbrains.kotlinx:kotlinx-html:0.8.0")
}


tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions {
    jvmTarget = libs.versions.jvmTarget.get()
    apiVersion = libs.versions.kotlinTarget.get()
    languageVersion = libs.versions.kotlinTarget.get()
  }

  kotlinOptions.freeCompilerArgs += listOf(
    "-opt-in=kotlin.RequiresOptIn",
    "-opt-in=kotlin.ExperimentalStdlibApi",
    "-opt-in=kotlin.time.ExperimentalTime",
  )
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

kotlinDslPluginOptions {
  jvmTarget.set(libs.versions.jvmTarget)
}
