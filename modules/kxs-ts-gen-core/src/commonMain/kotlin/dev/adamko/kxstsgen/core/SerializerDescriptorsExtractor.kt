package dev.adamko.kxstsgen.core

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.modules.SerializersModule



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
      return extractDescriptors(serializer.descriptor)
        .distinctBy { it.nullable }
        .toSet()
    }

    private tailrec fun extractDescriptors(
      current: SerialDescriptor? = null,
      queue: ArrayDeque<SerialDescriptor> = ArrayDeque(),
      extracted: Set<SerialDescriptor> = emptySet(),
    ): Set<SerialDescriptor> {
      return if (current == null) {
        extracted
      } else {
        val currentDescriptors = elementDescriptorsExtractor.elementDescriptors(current)
        queue.addAll(currentDescriptors - extracted)
        extractDescriptors(queue.removeFirstOrNull(), queue, extracted + current)
      }
    }
  }
}


fun interface TsElementDescriptorsExtractor {
  fun elementDescriptors(descriptor: SerialDescriptor): Iterable<SerialDescriptor>

  companion object {

    @Suppress("UNUSED_PARAMETER")
    fun default(
      serializersModule: SerializersModule,
    ) =
      TsElementDescriptorsExtractor { descriptor ->
        // TODO: Use serializersModule to resolve contextual and polymorphic serializers
        // The module is available for future enhancements where it can be used to:
        // - Resolve @Contextual serializer types to their actual descriptors
        // - Discover all polymorphic subclasses registered in the module
        when (descriptor.kind) {
          SerialKind.ENUM       -> emptyList()

          SerialKind.CONTEXTUAL -> {
            emptyList()
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
          PolymorphicKind.OPEN  ->
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
