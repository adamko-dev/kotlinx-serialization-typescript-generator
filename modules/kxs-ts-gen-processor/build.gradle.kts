import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  buildsrc.convention.`kotlin-jvm`
//  kotlin("plugin.serialization")

//  id("org.jetbrains.reflekt")
}

val kspVersion = "1.6.10-1.0.4"
val kotlinCompileTestingVersion = "1.4.7"
val kotlinxSerializationVersion = "1.3.2" // TODO put dependencies in libs.version.toml

dependencies {
  implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")

  testImplementation("com.github.tschuchortdev:kotlin-compile-testing:$kotlinCompileTestingVersion")
  testImplementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:$kotlinCompileTestingVersion")

  implementation(projects.modules.kxsTsGenCore)

  implementation(platform("org.jetbrains.kotlinx:kotlinx-serialization-bom:${kotlinxSerializationVersion}"))
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-core")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")

  testImplementation(kotlin("test"))
}

tasks.withType<KotlinCompile> {
  kotlinOptions.freeCompilerArgs += listOf(
    "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
  )
}
