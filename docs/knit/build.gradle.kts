plugins {
  buildsrc.convention.`kotlin-jvm`
  kotlin("plugin.serialization")
  id("org.jetbrains.kotlinx.knit")
}


val kotlinxSerializationVersion = "1.3.2"

dependencies {
  implementation(projects.modules.kxsTsGenCore)

  implementation(platform("org.jetbrains.kotlinx:kotlinx-serialization-bom:${kotlinxSerializationVersion}"))
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-core")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")

  implementation("org.jetbrains.kotlinx:kotlinx-knit:0.3.0")

  testImplementation(kotlin("test"))

  testImplementation("org.jetbrains.kotlinx:kotlinx-knit-test:0.3.0")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  kotlinOptions.freeCompilerArgs += listOf(
    "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
  )
}

sourceSets.test {
  java.srcDirs("example", "test")
}

knit {
  val docsDir = rootProject.layout.projectDirectory.dir("docs")
  rootDir = docsDir.asFile
  files = project.fileTree(docsDir) {
    include("*.md")
  }
}

tasks.test {
  dependsOn(tasks.knit)
//  finalizedBy(tasks.knitCheck)
}

tasks.compileKotlin { mustRunAfter(tasks.knit) }

//tasks.knitCheck {
//  dependsOn(tasks.test)
//}
