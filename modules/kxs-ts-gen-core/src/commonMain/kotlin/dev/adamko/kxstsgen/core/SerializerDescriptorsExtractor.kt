package dev.adamko.kxstsgen.core

import dev.adamko.kxstsgen.core.util.MutableMapWithDefaultPut
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
        PolymorphicKind.OPEN  ->
          // Polymorphic descriptors have 2 elements, the 'type' and 'value' - we don't need either
          // for generation, they're metadata that will be used later.
          // The elements of 'value' are similarly unneeded, but their elements might contain new
          // descriptors - so extract them
          descriptor.elementDescriptors
            .flatMap { it.elementDescriptors }
            .flatMap { it.elementDescriptors }

        // Example:
        // com.application.Polymorphic<MySealedClass>
        //   ├── 'type' descriptor (ignore / it's a String, so check its elements, it doesn't hurt)
        //   └── 'value' descriptor (check elements...)
        //        ├── com.application.Polymorphic<Subclass1>  (ignore)
        //        │   ├── Double                              (extract!)
        //        │   └── com.application.SomeOtherClass      (extract!)
        //        └── com.application.Polymorphic<Subclass2>  (ignore)
        //            ├── UInt                                (extract!)
        //            └── List<com.application.AnotherClass   (extract!
      }
    }
  }
}
