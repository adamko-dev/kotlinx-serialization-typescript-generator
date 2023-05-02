package buildsrc.config

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugins.signing.SigningExtension


fun MavenPublication.createKxsTsGenPom(): Unit = pom {
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
