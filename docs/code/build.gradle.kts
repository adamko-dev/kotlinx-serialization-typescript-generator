import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  buildsrc.convention.`kotlin-jvm`
  buildsrc.convention.node
  kotlin("plugin.serialization")
  id("org.jetbrains.kotlinx.knit")
  `java-test-fixtures`
}

dependencies {
  implementation(platform(projects.modules.versionsPlatform))
  implementation(projects.modules.kxsTsGenCore)
  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.kotlinx.knit)
  implementation(kotlin("reflect"))

  testImplementation(kotlin("test"))
  testImplementation(libs.kotlinx.knit.test)

  testFixturesImplementation(platform(projects.modules.versionsPlatform))
  testFixturesImplementation(libs.kotlinProcess)
  testFixturesImplementation(libs.kotest.frameworkEngine)
  testFixturesImplementation(libs.kotest.assertionsCore)
}

tasks.withType<KotlinCompile>().configureEach {
  mustRunAfter(tasks.knit)
  kotlinOptions.freeCompilerArgs += listOf(
    "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
  )
}

sourceSets.main {
  java.srcDirs("example")
}

sourceSets.test {
  java.srcDirs("test")
}

sourceSets.testFixtures {
  java.srcDirs("util")
}

knit {
  val docsDir = rootProject.layout.projectDirectory.dir("docs")
  rootDir = docsDir.asFile
  files = project.fileTree(docsDir) {
    include("*.md")
  }
}

tasks.withType<Test>().configureEach { dependsOn(tasks.knit) }

tasks.test {
  // TSCompile tests are slow, they don't need to run every time
  if (!kxsTsGenSettings.enableTsCompileTests.get()) {
    systemProperty("kotest.tags", "!TSCompile")
  }
}
