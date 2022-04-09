import com.github.gradle.node.NodePlugin
import com.github.gradle.node.npm.task.NpmTask
import com.github.gradle.node.npm.task.NpxTask


plugins {
  buildsrc.convention.`kotlin-jvm`
  buildsrc.convention.node
  kotlin("plugin.serialization")
  id("org.jetbrains.kotlinx.knit")
}


node {
  nodeProjectDir.set(layout.buildDirectory.dir("node"))
}


val npmInit by tasks.registering(NpmTask::class) {
  group = NodePlugin.NPM_GROUP
  dependsOn(tasks.nodeSetup)
  npmCommand.set(listOf("init"))
  args.set(listOf("--yes"))
  ignoreExitValue.set(false)
  execOverrides { standardOutput = System.out }
}

val npmInstallTypeScript by tasks.registering(NpmTask::class) {
  group = NodePlugin.NPM_GROUP
  dependsOn(npmInit)
  npmCommand.set(listOf("install"))
  args.set(listOf("typescript"))
  ignoreExitValue.set(false)
  execOverrides { standardOutput = System.out }
//  inputs.dir("node_modules")
//  inputs.file("package.json")
//  inputs.dir("src")
//  inputs.dir("test")
}

tasks.test { dependsOn(npmInstallTypeScript) }

val npxInstallTypeScript by tasks.registering(NpxTask::class) {
  group = NodePlugin.NPM_GROUP
  dependsOn(tasks.nodeSetup)
  command.set("cowsay")
  args.set(listOf("moo"))
  ignoreExitValue.set(false)
  execOverrides { standardOutput = System.out }
}


val kotlinxSerializationVersion = "1.3.2"

dependencies {
  implementation(projects.modules.kxsTsGenCore)

  implementation(platform("org.jetbrains.kotlinx:kotlinx-serialization-bom:${kotlinxSerializationVersion}"))
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-core")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")

  implementation("org.jetbrains.kotlinx:kotlinx-knit:0.3.0")

  implementation(kotlin("reflect"))


  testImplementation(kotlin("test"))

  testImplementation("org.jetbrains.kotlinx:kotlinx-knit-test:0.3.0")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
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

tasks.test {
  dependsOn(tasks.knit)
//  finalizedBy(tasks.knitCheck)
}

tasks.compileKotlin { mustRunAfter(tasks.knit) }

//tasks.knitCheck {
//  dependsOn(tasks.test)
//}
