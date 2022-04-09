@Suppress("UnstableApiUsage") // centralised repository definitions are incubating
dependencyResolutionManagement {

  repositories {
    myMavenLocal()
    mavenCentral()
    jitpack()
    gradlePluginPortal()

    // Declare the Node.js download repository
    ivy("https://nodejs.org/dist/") {
      name = "Node Distributions at $url"
      patternLayout { artifact("v[revision]/[artifact](-v[revision]-[classifier]).[ext]") }
      metadataSources { artifact() }
      content { includeModule("org.nodejs", "node") }
    }
    ivy("https://github.com/yarnpkg/yarn/releases/download") {
      name = "Yarn Distributions at $url"
      patternLayout { artifact("v[revision]/[artifact](-v[revision]).[ext]") }
      metadataSources { artifact() }
      content { includeModule("com.yarnpkg", "yarn") }
    }
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


fun RepositoryHandler.myMavenLocal(enabled: Boolean = false) {
  if (enabled) {
    logger.lifecycle("Maven local is enabled")
    mavenLocal {
      content {
//        includeGroup("org.jetbrains.reflekt")
      }
    }
  }
}
