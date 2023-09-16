@file:Suppress("UnstableApiUsage")

rootProject.name = "kotlinx-serialization-typescript-generator"

pluginManagement {
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

//region git versioning
val gitDescribe: Provider<String> =
  providers
    .exec {
      workingDir(rootDir)
      commandLine(
        "git",
        "describe",
        "--always",
        "--tags",
        "--dirty=-SNAPSHOT",
        "--broken=-SNAPSHOT",
        "--match=v[0-9]*\\.[0-9]*\\.[0-9]*",
      )
      isIgnoreExitValue = true
    }.standardOutput.asText.map { it.trim() }

val currentBranchName: Provider<String> =
  providers
    .exec {
      workingDir(rootDir)
      commandLine(
        "git",
        "branch",
        "--show-current",
      )
      isIgnoreExitValue = true
    }.standardOutput.asText.map { it.trim() }

val currentCommitHash: Provider<String> =
  providers.exec {
    workingDir(rootDir)
    commandLine(
      "git",
      "rev-parse",
      "--short",
      "HEAD",
    )
    isIgnoreExitValue = true
  }.standardOutput.asText.map { it.trim() }

val gitVersion: Provider<String> =
  gitDescribe.zip(currentBranchName) { described, branch ->
    val snapshot = if ("SNAPSHOT" in described) "-SNAPSHOT" else ""

    val descriptions = described.split("-")

    if (branch.isNullOrBlank() && descriptions.size in 1..2) {
      // detached if there's no current branch, or if 'git described' indicates additional commits
      val tag = descriptions.getOrNull(0)
      "$tag$snapshot"
    } else {
      // current commit is attached
      "$branch$snapshot"
    }
  }.orElse(currentCommitHash)

gradle.allprojects {
  extensions.add<Provider<String>>("gitVersion", gitVersion)
}
//endregion
