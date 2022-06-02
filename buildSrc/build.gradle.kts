import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  idea
  `kotlin-dsl`
  kotlin("jvm") version "1.6.21"
}


dependencies {
  implementation(enforcedPlatform(libs.kotlin.bom))
  implementation("org.jetbrains.kotlin:kotlin-serialization")
//  implementation("org.jetbrains.kotlin:kotlin-reflect")

  implementation(libs.kotlin.gradlePlugin)
  implementation(libs.kotlinSymbolProcessing.gradlePlugin)

  implementation(libs.kotest.gradlePlugin)

  implementation(libs.kotlinx.kover.gradlePlugin)

  implementation(libs.kotlinx.knit.gradlePlugin)

  implementation(libs.gradleNodePlugin)

  implementation(libs.qoomonGitVersioning)
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
    (this as JavaToolchainSpec).languageVersion.set(
      libs.versions.jvmTarget.map {
        JavaLanguageVersion.of(
          it.substringAfter(".")
        )
      }
    )
  }

  kotlinDslPluginOptions {
    jvmTarget.set(libs.versions.jvmTarget)
  }
}

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}
