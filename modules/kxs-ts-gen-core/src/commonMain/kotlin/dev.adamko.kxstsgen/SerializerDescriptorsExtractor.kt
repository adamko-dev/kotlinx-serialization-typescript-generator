package dev.adamko.kxstsgen

import dev.adamko.kxstsgen.util.MutableMapWithDefaultPut
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.elementDescriptors


/**
 * Recursively extract all descriptors from a serializer and its elements.
 */
fun interface SerializerDescriptorsExtractor {

  operator fun invoke(
    serializer: KSerializer<*>
  ): Set<SerialDescriptor>


  object Default : SerializerDescriptorsExtractor {

    override operator fun invoke(
      serializer: KSerializer<*>
    ): Set<SerialDescriptor> {
      return extractDescriptors(serializer.descriptor)
    }


    private tailrec fun extractDescriptors(
      current: SerialDescriptor? = null,
      queue: ArrayDeque<SerialDescriptor> = ArrayDeque(),
      extracted: Set<SerialDescriptor> = emptySet(),
    ): Set<SerialDescriptor> {
      return if (current == null) {
        extracted
      } else {
        val currentDescriptors = elementDescriptors.getValue(current)
        queue.addAll(currentDescriptors - extracted)
        extractDescriptors(queue.removeFirstOrNull(), queue, extracted + current)
      }
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
