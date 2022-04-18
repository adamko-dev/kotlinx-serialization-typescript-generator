import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  idea
  `kotlin-dsl`
  kotlin("jvm") version "1.6.20"
  `project-report`
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
}

val gradleJvmTarget = "11"
val gradleKotlinTarget = "1.6"

tasks.withType<KotlinCompile>().configureEach {

  kotlinOptions {
    jvmTarget = gradleJvmTarget
    apiVersion = gradleKotlinTarget
    languageVersion = gradleKotlinTarget
  }

  kotlinOptions.freeCompilerArgs += listOf(
    "-opt-in=kotlin.RequiresOptIn",
    "-opt-in=kotlin.ExperimentalStdlibApi",
    "-opt-in=kotlin.time.ExperimentalTime",
  )
}

kotlin {
  jvmToolchain {
    (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(gradleJvmTarget))
  }

  kotlinDslPluginOptions {
    jvmTarget.set(gradleJvmTarget)
  }
}

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}
