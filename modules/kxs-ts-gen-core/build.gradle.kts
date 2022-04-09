import buildsrc.config.publicationsFromMainHost


plugins {
  buildsrc.convention.`kotlin-multiplatform`
  buildsrc.convention.`maven-publish`
  kotlin("plugin.serialization")
//  id("org.jetbrains.reflekt")
  id("io.kotest.multiplatform")
}

val kotlinxSerializationVersion = "1.3.2"
val kotestVersion = "5.2.2"

kotlin {

//  js(IR) {
//    binaries.executable()
//    browser {
//      commonWebpackConfig {
//        cssSupport.enabled = true
//      }
//    }
//  }

  jvm {
    compilations.all {
      kotlinOptions {
        jvmTarget = "11"
      }
    }
    withJava()
    testRuns["test"].executionTask.configure {
      useJUnitPlatform()
    }
  }

//  publishing {
//    publications {
//      matching { it.name in publicationsFromMainHost() }.all {
//        val targetPublication = this@all
//        tasks.withType<AbstractPublishToMaven>()
//          .matching { it.publication == targetPublication }
//          .configureEach { onlyIf { findProperty("isMainHost") == "true" } }
//      }
//    }
//  }

  sourceSets {

    all {
      languageSettings.apply {
        optIn("kotlin.RequiresOptIn")
        optIn("kotlin.ExperimentalStdlibApi")
        optIn("kotlin.time.ExperimentalTime")
        optIn("kotlinx.serialization.ExperimentalSerializationApi")
      }
    }

    val commonMain by getting {
      dependencies {
        implementation(
          project.dependencies.platform(
            "org.jetbrains.kotlinx:kotlinx-serialization-bom:${kotlinxSerializationVersion}"
          )
        )
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-core")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")
        implementation(kotlin("reflect"))
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))

        implementation("io.kotest:kotest-assertions-core:$kotestVersion")
        implementation("io.kotest:kotest-assertions-json:$kotestVersion")
        implementation("io.kotest:kotest-property:$kotestVersion")
        implementation("io.kotest:kotest-framework-engine:$kotestVersion")
        implementation("io.kotest:kotest-framework-datatest:$kotestVersion")
      }
    }
//    val nativeMain by getting
//    val nativeTest by getting
//    val jsMain by getting
//    val jsTest by getting
    val jvmMain by getting {
      dependencies {
        implementation(kotlin("reflect"))
      }
    }

    val jvmTest by getting {
      dependencies {
        implementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
      }
    }
  }
}
