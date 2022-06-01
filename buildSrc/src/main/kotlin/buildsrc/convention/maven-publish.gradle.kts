package buildsrc.convention

import buildsrc.config.publishing
import buildsrc.config.signing
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinMultiplatformPlugin

plugins {
  `maven-publish`
  signing
}


val sonatypeRepositoryUsername: Provider<String> =
  providers.gradleProperty("sonatypeRepositoryUsername")
val sonatypeRepositoryPassword: Provider<String> =
  providers.gradleProperty("sonatypeRepositoryPassword")

val sonatypeRepositoryReleaseUrl: Provider<String> = provider {
  if (version.toString().endsWith("SNAPSHOT")) {
    "https://s01.oss.sonatype.org/content/repositories/snapshots/"
  } else {
    "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
  }
}


val signingKeyId: Provider<String> =
  providers.gradleProperty("signing.keyId")
val signingPassword: Provider<String> =
  providers.gradleProperty("signing.password")
val signingSecretKeyRingFile: Provider<String> =
  providers.gradleProperty("signing.secretKeyRingFile")


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
    maven(sonatypeRepositoryReleaseUrl) {
      name = "sonatype"
      credentials {
        username = sonatypeRepositoryUsername.get()
        password = sonatypeRepositoryPassword.get()
      }
    }
  }
  publications.withType<MavenPublication>().configureEach {
    createKxTsGenPom()
  }
}


signing {

//  if (
//    signingKeyId.isPresent() &&
//    signingPassword.isPresent() &&
//    signingSecretKeyRingFile.isPresent()
//  ) {
//  useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
//  } else {
//    useGpgCmd()
//  }

  useGpgCmd()

  // sign all publications
  sign(publishing.publications)
}


plugins.configureEach {
  when (this) {
    is KotlinMultiplatformPlugin -> {

      // Stub javadoc.jar artifact (required by Maven Central?)
      val javadocJar by tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
      }

      publishing.publications.create<MavenPublication>("mavenKotlinMpp") {
        from(components["kotlin"])
        artifact(javadocJar)
        artifact(tasks["sourcesJar"])
      }
    }

    // clashes with KtMPP plugin?
    // causes error
    // Artifact kxs-ts-gen-core-jvm-maven-publish-SNAPSHOT.jar wasn't produced by this build
//    is JavaPlugin                -> afterEvaluate {
//      if (!plugins.hasPlugin(KotlinMultiplatformPlugin::class)) {
//        publishing.publications.create<MavenPublication>("mavenJava") {
//          from(components["java"])
//          artifact(tasks["sourcesJar"])
//        }
//      }
//    }

    is JavaPlatformPlugin        -> {
      publishing.publications.create<MavenPublication>("mavenJavaPlatform") {
        from(components["javaPlatform"])
      }
    }
  }
}


fun MavenPublication.createKxTsGenPom(): Unit = pom {
  name.set("Kotlinx Serialization Typescript Generator")
  description.set("KxTsGen creates TypeScript interfaces from Kotlinx Serialization @Serializable classes")
  url.set("https://github.com/adamko-dev/kotlinx-serialization-typescript-generator")

  licenses {
    license {
      name.set("The Apache License, Version 2.0")
      url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
    }
  }

  developers {
    developer {
      email.set("adam@adamko.dev")
    }
  }

  scm {
    connection.set("scm:git:git://github.com/adamko-dev/kotlinx-serialization-typescript-generator.git")
    developerConnection.set("scm:git:ssh://github.com:adamko-dev/kotlinx-serialization-typescript-generator.git")
    url.set("https://github.com/adamko-dev/kotlinx-serialization-typescript-generator")
  }
}
