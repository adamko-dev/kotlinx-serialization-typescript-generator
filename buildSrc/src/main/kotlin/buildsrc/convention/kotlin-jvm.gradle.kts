package buildsrc.convention

import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("buildsrc.convention.base")
  kotlin("jvm")
  `java-library`
}

dependencies {
  // versions provided by versions-platform subproject
  testImplementation("io.kotest:kotest-runner-junit5")
  testImplementation("io.kotest:kotest-assertions-core")
  testImplementation("io.kotest:kotest-property")
  testImplementation("io.kotest:kotest-framework-datatest")
}

kotlin {
  jvmToolchain(11)
}

java {
  withJavadocJar()
  withSourcesJar()
}

tasks.withType<KotlinCompile>().configureEach {
  compilerOptions {
    jvmTarget = JvmTarget.JVM_11
    apiVersion = KotlinVersion.KOTLIN_1_7
    languageVersion = KotlinVersion.KOTLIN_1_7

    freeCompilerArgs.addAll(
      "-opt-in=kotlin.ExperimentalStdlibApi",
      "-opt-in=kotlin.time.ExperimentalTime",
    )
  }
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
