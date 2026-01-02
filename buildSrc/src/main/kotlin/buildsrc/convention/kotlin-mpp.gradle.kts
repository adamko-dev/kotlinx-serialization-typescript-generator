package buildsrc.convention

import buildsrc.config.relocateKotlinJsStore
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile


plugins {
  id("buildsrc.convention.base")
  kotlin("multiplatform")
  id("io.kotest")
  id("com.google.devtools.ksp")
}


relocateKotlinJsStore()


// IJ still doesn't support script plugins???
extensions.configure<KotlinMultiplatformExtension> {
//kotlin {
  js {
    browser()
    nodejs()
  }

  jvmToolchain(21)

  compilerOptions {
    languageVersion = KOTLIN_2_0
    apiVersion = KOTLIN_2_0
  }

  jvm {
    compilerOptions {
      jvmTarget = JvmTarget.JVM_11
      freeCompilerArgs.add(jvmTarget.map { target ->
        "-Xjdk-release=${target.target}"
      })
    }
  }
}

tasks.withType<KotlinJvmCompile>().configureEach {
  compilerOptions {
    jvmTarget = JvmTarget.JVM_11
  }
}

tasks.withType<JavaCompile>().configureEach {
  targetCompatibility = "11"
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
}
