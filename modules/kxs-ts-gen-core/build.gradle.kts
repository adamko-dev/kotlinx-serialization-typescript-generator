plugins {
  id("buildsrc.convention.kotlin-mpp")
  id("buildsrc.convention.maven-publish")
  kotlin("plugin.serialization")
  // id("io.kotest.multiplatform") // Kotest does not support nested JS tests https://github.com/kotest/kotest/issues/3141
}

kotlin {

  sourceSets {
    configureEach {
      languageSettings {
        optIn("dev.adamko.kxstsgen.core.UnimplementedKxsTsGenApi")
        optIn("kotlinx.serialization.ExperimentalSerializationApi")
      }
    }

    commonMain {
      dependencies {
        implementation(project.dependencies.platform(projects.modules.versionsPlatform))
        implementation(libs.kotlinx.serialization.core)
        implementation(libs.kotlinx.serialization.json)
      }
    }
    commonTest {
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

    jvmMain {
      dependencies {
        implementation(project.dependencies.platform(projects.modules.versionsPlatform))
        implementation(kotlin("reflect"))
      }
    }

    jvmTest {
      dependencies {
        implementation(libs.kotest.frameworkEngine)
        implementation(libs.kotest.runnerJUnit5)
      }
    }
  }
}
