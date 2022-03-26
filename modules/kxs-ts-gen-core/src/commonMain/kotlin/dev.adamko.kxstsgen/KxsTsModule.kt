package dev.adamko.kxstsgen

import kotlin.reflect.KClass
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.SerializersModuleCollector


/** Collects the contents of a [SerializersModule], so kxs-ts-gen can view registered classes. */
class KxsTsModule(
  val serializersModule: SerializersModule = EmptySerializersModule
) {

  private val contextualClasses: MutableSet<KClass<*>> = mutableSetOf()
  private val polymorphicClasses: MutableList<Pair<KClass<*>, KClass<*>>> = mutableListOf()

  init {
    serializersModule.dumpTo(Collector())
  }


  data class Polymorphic<Base : Any, Sub : Base>(
    val baseClass: KClass<Base>,
    val actualClass: KClass<Sub>,
    val actualSerializer: KSerializer<Sub>,
  ) {
    val actualDescriptor: SerialDescriptor by actualSerializer::descriptor
  }

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
