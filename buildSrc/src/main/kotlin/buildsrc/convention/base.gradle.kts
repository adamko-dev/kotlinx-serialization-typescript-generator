package buildsrc.convention

import buildsrc.config.KxsTsGenBuildSettings

plugins {
  base
}

if (project != rootProject) {
  project.group = rootProject.group
  project.version = rootProject.version
}


extensions.create<KxsTsGenBuildSettings>(KxsTsGenBuildSettings.NAME)
