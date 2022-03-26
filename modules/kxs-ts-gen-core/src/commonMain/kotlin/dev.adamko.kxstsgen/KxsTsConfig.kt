package dev.adamko.kxstsgen

import kotlin.jvm.JvmInline
import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor

data class KxsTsConfig(
  val indent: String = "  ",
  val structureSeparator: String = "\n\n",
  val namespaceConfig: NamespaceConfig = NamespaceConfig.Disabled,
  val typeAliasTyping: TypeAliasTypingConfig = TypeAliasTypingConfig.None,
) {

  sealed interface NamespaceConfig {
    /** Use the prefix of the [SerialDescriptor]  */
    object DescriptorNamePrefix : NamespaceConfig
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

//
//
//class SerializerClassMap {
//  private val _map: MutableMap<KSerializer<*>, KClass<*>> = mutableMapOf()
//
//  val size: Int by _map::size
//
//  fun containsKey(key: KSerializer<*>): Boolean = _map.containsKey(key)
//  fun containsValue(value: KClass<*>): Boolean = _map.containsValue(value)
//
//
//  fun isEmpty(): Boolean = _map.isEmpty()
//
//  val keys: MutableSet<KSerializer<*>> by _map::keys
//  val values: MutableCollection<KClass<*>> by _map::values
//
//  fun clear(): Unit = _map.clear()
//
//  @Suppress("UNCHECKED_CAST")
//  fun <T : Any> get(key: KSerializer<in T>): KClass<out T>? =
//    _map[key] as KClass<out T>?
//
//  @Suppress("UNCHECKED_CAST")
//  fun <T : Any> put(key: KSerializer<T>, value: KClass<T>): KClass<T>? =
//    _map.put(key, value) as KClass<T>?
//
//  @Suppress("UNCHECKED_CAST")
//  fun <T : Any> remove(key: KSerializer<T>): KClass<T>? = _map.remove(key) as KClass<T>?
//
//  fun entries(): Set<Entry<*>> = _map.map { Entry<Any>(it) }.toSet()
//
//  data class Entry<T : Any>(
//    val serializer: KSerializer<T>,
//    val kClass: KClass<T>,
//  ) {
//    @Suppress("UNCHECKED_CAST")
//    constructor(entry: Map.Entry<*, *>)
//      : this(entry.key as KSerializer<T>, entry.value as KClass<T>)
//  }
//
//}
