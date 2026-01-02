plugins {
  buildsrc.convention.`kotlin-mpp`
  buildsrc.convention.`maven-publish`
  kotlin("plugin.serialization")
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
        implementation(libs.kotlinx.serialization.cbor)
      }
    }
    commonTest {
      dependencies {
        implementation(kotlin("test"))

        implementation(libs.kotest.assertionsCore)
        implementation(libs.kotest.assertionsJson)
        implementation(libs.kotest.property)
        implementation(libs.kotest.frameworkEngine)

        implementation(libs.kotlinx.serialization.cbor)
        implementation(libs.okio.core)
      }
    }

    jvmMain {
      dependencies {
        implementation(project.dependencies.platform(projects.modules.versionsPlatform))
        implementation(kotlin("reflect"))
      }
    }

    jvmTest {
      dependencies {
//        implementation(libs.kotest.frameworkEngine)
        implementation(libs.kotest.runnerJUnit5)
      }
    }
  }
}
