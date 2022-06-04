package buildsrc.convention

import buildsrc.config.createKxsTsGenPom
import buildsrc.config.credentialsAction
import buildsrc.config.isKotlinMultiplatformJavaEnabled
import buildsrc.config.publishing
import buildsrc.config.signing
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinMultiplatformPlugin


plugins {
  `maven-publish`
  signing
}

val sonatypeRepositoryCredentials: Provider<Action<PasswordCredentials>> =
  providers.credentialsAction("sonatypeRepository")


val sonatypeRepositoryReleaseUrl: Provider<String> = provider {
  if (version.toString().endsWith("SNAPSHOT")) {
    "https://s01.oss.sonatype.org/content/repositories/snapshots/"
  } else {
    "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
  }
}


val signingKeyId: Provider<String> =
  providers.gradleProperty("signing.keyId")
val signingKey: Provider<String> =
  providers.gradleProperty("signing.key")
val signingPassword: Provider<String> =
  providers.gradleProperty("signing.password")
val signingSecretKeyRingFile: Provider<String> =
  providers.gradleProperty("signing.secretKeyRingFile")


val javadocJarStub by tasks.registering(Jar::class) {
  group = JavaBasePlugin.DOCUMENTATION_GROUP
  description = "Stub javadoc.jar artifact (required by Maven Central)"
  archiveClassifier.set("javadoc")
}


tasks.withType<AbstractPublishToMaven>().configureEach {
  // Gradle warns about some signing tasks using publishing task outputs without explicit
  // dependencies. I'm not going to go through them all and fix them, so here's a quick fix.
  dependsOn(tasks.withType<Sign>())

  if (sonatypeRepositoryCredentials.isPresent()) {
    dependsOn(javadocJarStub)
  }

  doLast {
    logger.lifecycle("[${this.name}] ${project.group}:${project.name}:${project.version}")
  }
}


publishing {
  if (sonatypeRepositoryCredentials.isPresent()) {
    repositories {
      maven(sonatypeRepositoryReleaseUrl) {
        name = "sonatype"
        credentials(sonatypeRepositoryCredentials.get())
      }
      // publish to local dir, for testing
      // maven {
      //   name = "maven-internal"
      //   url = uri(rootProject.layout.buildDirectory.dir("maven-internal"))
      // }
    }
    publications.withType<MavenPublication>().configureEach {
      createKxsTsGenPom()
      artifact(javadocJarStub)
    }
  }
}


signing {
  if (sonatypeRepositoryCredentials.isPresent()) {
    if (signingKeyId.isPresent() && signingKey.isPresent() && signingPassword.isPresent()) {
      useInMemoryPgpKeys(signingKeyId.get(), signingKey.get(), signingPassword.get())
    } else {
      useGpgCmd()
    }

    // sign all publications
    sign(publishing.publications)
    sign(javadocJarStub.get())
  }
}


plugins.withType(KotlinMultiplatformPlugin::class).configureEach {
  publishing.publications.withType<MavenPublication>().configureEach {
    artifact(javadocJarStub)
  }
}


plugins.withType(JavaPlugin::class).configureEach {
  afterEvaluate {
    if (!isKotlinMultiplatformJavaEnabled()) {
      publishing.publications.create<MavenPublication>("mavenJava") {
        from(components["java"])
        artifact(tasks["sourcesJar"])
      }
    }
  }
}


plugins.withType(JavaPlatformPlugin::class).configureEach {
  publishing.publications.create<MavenPublication>("mavenJavaPlatform") {
    from(components["javaPlatform"])
  }
}
