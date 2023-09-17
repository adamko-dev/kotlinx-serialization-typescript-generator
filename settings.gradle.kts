@file:Suppress("UnstableApiUsage")

rootProject.name = "kotlinx-serialization-typescript-generator"

pluginManagement {
  includeBuild("build-tools/build-plugins")
  includeBuild("build-tools/settings-plugins")
  repositories {
    mavenCentral()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)

  repositories {
    mavenCentral()

    // Declare the Node.js & Yarn download repositories
    // Required by Gradle Node plugin: https://github.com/node-gradle/gradle-node-plugin/blob/3.5.1/docs/faq.md#is-this-plugin-compatible-with-centralized-repositories-declaration
    exclusiveContent {
      forRepository {
        ivy("https://cache-redirector.jetbrains.com/nodejs.org/dist/") {
          name = "Node Distributions at $url"
          patternLayout { artifact("v[revision]/[artifact](-v[revision]-[classifier]).[ext]") }
          metadataSources { artifact() }
          content { includeModule("org.nodejs", "node") }
        }
      }
      filter { includeGroup("org.nodejs") }
    }

    exclusiveContent {
      forRepository {
        ivy("https://github.com/yarnpkg/yarn/releases/download") {
          name = "Yarn Distributions at $url"
          patternLayout { artifact("v[revision]/[artifact](-v[revision]).[ext]") }
          metadataSources { artifact() }
          content { includeModule("com.yarnpkg", "yarn") }
        }
      }
      filter { includeGroup("com.yarnpkg") }
    }
  }
}

plugins {
  id("kxstsgen.settings.git-version-setup")
}

include(
  ":modules:kxs-ts-gen-core",
  ":modules:kxs-ts-gen-gradle-plugin",
  ":modules:kxs-ts-gen-processor",
  ":modules:versions-platform",
  ":docs:code",
  ":site",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

////region git versioning
//
//gradle.allprojects {
//  plugins.apply("kxstsgen.settings.git-version")
//
////  val gitVersion = providers.of(GitVersionSource::class) {
////    parameters {
////      projectRootDir.set(objects.directoryProperty().fileValue(rootDir))
////      gitDescribe.convention(exec {
////        commandLine(
////          "git",
////          "describe",
////          "--always",
////          "--tags",
////          "--dirty=-DIRTY",
////          "--broken=-BROKEN",
////          "--match=v[0-9]*\\.[0-9]*\\.[0-9]*",
////        )
////      })
////      currentBranchName.convention(exec {
////        commandLine(
////          "git",
////          "branch",
////          "--show-current",
////        )
////      })
////      currentCommitHash.convention(exec {
////        commandLine(
////          "git",
////          "rev-parse",
////          "--short",
////          "HEAD",
////        )
////      })
////    }
////  }
//  extensions.add<Provider<String>>("gitVersion", providers.provider { "unknown" })
//}
////endregion
