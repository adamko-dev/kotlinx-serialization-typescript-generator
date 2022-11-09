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
