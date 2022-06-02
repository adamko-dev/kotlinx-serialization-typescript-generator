package buildsrc.config

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugins.signing.SigningExtension


// hacks because IntelliJ still doesn't properly load DSL accessors for buildSrc

/** Configure [PublishingExtension] */
fun Project.publishing(action: PublishingExtension.() -> Unit): Unit =
  extensions.configure(action)

val Project.publishing: PublishingExtension
  get() = extensions.getByType<PublishingExtension>()

/** Configure [SigningExtension] */
fun Project.signing(action: SigningExtension.() -> Unit): Unit =
  extensions.configure(action)

val Project.signing: SigningExtension
  get() = extensions.getByType<SigningExtension>()
