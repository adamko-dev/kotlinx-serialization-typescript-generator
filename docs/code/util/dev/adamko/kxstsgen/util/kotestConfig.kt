package dev.adamko.kxstsgen.util

import io.kotest.core.Tag
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.spec.IsolationMode


@Suppress("unused") // picked up by Kotest scanning
object KotestConfig : AbstractProjectConfig() {
  //  override val parallelism = 10 // causes tests failures...
  override val isolationMode = IsolationMode.InstancePerLeaf
}

object Knit : Tag()

object TSCompile : Tag()

val tsCompile = setOf(TSCompile)
