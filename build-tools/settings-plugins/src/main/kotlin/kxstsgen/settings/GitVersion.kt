package kxstsgen.settings

import org.gradle.api.provider.Provider

//@JvmInline
//value
class GitVersion(
  private val gitVersion: Provider<String>
) {
  override fun toString(): String = gitVersion.get()
}
