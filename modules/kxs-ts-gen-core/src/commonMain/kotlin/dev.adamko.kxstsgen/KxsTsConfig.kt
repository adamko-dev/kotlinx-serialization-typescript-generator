package dev.adamko.kxstsgen

import kotlin.jvm.JvmInline
import kotlinx.serialization.descriptors.SerialDescriptor

data class KxsTsConfig(
  val indent: String = "  ",
  val namespaceConfig: NamespaceConfig = NamespaceConfig.None,
) {

  sealed interface NamespaceConfig {
    /** Use the prefix of the [SerialDescriptor]  */
    object UseDescriptorNamePrefix : NamespaceConfig
    /** don't generate a namespace */
    object None : NamespaceConfig
    @JvmInline
    value class Hardcoded(val namespace: String) : NamespaceConfig
  }

}
