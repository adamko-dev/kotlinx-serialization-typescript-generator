@file:OptIn(InternalSerializationApi::class)

package dev.adamko.kxstsgen

import dev.adamko.kxstsgen.util.MutableMapWithDefaultPut
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SealedClassSerializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.elementDescriptors


//  private val polymorphicDescriptorData: MutableMap<SerialDescriptor, PolymorphicDescriptorData?>
//    by MapWithPutDefault { descriptor ->
//      polymorphicSerializerData.entries.firstOrNull { (serializer, _) ->
//        serializer.descriptor == descriptor
//      }?.value
//    }

/**
 * Recursively extract all descriptors from a serializer and its elements.
 */
fun interface SerializerDescriptorsExtractor {

  operator fun invoke(
    allDescriptorData: Iterable<DescriptorData>,
  ): Set<SerialDescriptor>


  object Default : SerializerDescriptorsExtractor {

    override operator fun invoke(
      allDescriptorData: Iterable<DescriptorData>,
    ): Set<SerialDescriptor> {
      return allDescriptorData.flatMap { descriptorData ->
        when (descriptorData) {
          is DescriptorData.Polymorphic -> descriptorData.subclasses // only yield subclasses
          is DescriptorData.Monomorphic -> listOf(descriptorData.descriptor)
        }
      }.flatMap { descriptor -> extractedDescriptors.getValue(descriptor) }
        .distinct()
        .toSet()
    }

    private val extractedDescriptors by MutableMapWithDefaultPut<SerialDescriptor, Sequence<SerialDescriptor>> { descriptor ->
      sequence {
        val seen = mutableSetOf<SerialDescriptor>()
        val deque = ArrayDeque<SerialDescriptor>()
        deque.addLast(descriptor)

        while (deque.isNotEmpty()) {
          val next = deque.removeFirst()

          val nextElementDescriptors = elementDescriptors.getValue(next)

          nextElementDescriptors
            .filter { it !in seen }
            .forEach { deque.addLast(it) }

          seen.add(next)
          yield(next)
        }
      }.distinct()
    }

    private val elementDescriptors by MutableMapWithDefaultPut<SerialDescriptor, Iterable<SerialDescriptor>> { descriptor ->
      when (descriptor.kind) {
        SerialKind.ENUM       -> emptyList()

        SerialKind.CONTEXTUAL -> emptyList()

        PrimitiveKind.BOOLEAN,
        PrimitiveKind.BYTE,
        PrimitiveKind.CHAR,
        PrimitiveKind.SHORT,
        PrimitiveKind.INT,
        PrimitiveKind.LONG,
        PrimitiveKind.FLOAT,
        PrimitiveKind.DOUBLE,
        PrimitiveKind.STRING  -> emptyList()

        StructureKind.CLASS,
        StructureKind.LIST,
        StructureKind.MAP,
        StructureKind.OBJECT  -> descriptor.elementDescriptors

        PolymorphicKind.SEALED,
        PolymorphicKind.OPEN  -> descriptor
          .elementDescriptors
          .filter { it.kind is PolymorphicKind }
          .flatMap { it.elementDescriptors }
      }
    }

  }
}


/** Create [DescriptorData] */
fun interface DescriptorDataProcessor {
  operator fun invoke(
    context: KxsTsConvertorContext,
    config: KxsTsConfig,
    serializer: KSerializer<*>,
  ): DescriptorData

  object Default : DescriptorDataProcessor {
    override fun invoke(
      context: KxsTsConvertorContext,
      config: KxsTsConfig,
      serializer: KSerializer<*>,
    ): DescriptorData {
      return when (serializer) {
        is PolymorphicSerializer<*> ->
          DescriptorData.Polymorphic.Open(
            descriptor = serializer.descriptor,
            subclasses = config.polymorphicDescriptors.getValue(serializer.baseClass)
          )

        is SealedClassSerializer<*> ->
          DescriptorData.Polymorphic.Closed(
            descriptor = serializer.descriptor,
            subclasses = extractSealedSubclassSerializers(serializer).map { it.descriptor }.toSet(),
          )

        else                        ->
          DescriptorData.Monomorphic(serializer.descriptor)
      }
    }
  }
}


sealed class DescriptorData {
  abstract val descriptor: SerialDescriptor

  data class Monomorphic(
    override val descriptor: SerialDescriptor,
  ) : DescriptorData()

  sealed class Polymorphic : DescriptorData() {

    abstract val subclasses: Set<SerialDescriptor>

    data class Closed(
      override val descriptor: SerialDescriptor,
      override val subclasses: Set<SerialDescriptor>,
    ) : Polymorphic()

    data class Open(
      override val descriptor: SerialDescriptor,
      override val subclasses: Set<SerialDescriptor>,
    ) : Polymorphic()

  }

  override fun hashCode(): Int = descriptor.hashCode()
  override fun equals(other: Any?): Boolean = (descriptor == other)
}
