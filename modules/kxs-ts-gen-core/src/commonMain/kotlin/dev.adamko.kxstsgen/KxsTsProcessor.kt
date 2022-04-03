package dev.adamko.kxstsgen


import dev.adamko.kxstsgen.util.MutableMapWithDefaultPut
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor


class KxsTsProcessor(
  val config: KxsTsConfig,
  val context: KxsTsConvertorContext = KxsTsConvertorContext.Default(),

  val descriptorDataProcessor: DescriptorDataProcessor =
    DescriptorDataProcessor.Default,

  val serializerDescriptorsExtractor: SerializerDescriptorsExtractor =
    SerializerDescriptorsExtractor.Default,

  val sourceCodeGenerator: KxsTsSourceCodeGenerator =
    KxsTsSourceCodeGenerator.Default(config, context),

  ) {

  private val descriptorData by MutableMapWithDefaultPut<KSerializer<*>, DescriptorData> { serializer ->
    descriptorDataProcessor(context, config, serializer)
  }

  private val serializers = mutableSetOf<KSerializer<*>>()

  fun addSerializer(serializer: KSerializer<*>) {
    serializers += serializer
  }

  fun process(): String {
    val allDescriptorData: Map<SerialDescriptor, DescriptorData> =
      serializers.associate { serializer ->
        serializer.descriptor to descriptorData.getValue(serializer)
      }

    val allSerialDescriptors: Set<SerialDescriptor> =
      serializerDescriptorsExtractor(allDescriptorData.values)

    val allTsElements: List<TsElement> =
      allSerialDescriptors.map { descriptor ->
        TsElementConverter.Default(context, descriptor, allDescriptorData[descriptor])
      }

    return allTsElements
      .groupBy { element -> sourceCodeGenerator.groupElementsBy(element) }
      .mapValues { (_, elements) ->
        elements
          .filterIsInstance<TsDeclaration>()
          .map { element -> sourceCodeGenerator.generateDeclaration(element) }
          .filter { it.isNotBlank() }
          .joinToString(config.declarationSeparator)
      }
      .values// TODO  create namespaces
      .joinToString(config.declarationSeparator)
  }


}
