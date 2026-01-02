import buildsrc.config.excludeGeneratedGradleDsl

plugins {
  idea
  id("org.jetbrains.kotlinx.kover")
  buildsrc.convention.base
  id("com.gradleup.nmcp.aggregation")
}


project.group = "dev.adamko.kxstsgen"
project.version = object {
  private val gitVersion = project.gitVersion
  override fun toString(): String = gitVersion.get()
}

idea {
  module {
    excludeGeneratedGradleDsl(layout)
    excludeDirs = excludeDirs + layout.files(
      ".idea",
      ".kotlin",
      "buildSrc/.kotlin",
      "gradle/kotlin-js-store",
      "gradle/wrapper",
      "site/.docusaurus",
    )
  }
}

val projectVersion by tasks.registering {
  description = "prints the project version"
  group = "help"
  val version = providers.provider { project.version }
  inputs.property("version", version)
  outputs.cacheIf("logging task, it should always run") { false }
  doLast {
    logger.quiet("${version.orNull}")
  }
}

nmcpAggregation {
  centralPortal {
    username = providers.gradleProperty("dev.adamko.kxstsgen.mavenCentralUsername")
      .orElse(providers.environmentVariable("MAVEN_SONATYPE_USERNAME"))
    password = providers.gradleProperty("dev.adamko.kxstsgen.mavenCentralPassword")
      .orElse(providers.environmentVariable("MAVEN_SONATYPE_PASSWORD"))

    // publish manually from the portal
    publishingType = "USER_MANAGED"
  }
}

dependencies {
  nmcpAggregation(project)
}

val isReleaseVersion: Provider<Boolean> =
  providers.provider { project.version.toString().endsWith("-SNAPSHOT") }

tasks.nmcpPublishAggregationToCentralPortal {
  val isReleaseVersion = isReleaseVersion
  onlyIf("is release version") { _ -> isReleaseVersion.get() }
}

tasks.nmcpPublishAggregationToCentralPortalSnapshots {
  val isReleaseVersion = isReleaseVersion
  onlyIf("is snapshot version") { _ -> !isReleaseVersion.get() }
}

tasks.register("nmcpPublish") {
  group = PublishingPlugin.PUBLISH_TASK_GROUP
  dependsOn(tasks.nmcpPublishAggregationToCentralPortal)
  dependsOn(tasks.nmcpPublishAggregationToCentralPortalSnapshots)
}
