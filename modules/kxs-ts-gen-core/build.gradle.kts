plugins {
  kotlin("multiplatform")
  `java-library`
  kotlin("plugin.serialization")
}

val kotlinxSerializationVersion = "1.3.2"

kotlin {
  val hostOs = System.getProperty("os.name")
  val isMingwX64 = hostOs.startsWith("Windows")
  val nativeTarget = when {
    hostOs == "Mac OS X" -> macosX64("native")
    hostOs == "Linux"    -> linuxX64("native")
    isMingwX64           -> mingwX64("native")
    else                 -> throw GradleException("Host OS is not supported in Kotlin/Native.")
  }


  js(IR) {
    binaries.executable()
    browser {
      commonWebpackConfig {
        cssSupport.enabled = true
      }
    }
  }
  jvm {
    compilations.all {
      kotlinOptions.jvmTarget = "11"
    }
    withJava()
    testRuns["test"].executionTask.configure {
      useJUnitPlatform()
    }
  }
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
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }
    val nativeMain by getting
    val nativeTest by getting
    val jsMain by getting
    val jsTest by getting
    val jvmMain by getting
    val jvmTest by getting
  }
}
