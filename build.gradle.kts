import buildsrc.config.excludeGeneratedGradleDsl

plugins {
  base
  id("me.qoomon.git-versioning") version "5.1.2"
  idea
  buildsrc.convention.`kotlin-jvm`
  kotlin("plugin.serialization")
  id("org.jetbrains.kotlinx.knit")
}


project.group = "dev.adamko"
project.version = "0.0.0-SNAPSHOT"
gitVersioning.apply {
  refs {
    branch(".+") { version = "\${ref}-SNAPSHOT" }
    tag("v(?<version>.*)") { version = "\${ref.version}" }
  }

  // optional fallback configuration in case of no matching ref configuration
  rev { version = "\${commit}" }
}


tasks.wrapper {
  gradleVersion = "7.4"
  distributionType = Wrapper.DistributionType.ALL
}

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
    excludeGeneratedGradleDsl(layout)
    excludeDirs = excludeDirs + layout.files(
      ".idea",
      "gradle/kotlin-js-store",
      "gradle/wrapper",
    )
  }
}


val kotlinxSerializationVersion = "1.3.2"

dependencies {
  implementation(projects.modules.kxsTsGenCore)

  implementation(platform("org.jetbrains.kotlinx:kotlinx-serialization-bom:${kotlinxSerializationVersion}"))
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-core")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")

  testImplementation(kotlin("test"))

  testImplementation("org.jetbrains.kotlinx:kotlinx-knit-test:0.3.0")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  kotlinOptions.freeCompilerArgs += listOf(
    "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
  )
}

sourceSets.test {
  java.srcDirs("docs/example", "docs/test")
}

//knit {
//  rootDir = layout.projectDirectory.asFile
//  files = rootProject.fileTree("docs")
//}

tasks.test {
  dependsOn(tasks.knit)
}

tasks.compileKotlin { mustRunAfter(tasks.knit) }
