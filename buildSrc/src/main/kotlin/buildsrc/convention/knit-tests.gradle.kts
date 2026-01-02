package buildsrc.convention

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("buildsrc.convention.base")
  id("buildsrc.convention.knit-files")
  id("org.jetbrains.kotlinx.knit")
}

tasks.withType<KotlinCompile>().configureEach {
  mustRunAfter(tasks.knit)
}
