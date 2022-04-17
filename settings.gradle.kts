rootProject.name = "kotlinx-serialization-typescript-generator"

apply(from = "./buildSrc/repositories.settings.gradle.kts")

include(
  ":modules:kxs-ts-gen-core",
  ":modules:kxs-ts-gen-gradle-plugin",
  ":modules:kxs-ts-gen-processor",
  ":modules:versions-platform",
  ":docs:code",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
  @Suppress("UnstableApiUsage") // Central declaration of repositories is an incubating feature
  repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
}
