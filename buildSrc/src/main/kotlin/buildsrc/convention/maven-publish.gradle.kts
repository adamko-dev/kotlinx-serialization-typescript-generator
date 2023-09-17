package buildsrc.convention

import buildsrc.config.credentialsAction
import buildsrc.config.isKotlinMultiplatformJavaEnabled
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinMultiplatformPlugin


plugins {
  `maven-publish`
  signing
}

val sonatypeRepositoryCredentials: Provider<Action<PasswordCredentials>> =
  providers.credentialsAction("sonatypeRepository")

val projectVersion: Provider<String> = provider { project.version.toString() }

val sonatypeRepositoryReleaseUrl: Provider<String> = projectVersion.map { version ->
  val isRelease = version.endsWith("SNAPSHOT")
  if (isRelease) {
    "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
  } else {
    "https://s01.oss.sonatype.org/content/repositories/snapshots/"
  }
}


//region Signing
val signingKeyId: Provider<String> =
  providers.gradleProperty("signing.keyId")
val signingKey: Provider<String> =
  providers.gradleProperty("signing.key")
val signingPassword: Provider<String> =
  providers.gradleProperty("signing.password")

signing {
  logger.info("maven-publishing.gradle.kts enabled signing for ${project.path}")

  val keyId = signingKeyId.orNull
  val key = signingKey.orNull
  val password = signingPassword.orNull

  val signingKeysPresent =
    !keyId.isNullOrBlank() && !key.isNullOrBlank() && !password.isNullOrBlank()

  if (signingKeysPresent) {
    useInMemoryPgpKeys(keyId, key, password)
  }

  // only require signing when publishing to Sonatype
  setRequired({
    signingKeysPresent
      ||
      gradle.taskGraph.allTasks
        .filterIsInstance<PublishToMavenRepository>()
        .any { it.repository.name == "SonatypeRelease" }
  })
}

afterEvaluate {
  // Register signatures afterEvaluate, otherwise the signing plugin creates the signing tasks
  // too early, before all the publications are added.
  signing.sign(publishing.publications)
}
//endregion


//region Javadoc JAR stub
// use creating, not registering, because the signing plugin doesn't accept task providers
val javadocJarStub by tasks.creating(Jar::class) {
  group = JavaBasePlugin.DOCUMENTATION_GROUP
  description = "Stub javadoc.jar artifact (required by Maven Central)"
  archiveClassifier.set("javadoc")
}
//endregion


publishing {
  repositories {
    // publish to local dir, for testing
    maven(rootProject.layout.buildDirectory.dir("maven-internal")) {
      name = "MavenInternal"
    }

    if (sonatypeRepositoryCredentials.isPresent()) {
      maven(sonatypeRepositoryReleaseUrl) {
        name = "SonatypeRelease"
        credentials(sonatypeRepositoryCredentials.get())
      }
    }
  }
  publications.withType<MavenPublication>().configureEach {
    pom {
      name.set("Kotlinx Serialization Typescript Generator")
      description.set("KxsTsGen creates TypeScript interfaces from Kotlinx Serialization @Serializable classes")
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
    artifact(javadocJarStub)
  }
}


plugins.withType<KotlinMultiplatformPlugin>().configureEach {
  publishing.publications.withType<MavenPublication>().configureEach {
    //artifact(javadocJarStub)
  }
}


plugins.withType<JavaPlugin>().configureEach {
  afterEvaluate {
    if (!isKotlinMultiplatformJavaEnabled()) {
      publishing.publications.create<MavenPublication>("mavenJava") {
        from(components["java"])
        artifact(tasks["sourcesJar"])
      }
    }
  }
}


plugins.withType<JavaPlatformPlugin>().configureEach {
//  val javadocJarStub = javadocStubTask()
  publishing.publications.create<MavenPublication>("mavenJavaPlatform") {
    from(components["javaPlatform"])
    //artifact(javadocJarStub)
  }
}


//region Fix Gradle warning about signing tasks using publishing task outputs without explicit dependencies
// https://youtrack.jetbrains.com/issue/KT-46466 https://github.com/gradle/gradle/issues/26091
tasks.withType<AbstractPublishToMaven>().configureEach {
  val signingTasks = tasks.withType<Sign>()
  mustRunAfter(signingTasks)
}
//endregion


//region publishing logging
tasks.withType<AbstractPublishToMaven>().configureEach {
  val publicationGAV = provider { publication?.run { "$group:$artifactId:$version" } }
  inputs.property("publicationGAV", publicationGAV).optional(true)
  doFirst("log publication GAV") {
    if (publicationGAV.isPresent) {
      logger.lifecycle("[task: ${path}] ${publicationGAV.get()}")
    }
  }
}
//endregion


//region IJ workarounds
// manually define the Kotlin DSL accessors because IntelliJ _still_ doesn't load them properly
fun Project.publishing(configure: PublishingExtension.() -> Unit): Unit =
  extensions.configure(configure)

val Project.publishing: PublishingExtension
  get() = extensions.getByType()

fun Project.signing(configure: SigningExtension.() -> Unit): Unit =
  extensions.configure(configure)

val Project.signing: SigningExtension
  get() = extensions.getByType()
//endregion
