import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  idea
  `kotlin-dsl`
  kotlin("jvm") version "1.6.10"
  `project-report`
}


object Versions {
  const val jvmTarget = "11"
  const val kotlin = "1.6.10"
  const val kotlinTarget = "1.6"
  const val kotlinxKnit = "0.3.0"
  const val kotlinxKover = "0.5.0"
  const val kotlinxSerialization = "1.3.2"
  const val ksp = "1.6.10-1.0.4"

  const val kotest = "5.1.0"
}


dependencies {
  implementation(enforcedPlatform("org.jetbrains.kotlin:kotlin-bom:${Versions.kotlin}"))
  implementation("org.jetbrains.kotlin:kotlin-serialization")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")

  implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:${Versions.ksp}")

  implementation(platform("org.jetbrains.kotlinx:kotlinx-serialization-bom:${Versions.kotlinxSerialization}"))

  implementation("io.kotest:kotest-framework-multiplatform-plugin-gradle:${Versions.kotest}")

  implementation("org.jetbrains.kotlinx:kotlinx-knit:${Versions.kotlinxKnit}")
  implementation("org.jetbrains.kotlinx:kover:${Versions.kotlinxKover}")

  implementation("org.jetbrains.reflekt:gradle-plugin:1.6.10-1-SNAPSHOT") {
    isChanging = true
  }
}


tasks.withType<KotlinCompile>().configureEach {

  kotlinOptions {
    jvmTarget = Versions.jvmTarget
    apiVersion = Versions.kotlinTarget
    languageVersion = Versions.kotlinTarget
  }

  kotlinOptions.freeCompilerArgs += listOf(
    "-opt-in=kotlin.RequiresOptIn",
    "-opt-in=kotlin.ExperimentalStdlibApi",
    "-opt-in=kotlin.time.ExperimentalTime",
  )
}

kotlin {
  jvmToolchain {
    (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(Versions.jvmTarget))
  }

  kotlinDslPluginOptions {
    jvmTarget.set(Versions.jvmTarget)
  }
}

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}
