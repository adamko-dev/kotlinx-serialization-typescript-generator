package buildsrc.convention

import buildsrc.config.publishing
import buildsrc.config.signing
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinMultiplatformPlugin
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget


plugins {
  `maven-publish`
  signing
}

val sonatypeRepositoryCredentials: Provider<Action<PasswordCredentials>> =
  providers.zip(
    providers.gradleProperty("sonatypeRepositoryUsername"),
    providers.gradleProperty("sonatypeRepositoryPassword"),
  ) { user, pass ->
    Action<PasswordCredentials> {
      username = user
      password = pass
    }
  }

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


tasks.matching {
  it.name.startsWith(PublishingPlugin.PUBLISH_LIFECYCLE_TASK_NAME)
    && it.group == PublishingPlugin.PUBLISH_TASK_GROUP
}.configureEach {
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
    }
    publications.withType<MavenPublication>().configureEach {
      createKxTsGenPom()
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


fun MavenPublication.createKxTsGenPom(): Unit = pom {
  name.set("Kotlinx Serialization Typescript Generator")
  description.set("KxTsGen creates TypeScript interfaces from Kotlinx Serialization @Serializable classes")
  url.set("https://github.com/adamko-dev/kotlinx-serialization-typescript-generator")

  licenses {
    license {
      name.set("The Apache License, Version 2.0")
      url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
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


/** Logic from [KotlinJvmTarget.withJava] */
fun Project.isKotlinMultiplatformJavaEnabled(): Boolean {
  val multiplatformExtension: KotlinMultiplatformExtension? =
    extensions.findByType(KotlinMultiplatformExtension::class)

  return multiplatformExtension?.targets
    ?.any { it is KotlinJvmTarget && it.withJavaEnabled }
    ?: false
}
