package buildsrc.convention

import buildsrc.config.createKxsTsGenPom
import buildsrc.config.credentialsAction
import buildsrc.config.isKotlinMultiplatformJavaEnabled
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


signing {
  if (sonatypeRepositoryCredentials.isPresent()) {
    if (signingKeyId.isPresent() && signingKey.isPresent() && signingPassword.isPresent()) {
      useInMemoryPgpKeys(signingKeyId.get(), signingKey.get(), signingPassword.get())
    } else {
      useGpgCmd()
    }
  }
}


afterEvaluate {
  // Register signatures afterEvaluate, otherwise the signing plugin creates the signing tasks
  // too early, before all the publications are added.
  // Use .all { }, not .configureEach { }, otherwise the signing plugin doesn't create the tasks
  // soon enough.

  if (sonatypeRepositoryCredentials.isPresent()) {
    publishing.publications.withType<MavenPublication>().all {
      signing.sign(this)
      logger.lifecycle("configuring signature for publication ${this.name}")
    }
  }
}

//region Javadoc JAR stub
// use creating, not registering, because the signing plugin doesn't accept task providers
val javadocJarStub by tasks.creating(Jar::class) {
  group = JavaBasePlugin.DOCUMENTATION_GROUP
  description = "Stub javadoc.jar artifact (required by Maven Central)"
  archiveClassifier.set("javadoc")
}

tasks.withType<AbstractPublishToMaven>().all {
  dependsOn(javadocJarStub)
}

if (sonatypeRepositoryCredentials.isPresent()) {
  val signingTasks = signing.sign(javadocJarStub)
  tasks.withType<AbstractPublishToMaven>().all {
    signingTasks.forEach { dependsOn(it) }
  }
}
//endregion

publishing {
  if (sonatypeRepositoryCredentials.isPresent()) {
    repositories {
      maven(sonatypeRepositoryReleaseUrl) {
        name = "sonatype"
        credentials(sonatypeRepositoryCredentials.get())
      }
//      // publish to local dir, for testing
//      maven(rootProject.layout.buildDirectory.dir("maven-internal")) {
//        name = "maven-internal"
//      }
    }
    publications.withType<MavenPublication>().configureEach {
      createKxsTsGenPom()
      artifact(javadocJarStub)
    }
  }
}


plugins.withType<KotlinMultiplatformPlugin>().configureEach {
  publishing.publications.withType<MavenPublication>().configureEach {
//    artifact(javadocJarStub)
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
//    artifact(javadocJarStub)
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
  doLast("log publication GAV") {
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
