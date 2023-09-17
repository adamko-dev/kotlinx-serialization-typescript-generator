package buildsrc.convention

import buildsrc.config.relocateKotlinJsStore
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget


plugins {
  id("buildsrc.convention.base")
  kotlin("multiplatform")
}


relocateKotlinJsStore()


kotlin {
  js(IR) {
    browser()
    nodejs()
  }

  jvm {
    withJava()
  }

  targets.configureEach {
    compilations.configureEach {
      kotlinOptions {
        languageVersion = "1.8"
        apiVersion = "1.8"
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
