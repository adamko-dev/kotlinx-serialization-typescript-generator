package dev.adamko.kxstsgen

import dev.adamko.kxstsgen.core.KxsTsSourceCodeGenerator
import dev.adamko.kxstsgen.core.SerializerDescriptorsExtractor
import dev.adamko.kxstsgen.core.TsDeclaration
import dev.adamko.kxstsgen.core.TsElement
import dev.adamko.kxstsgen.core.TsElementConverter
import dev.adamko.kxstsgen.core.TsElementId
import dev.adamko.kxstsgen.core.TsElementIdConverter
import dev.adamko.kxstsgen.core.TsMapTypeConverter
import dev.adamko.kxstsgen.core.TsTypeRef
import dev.adamko.kxstsgen.core.TsTypeRefConverter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor


/**
 * Generate TypeScript from [`@Serializable`][Serializable] Kotlin.
 *
 * The output can be controlled by the settings in [config],
 * or by setting hardcoded values in [serializerDescriptors] or [descriptorElements],
 * or changed by overriding any converter.
 *
 * @param[config] General settings that affect how KxTsGen works
 * @param[descriptorsExtractor] Given a [KSerializer], extract all [SerialDescriptor]s
 * @param[elementIdConverter] Create an [TsElementId] from a [SerialDescriptor]
 * @param[mapTypeConverter] Decides how [Map]s should be converted
 * @param[typeRefConverter] Creates [TsTypeRef]s
 * @param[elementConverter] Converts [SerialDescriptor]s to [TsElement]s
 * @param[sourceCodeGenerator] Convert [TsElement]s to TypeScript source code
 */
open class KxsTsGenerator(
  open val config: KxsTsConfig = KxsTsConfig(),

  open val descriptorsExtractor: SerializerDescriptorsExtractor = SerializerDescriptorsExtractor.Default,

  open val elementIdConverter: TsElementIdConverter = TsElementIdConverter.Default,

  open val mapTypeConverter: TsMapTypeConverter = TsMapTypeConverter.Default,

  open val typeRefConverter: TsTypeRefConverter =
    TsTypeRefConverter.Default(elementIdConverter, mapTypeConverter),

  open val elementConverter: TsElementConverter =
    TsElementConverter.Default(
      elementIdConverter,
      mapTypeConverter,
      typeRefConverter,
    ),

  open val sourceCodeGenerator: KxsTsSourceCodeGenerator = KxsTsSourceCodeGenerator.Default(config),
) {

  /**
   * Stateful cache of all [descriptors][SerialDescriptor] extracted from a
   * [serializer][KSerializer].
   *
   * To customise the descriptors that a serializer produces, set value into this map.
   */
  open val serializerDescriptors: MutableMap<KSerializer<*>, Set<SerialDescriptor>> = mutableMapOf()

  /**
   * Cache of all [elements][TsElement] that are created from any [descriptor][SerialDescriptor].
   *
   * To customise the elements that a descriptor produces, set value into this map.
   */
  open val descriptorElements: MutableMap<SerialDescriptor, Set<TsElement>> = mutableMapOf()

  open fun generate(vararg serializers: KSerializer<*>): String {
    return serializers
      .toSet()

      // 1. get all SerialDescriptors from a KSerializer
      .flatMap { serializer ->
        serializerDescriptors.getOrPut(serializer) { descriptorsExtractor(serializer) }
      }
      .toSet()

      // 2. convert each SerialDescriptor to some TsElements
      .flatMap { descriptor ->
        descriptorElements.getOrPut(descriptor) { elementConverter(descriptor) }
      }
      .toSet()

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
