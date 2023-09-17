plugins {
  `kotlin-dsl`
}

dependencies {
  implementation(platform(libs.kotlin.bom))

  implementation(libs.gradlePlugin.kotlin)
  implementation(libs.gradlePlugin.kotlinSerialization)
  implementation(libs.gradlePlugin.gradleNode)
  implementation(libs.gradlePlugin.kotlinxKover)
  implementation(libs.gradlePlugin.kotlinxKnit)
  implementation(libs.gradlePlugin.kotest)
  implementation(libs.gradlePlugin.kotlinSymbolProcessing)

  implementation("org.jetbrains:markdown:0.5.0")
  implementation("org.jetbrains.kotlinx:kotlinx-html:0.9.1")
}
