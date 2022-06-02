plugins {
  buildsrc.convention.`kotlin-mpp`
  buildsrc.convention.`maven-publish`
  kotlin("plugin.serialization")
  id("io.kotest.multiplatform")
}

kotlin {

  js(IR) {
    binaries.executable()
//    browser {
//      commonWebpackConfig {
//        cssSupport.enabled = true
//      }
//    }
    nodejs()
  }

  jvm {
    compilations.all {
      kotlinOptions {
        jvmTarget = "1.8"
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
      languageSettings {
        optIn("kotlin.RequiresOptIn")
        optIn("kotlin.ExperimentalStdlibApi")
        optIn("kotlin.time.ExperimentalTime")
        optIn("kotlinx.serialization.ExperimentalSerializationApi")
      }
    }

    val commonMain by getting {
      dependencies {
        implementation(project.dependencies.platform(projects.modules.versionsPlatform))
        implementation(libs.kotlinx.serialization.core)
        implementation(libs.kotlinx.serialization.json)
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))

        implementation(libs.kotest.assertionsCore)
        implementation(libs.kotest.assertionsJson)
        implementation(libs.kotest.property)
        implementation(libs.kotest.frameworkEngine)
        implementation(libs.kotest.frameworkDatatest)

        implementation(libs.kotlinx.serialization.cbor)
        implementation(libs.okio.core)
      }
    }
//    val nativeMain by getting
//    val nativeTest by getting
    val jsMain by getting
    val jsTest by getting
    val jvmMain by getting {
      dependencies {
        implementation(project.dependencies.platform(projects.modules.versionsPlatform))
        implementation(kotlin("reflect"))
      }
    }

    val jvmTest by getting {
      dependencies {
        implementation(libs.kotest.frameworkEngine)
        implementation(libs.kotest.runnerJUnit5)
      }
    }
  }
}

//
//val javadocJar by tasks.creating(Jar::class) {
//  group = JavaBasePlugin.DOCUMENTATION_GROUP
//  description = "Assembles java doc to jar"
//  archiveClassifier.set("javadoc")
//  from(tasks.javadoc)
//}
//

//publishing {
//  publications.withType<MavenPublication>().configureEach {
////    artifact(javadocJar)
//  }
//}
