package dev.adamko.kxstsgen

import dev.adamko.kxstsgen.core.UnimplementedKxTsGenApi
import dev.adamko.kxstsgen.core.util.MutableMapWithDefaultPut
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


/**
 * @param[indent] Define the indentation that is used when generating source code
 * @param[declarationSeparator] The string that is used when joining [TsDeclaration]s
 * @param[namespaceConfig] (UNIMPLEMENTED) How elements are grouped into [TsDeclaration.TsNamespace]s.
 * @param[typeAliasTyping] (UNIMPLEMENTED) Control if type aliases are simple, or 'branded'.
 * @param[serializersModule] Used to obtain contextual and polymorphic information.
 */
data class KxsTsConfig(
  val indent: String = "  ",
  val declarationSeparator: String = "\n\n",
  @UnimplementedKxTsGenApi
  val namespaceConfig: NamespaceConfig = NamespaceConfig.Disabled,
  @UnimplementedKxTsGenApi
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
