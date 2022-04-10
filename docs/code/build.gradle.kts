import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  buildsrc.convention.`kotlin-jvm`
  buildsrc.convention.node
  kotlin("plugin.serialization")
  id("org.jetbrains.kotlinx.knit")
}

val kotlinxSerializationVersion = "1.3.2"

dependencies {
  implementation(projects.modules.kxsTsGenCore)

  implementation(platform("org.jetbrains.kotlinx:kotlinx-serialization-bom:${kotlinxSerializationVersion}"))
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-core")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")

  implementation("org.jetbrains.kotlinx:kotlinx-knit:0.3.0")

  implementation(kotlin("reflect"))

  testImplementation(kotlin("test"))

  testImplementation("org.jetbrains.kotlinx:kotlinx-knit-test:0.3.0")
  testImplementation("com.github.pgreze:kotlin-process:1.3.1")
}

tasks.withType<KotlinCompile> {
  mustRunAfter(tasks.knit)
  kotlinOptions.freeCompilerArgs += listOf(
    "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
  )
}

sourceSets.test {
  java.srcDirs(
    "example",
    "test",
    "util",
  )
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
  systemProperty("kotest.tags", "!TSCompile")
}
