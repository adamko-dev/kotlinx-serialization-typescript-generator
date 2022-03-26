@Suppress("UnstableApiUsage") // centralised repository definitions are incubating
dependencyResolutionManagement {

  repositories {
    myMavenLocal()
    mavenCentral()
    jitpack()
    gradlePluginPortal()
  }

  pluginManagement {
    repositories {
      myMavenLocal()
      jitpack()
      gradlePluginPortal()
      mavenCentral()
    }
  }
}


fun RepositoryHandler.jitpack() {
  maven("https://jitpack.io")
}


fun RepositoryHandler.myMavenLocal(enabled: Boolean = true) {
  if (enabled) {
    logger.lifecycle("Maven local is enabled")
    mavenLocal {
      content {
        includeGroup("org.jetbrains.reflekt")
      }
    }
  }
}
