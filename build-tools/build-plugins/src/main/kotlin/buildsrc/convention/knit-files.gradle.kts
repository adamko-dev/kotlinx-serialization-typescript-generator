package buildsrc.convention

import buildsrc.config.asConsumer
import buildsrc.config.asProvider
import buildsrc.config.knitDocsAttributes
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering

plugins {
  id("buildsrc.convention.base")
}

val knitDocsElements by configurations.registering {
  asProvider()
  attributes { knitDocsAttributes(objects) }
}

val knitDocs by configurations.registering {
  asConsumer()
  attributes { knitDocsAttributes(objects) }
}
