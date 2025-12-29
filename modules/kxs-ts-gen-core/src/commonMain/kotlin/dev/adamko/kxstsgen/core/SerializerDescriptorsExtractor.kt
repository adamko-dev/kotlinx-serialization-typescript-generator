package dev.adamko.kxstsgen.core

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.SerializersModuleCollector
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlin.reflect.KClass



/**
 * Recursively extract all descriptors from a serializer and its elements.
 */
fun interface SerializerDescriptorsExtractor {

  operator fun invoke(
    serializer: KSerializer<*>
  ): Set<SerialDescriptor>

  companion object {
    /** The default [SerializerDescriptorsExtractor], for easy use. */
    fun default(
      serializersModule: SerializersModule,
    ): SerializerDescriptorsExtractor {
      return Default(
        elementDescriptorsExtractor = TsElementDescriptorsExtractor.default(serializersModule)
      )
    }
  }

  class Default(
    private val elementDescriptorsExtractor: TsElementDescriptorsExtractor,
  ) : SerializerDescriptorsExtractor {

    override operator fun invoke(
      serializer: KSerializer<*>
    ): Set<SerialDescriptor> {
      val moduleDescriptors = elementDescriptorsExtractor.collectModuleDescriptors()
      return extractDescriptors(
        current = serializer.descriptor,
        queue = ArrayDeque(),
        extracted = emptySet(),
        moduleDescriptors = moduleDescriptors
      )
        .distinctBy { it.nullable }
        .toSet()
    }

    private tailrec fun extractDescriptors(
      current: SerialDescriptor? = null,
      queue: ArrayDeque<SerialDescriptor> = ArrayDeque(),
      extracted: Set<SerialDescriptor> = emptySet(),
      moduleDescriptors: Set<SerialDescriptor> = emptySet(),
    ): Set<SerialDescriptor> {
      return if (current == null) {
        extracted
      } else {
        val currentDescriptors = elementDescriptorsExtractor.elementDescriptors(current, moduleDescriptors)
        queue.addAll(currentDescriptors - extracted)
        extractDescriptors(queue.removeFirstOrNull(), queue, extracted + current, moduleDescriptors)
      }
    }
  }
}


interface TsElementDescriptorsExtractor {
  fun elementDescriptors(descriptor: SerialDescriptor, moduleDescriptors: Set<SerialDescriptor>): Iterable<SerialDescriptor>
  fun collectModuleDescriptors(): Set<SerialDescriptor> = emptySet()

  companion object {

    fun default(serializersModule: SerializersModule) =
      object : TsElementDescriptorsExtractor {

        private val cachedModuleDescriptors by lazy {
          collectDescriptorsFromModule(serializersModule)
        }

        override fun collectModuleDescriptors(): Set<SerialDescriptor> = cachedModuleDescriptors

        override fun elementDescriptors(descriptor: SerialDescriptor, moduleDescriptors: Set<SerialDescriptor>): Iterable<SerialDescriptor> {
          return when (descriptor.kind) {
            SerialKind.ENUM       -> emptyList()

            SerialKind.CONTEXTUAL -> {
              resolveContextualDescriptor(descriptor, moduleDescriptors)
            }

            PrimitiveKind.BOOLEAN,
            PrimitiveKind.BYTE,
            PrimitiveKind.CHAR,
            PrimitiveKind.SHORT,
            PrimitiveKind.INT,
            PrimitiveKind.LONG,
            PrimitiveKind.FLOAT,
            PrimitiveKind.DOUBLE,
            PrimitiveKind.STRING -> emptyList()

            StructureKind.CLASS,
            StructureKind.LIST,
            StructureKind.MAP,
            StructureKind.OBJECT -> descriptor.elementDescriptors

            PolymorphicKind.SEALED,
            PolymorphicKind.OPEN  -> {
              resolvePolymorphicDescriptors(descriptor, moduleDescriptors)
            }
          }
        }

        private fun resolveContextualDescriptor(descriptor: SerialDescriptor, moduleDescriptors: Set<SerialDescriptor>): Iterable<SerialDescriptor> {
          val typeParameters = getTypeParametersFromDescriptor(descriptor)
          return typeParameters.flatMap { typeParam ->
            moduleDescriptors.filter { moduleDesc ->
              moduleDesc.serialName.endsWith(typeParam) || 
              moduleDesc.serialName.endsWith(".$typeParam")
            }
          }
        }

        private fun resolvePolymorphicDescriptors(descriptor: SerialDescriptor, moduleDescriptors: Set<SerialDescriptor>): Iterable<SerialDescriptor> {
          val baseType = getPolymorphicBaseType(descriptor) ?: descriptor.serialName
          
          val subclassDescriptors = moduleDescriptors.filter { moduleDesc ->
            moduleDesc.kind.let { it is StructureKind.CLASS || it is StructureKind.OBJECT } &&
            isSubclassOf(moduleDesc.serialName, baseType)
          }

          return subclassDescriptors + subclassDescriptors.flatMap { it.elementDescriptors } + 
                 descriptor.elementDescriptors
                   .flatMap { it.elementDescriptors }
                   .flatMap { it.elementDescriptors }
        }

        private fun getTypeParametersFromDescriptor(descriptor: SerialDescriptor): List<String> {
          val serialName = descriptor.serialName
          if (serialName.contains("<") && serialName.contains(">")) {
            val typeParam = serialName.substringAfter("<").substringBefore(">")
            return listOf(typeParam)
          }
          return emptyList()
        }

        private fun getPolymorphicBaseType(descriptor: SerialDescriptor): String? {
          val serialName = descriptor.serialName
          return if (serialName.contains("<") && serialName.contains(">")) {
            serialName.substringAfter("<").substringBefore(">")
          } else {
            null
          }
        }

        private fun isSubclassOf(subclassSerialName: String, baseClassSerialName: String): Boolean {
          val subclassSimpleName = subclassSerialName.substringAfterLast(".")
          val baseSimpleName = baseClassSerialName.substringAfterLast(".")
          
          val subclassPackage = subclassSerialName.substringBeforeLast(".", "")
          val basePackage = baseClassSerialName.substringBeforeLast(".", "")
          
          return subclassSimpleName != baseSimpleName &&
                 !subclassSimpleName.contains("$") &&
                 !subclassSimpleName.contains("Companion") &&
                 subclassPackage == basePackage
        }
      }

    private fun collectDescriptorsFromModule(serializersModule: SerializersModule): Set<SerialDescriptor> {
      val descriptors = mutableSetOf<SerialDescriptor>()

      serializersModule.dumpTo(object : SerializersModuleCollector {
        override fun <T : Any> contextual(
          kClass: KClass<T>,
          provider: (typeArgumentsSerializers: List<KSerializer<*>>) -> KSerializer<*>
        ) {
          val serializer = provider(emptyList())
          descriptors.add(serializer.descriptor)
        }

        override fun <Base : Any, Sub : Base> polymorphic(
          baseClass: KClass<Base>,
          actualClass: KClass<Sub>,
          actualSerializer: KSerializer<Sub>
        ) {
          descriptors.add(actualSerializer.descriptor)
        }

        override fun <Base : Any> polymorphicDefaultSerializer(
          baseClass: KClass<Base>,
          defaultSerializerProvider: (value: Base) -> SerializationStrategy<Base>?
        ) {
        }

        override fun <Base : Any> polymorphicDefaultDeserializer(
          baseClass: KClass<Base>,
          defaultDeserializerProvider: (className: String?) -> DeserializationStrategy<Base>?
        ) {
        }
      })

      return descriptors
    }
  }
}
