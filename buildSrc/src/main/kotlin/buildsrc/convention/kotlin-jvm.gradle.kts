package buildsrc.convention

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_8

plugins {
  id("buildsrc.convention.base")
  kotlin("jvm")
  `java-library`
  id("io.kotest")
}

// IJ still doesn't support script plugins???
extensions.configure<KotlinJvmProjectExtension> {
//kotlin {
  jvmToolchain(21)
  compilerOptions {
    languageVersion = KOTLIN_1_8
    apiVersion = KOTLIN_1_8
    jvmTarget = JvmTarget.JVM_11
    freeCompilerArgs.add(jvmTarget.map { target ->
      "-Xjdk-release=${target.target}"
    })
    freeCompilerArgs.addAll(
      "-opt-in=kotlin.ExperimentalStdlibApi",
      "-opt-in=kotlin.time.ExperimentalTime",
    )
  }
}

java {
  withJavadocJar()
  withSourcesJar()
}
tasks.withType<JavaCompile>().configureEach {
  targetCompatibility = "11"
}

tasks.compileTestKotlin {
  compilerOptions {
    freeCompilerArgs.addAll(
      "-opt-in=io.kotest.common.ExperimentalKotest",
    )
  }
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
}
