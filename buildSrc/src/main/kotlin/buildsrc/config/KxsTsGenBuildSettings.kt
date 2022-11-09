package buildsrc.config

import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory

abstract class KxsTsGenBuildSettings(
  private val providers: ProviderFactory
) {
  val enableTsCompileTests: Provider<Boolean> =
    providers
      .gradleProperty("kxstsgen_enableTsCompileTests")
      .map { it.toBoolean() }
      .orElse(false)

  companion object {
    const val NAME = "kxsTsGenSettings"
  }
}
