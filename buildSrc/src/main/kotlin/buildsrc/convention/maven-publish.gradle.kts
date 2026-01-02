package buildsrc.convention

import buildsrc.config.credentialsAction

plugins {
  `maven-publish`
  signing
  id("com.gradleup.nmcp")
}

val sonatypeRepositoryCredentials: Provider<Action<PasswordCredentials>> =
  providers.credentialsAction("sonatypeRepository")

val projectVersion: Provider<String> = provider { project.version.toString() }


//region Signing
val signingKeyId: Provider<String> =
  providers.gradleProperty("signing.keyId")
val signingKey: Provider<String> =
  providers.gradleProperty("signing.key")
val signingPassword: Provider<String> =
  providers.gradleProperty("signing.password")

signing {
  val keyId = signingKeyId.orNull
  val key = signingKey.orNull
  val password = signingPassword.orNull

  val signingCredentialsPresent =
    !keyId.isNullOrBlank() && !key.isNullOrBlank() && !password.isNullOrBlank()

  if (signingCredentialsPresent) {
    logger.info("maven-publishing.gradle.kts enabled signing for ${project.displayName}")
    useInMemoryPgpKeys(keyId, key, password)
  }

  setRequired({
    signingCredentialsPresent || gradle.taskGraph.allTasks
      .filterIsInstance<PublishToMavenRepository>()
      .any { task ->
        task.repository.name in setOf(
          "SonatypeRelease",
        )
      }
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
val javadocJarStub by tasks.registering(Jar::class) {
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
  doLast("log publication GAV") {
    if (publicationGAV.isPresent) {
      logger.lifecycle("[task: ${path}] ${publicationGAV.get()}")
    }
  }
}
//endregion


//region Maven Central can't handle parallel uploads, so limit parallel uploads with a service.
abstract class MavenPublishLimiter : BuildService<BuildServiceParameters.None>

val mavenPublishLimiter =
  gradle.sharedServices.registerIfAbsent("mavenPublishLimiter", MavenPublishLimiter::class) {
    maxParallelUsages = 1
  }

tasks.withType<PublishToMavenRepository>().configureEach {
  usesService(mavenPublishLimiter)
}
//endregion
