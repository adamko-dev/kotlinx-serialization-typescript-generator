package buildsrc.convention

import buildsrc.config.publishing
import buildsrc.config.signing
import org.gradle.api.credentials.PasswordCredentials
import org.gradle.internal.credentials.DefaultPasswordCredentials

plugins {
  id("buildsrc.convention.subproject")
  `maven-publish`
  signing
}


//val sonatypeRepositoryCredentials: Provider<PasswordCredentials> =
//  providers.credentials(PasswordCredentials::class, "sonatypeRepositoryCredentials")

val sonatypeRepositoryUsername: String? by project.extra
val sonatypeRepositoryPassword: String? by project.extra
val sonatypeRepositoryCredentials: Provider<PasswordCredentials> = providers.provider {
  if (sonatypeRepositoryUsername.isNullOrBlank() || sonatypeRepositoryPassword.isNullOrBlank()) {
    null
  } else {
    DefaultPasswordCredentials(sonatypeRepositoryUsername, sonatypeRepositoryPassword)
  }
}


val sonatypeRepositoryId: String by project.extra

val sonatypeRepositoryReleaseUrl: Provider<String> = provider {
  if (version.toString().endsWith("SNAPSHOT")) {
    "https://oss.sonatype.org/content/repositories/snapshots/"
  } else {
    "https://oss.sonatype.org/service/local/staging/deployByRepositoryId/$sonatypeRepositoryId/"
  }
}


tasks.matching {
  it.name.startsWith(PublishingPlugin.PUBLISH_LIFECYCLE_TASK_NAME)
    && it.group == PublishingPlugin.PUBLISH_TASK_GROUP
}.configureEach {
  doLast {
    logger.lifecycle("[${this.name}] ${project.group}:${project.name}:${project.version}")
  }
}



publishing {
  repositories {
    if (sonatypeRepositoryCredentials.isPresent) {
      maven(sonatypeRepositoryReleaseUrl) {
        name = "oss"
        credentials {
          username = sonatypeRepositoryCredentials.get().username
          password = sonatypeRepositoryCredentials.get().password
        }
      }
    }
  }
}

signing {
  val signingKeyId: String? by project
  val signingKey: String? by project
  val signingPassword: String? by project
  useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
  setRequired(false)
}


plugins.withType<JavaPlugin> {
  if (!plugins.hasPlugin(KotlinMultiplatformPlugin::class)) {
    val publication = publishing.publications.create<MavenPublication>("mavenJava") {
      from(components["java"])
    }
    signing { sign(publication) }
  }
}

plugins.withType<JavaPlatformPlugin> {
  val publication = publishing.publications.create<MavenPublication>("mavenJavaPlatform") {
    from(components["javaPlatform"])
  }
  signing { sign(publication) }
}
