package buildsrc.convention

import buildsrc.config.relocateKotlinJsStore
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget


plugins {
  id("buildsrc.convention.base")
  kotlin("multiplatform")
}


relocateKotlinJsStore()


//kotlin {
extensions.configure<KotlinMultiplatformExtension> {
  targets.configureEach {
    compilations.configureEach {
      kotlinOptions {
        languageVersion = "1.7"
        apiVersion = "1.7"
      }
    }
  }

  targets.withType<KotlinJvmTarget> {
    compilations.configureEach {
      kotlinOptions {
        jvmTarget = "11"
      }
    }
    testRuns.configureEach {
      executionTask.configure {
        useJUnitPlatform()
      }
    }
  }
}
