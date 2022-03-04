package dev.adamko.kxstsgen

import kotlin.jvm.JvmInline
import kotlinx.serialization.descriptors.SerialDescriptor

data class KxsTsConfig(
  val indent: String = "  ",
  val namespaceConfig: NamespaceConfig = NamespaceConfig.Disabled,
  val typeAliasTyping: TypeAliasTypingConfig = TypeAliasTypingConfig.None,
) {

  sealed interface NamespaceConfig {
    /** Use the prefix of the [SerialDescriptor]  */
    object UseDescriptorNamePrefix : NamespaceConfig
    /** don't generate a namespace */
    object Disabled : NamespaceConfig
    @JvmInline
    value class Static(val namespace: String) : NamespaceConfig
  }

  sealed interface TypeAliasTypingConfig {
    object None : TypeAliasTypingConfig
    object BrandTyping : TypeAliasTypingConfig
  }

}
