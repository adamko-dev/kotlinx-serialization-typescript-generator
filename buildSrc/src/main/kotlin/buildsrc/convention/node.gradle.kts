package buildsrc.convention

plugins {
  id("com.github.node-gradle.node")
  id("buildsrc.convention.subproject")
}

val rootGradleDir: Directory by extra {
  rootProject.layout.projectDirectory.dir(".gradle")
}

node {
  download.set(true)

  distBaseUrl.set(null as String?) // set in repositories.settings.gradle.kts

  workDir.set(rootGradleDir.dir("nodejs"))
  npmWorkDir.set(rootGradleDir.dir("npm"))
  yarnWorkDir.set(rootGradleDir.dir("yarn"))
}

tasks.withType<Test>().configureEach {
  val npmInstallDir = tasks.npmSetup.map { it.npmDir.get().asFile.canonicalPath }
  inputs.dir(npmInstallDir)

  doFirst {
    environment("NPM_INSTALL_DIR", npmInstallDir.get())
  }
}
