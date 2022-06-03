import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  buildsrc.convention.`kotlin-jvm`
}

description = "Experimental alternative to Kotlinx Serialization. Currently unused."


dependencies {
  implementation(platform(projects.modules.versionsPlatform))

  implementation(libs.kotlinSymbolProcessing)

  testImplementation(libs.kotlinCompileTesting)
  testImplementation(libs.kotlinCompileTesting.ksp)

  implementation(projects.modules.kxsTsGenCore)

  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.serialization.json)

  testImplementation(kotlin("test"))
}


tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions.freeCompilerArgs += listOf(
    "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
  )
}
