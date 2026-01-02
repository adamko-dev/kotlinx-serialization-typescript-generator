plugins {
  buildsrc.convention.`kotlin-jvm`
  buildsrc.convention.node
  buildsrc.convention.`knit-tests`
  kotlin("plugin.serialization")
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

  testImplementation("io.kotest:kotest-runner-junit5")
  testImplementation("io.kotest:kotest-assertions-core")
  testImplementation("io.kotest:kotest-property")
}

kotlin {
  sourceSets {
    configureEach {
      languageSettings {
//        optIn("kotlin.ExperimentalStdlibApi")
//        optIn("kotlin.time.ExperimentalTime")
//        optIn("kotlinx.serialization.ExperimentalSerializationApi")
      }
    }
  }
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

tasks.withType<Test>().configureEach {
  dependsOn(tasks.knit)
}

tasks.test {
  // TSCompile tests are slow, they don't need to run every time
  if (kxsTsGenSettings.enableTsCompileTests.get()) {
    val npmInstallDir = tasks.npmSetup.map { it.npmDir.get().asFile.canonicalPath }
    inputs.dir(npmInstallDir)
    environment("NPM_INSTALL_DIR", npmInstallDir.get())
  } else {
    systemProperty("kotest.tags", "!TSCompile")
  }
}

val prepOutgoingKnitDocs by tasks.registering(Sync::class) {
  description = "prepares Knit Markdown files for sharing with other subprojects"
  dependsOn(tasks.knit, tasks.knitCheck)

  from(knit.files)
  into(temporaryDir)
}

configurations.knitDocsElements.configure {
  outgoing {
    artifact(prepOutgoingKnitDocs.map { it.destinationDir })
  }
}
