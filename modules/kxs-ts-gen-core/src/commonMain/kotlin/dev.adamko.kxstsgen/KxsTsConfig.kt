package dev.adamko.kxstsgen

import dev.adamko.kxstsgen.util.MutableMapWithDefaultPut
import kotlin.jvm.JvmInline
import kotlin.reflect.KClass
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.SerializersModuleCollector

data class KxsTsConfig(
  val indent: String = "  ",
  val structureSeparator: String = "\n\n",
  val namespaceConfig: NamespaceConfig = NamespaceConfig.Disabled,
  val typeAliasTyping: TypeAliasTypingConfig = TypeAliasTypingConfig.None,
  val serializersModule: SerializersModule = EmptySerializersModule,
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

  private val contextualClasses: MutableSet<KClass<*>> = mutableSetOf()

  private val _polymorphicDescriptors
    by MutableMapWithDefaultPut<KClass<*>, MutableSet<SerialDescriptor>> { mutableSetOf() }

  val polymorphicDescriptors: Map<KClass<*>, Set<SerialDescriptor>>
    get() = _polymorphicDescriptors.mapValues { it.value.toSet() }.toMap().withDefault { setOf() }

  init {
    serializersModule.dumpTo(Collector())
  }


  /** Collects the contents of a [SerializersModule], so kxs-ts-gen can view registered classes. */
  private inner class Collector : SerializersModuleCollector {

    override fun <T : Any> contextual(
      kClass: KClass<T>,
      provider: (typeArgumentsSerializers: List<KSerializer<*>>) -> KSerializer<*>
    ) {
      contextualClasses + kClass
    }

    override fun <Base : Any, Sub : Base> polymorphic(
      baseClass: KClass<Base>,
      actualClass: KClass<Sub>,
      actualSerializer: KSerializer<Sub>,
    ) {
      _polymorphicDescriptors.getValue(baseClass).add(actualSerializer.descriptor)
    }

    @ExperimentalSerializationApi
    override fun <Base : Any> polymorphicDefaultDeserializer(
      baseClass: KClass<Base>,
      defaultDeserializerProvider: (className: String?) -> DeserializationStrategy<out Base>?
    ) {
    }

    @ExperimentalSerializationApi
    override fun <Base : Any> polymorphicDefaultSerializer(
      baseClass: KClass<Base>,
      defaultSerializerProvider: (value: Base) -> SerializationStrategy<Base>?
    ) {
    }

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
