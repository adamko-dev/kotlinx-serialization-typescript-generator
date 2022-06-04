package dev.adamko.kxstsgen

import dev.adamko.kxstsgen.core.SerializerDescriptorsExtractor
import dev.adamko.kxstsgen.core.TsDeclaration
import dev.adamko.kxstsgen.core.TsElement
import dev.adamko.kxstsgen.core.TsElementConverter
import dev.adamko.kxstsgen.core.TsElementId
import dev.adamko.kxstsgen.core.TsElementIdConverter
import dev.adamko.kxstsgen.core.TsLiteral
import dev.adamko.kxstsgen.core.TsMapTypeConverter
import dev.adamko.kxstsgen.core.TsSourceCodeGenerator
import dev.adamko.kxstsgen.core.TsTypeRef
import dev.adamko.kxstsgen.core.TsTypeRefConverter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.nullable


/**
 * Generate TypeScript from [`@Serializable`][Serializable] Kotlin.
 *
 * The output can be controlled by the settings in [config],
 * or by setting hardcoded values in [serializerDescriptorOverrides] or [descriptorOverrides],
 * or changed by overriding any converter.
 *
 * @param[config] General settings that affect how KxsTsGen works
 * @param[sourceCodeGenerator] Convert [TsElement]s to TypeScript source code
 */
open class KxsTsGenerator(
  open val config: KxsTsConfig = KxsTsConfig(),

  open val sourceCodeGenerator: TsSourceCodeGenerator = TsSourceCodeGenerator.Default(config),
) {


  val serializerDescriptorOverrides: MutableMap<KSerializer<*>, Set<SerialDescriptor>> =
    mutableMapOf()

  val descriptorOverrides: MutableMap<SerialDescriptor, TsElement> = mutableMapOf()

  private fun findOverride(descriptor: SerialDescriptor): TsElement? {
    return descriptorOverrides.entries.run {
      firstOrNull { it.key == descriptor } ?: firstOrNull { it.key.nullable == descriptor.nullable }
    }?.value
  }

  open fun findMapTypeOverride(descriptor: SerialDescriptor): TsLiteral.TsMap.Type? {
    return when (findOverride(descriptor)) {
      null                         -> null

      is TsDeclaration.TsEnum      -> TsLiteral.TsMap.Type.MAPPED_OBJECT

      is TsLiteral.Custom,
      TsLiteral.Primitive.TsNumber,
      TsLiteral.Primitive.TsString -> TsLiteral.TsMap.Type.INDEX_SIGNATURE

      else                         -> TsLiteral.TsMap.Type.MAP
    }
  }


  open val descriptorsExtractor = object : SerializerDescriptorsExtractor {
    val extractor: SerializerDescriptorsExtractor = SerializerDescriptorsExtractor.Default
    val cache: MutableMap<KSerializer<*>, Set<SerialDescriptor>> = mutableMapOf()

    override fun invoke(serializer: KSerializer<*>): Set<SerialDescriptor> =
      cache.getOrPut(serializer) {
        serializerDescriptorOverrides[serializer] ?: extractor(serializer)
      }
  }


  val elementIdConverter: TsElementIdConverter = object : TsElementIdConverter {
    private val converter: TsElementIdConverter = TsElementIdConverter.Default
    private val cache: MutableMap<SerialDescriptor, TsElementId> = mutableMapOf()

    override fun invoke(descriptor: SerialDescriptor): TsElementId =
      cache.getOrPut(descriptor) {
        when (val override = findOverride(descriptor)) {
          is TsDeclaration -> override.id
          else             -> converter(descriptor)
        }
      }
  }


  val mapTypeConverter: TsMapTypeConverter = object : TsMapTypeConverter {
    private val converter = TsMapTypeConverter.Default
    private val cache: MutableMap<Pair<SerialDescriptor, SerialDescriptor>, TsLiteral.TsMap.Type> =
      mutableMapOf()

    override fun invoke(
      keyDescriptor: SerialDescriptor,
      valDescriptor: SerialDescriptor,
    ): TsLiteral.TsMap.Type =
      cache.getOrPut(keyDescriptor to valDescriptor) {
        findMapTypeOverride(keyDescriptor) ?: converter(keyDescriptor, valDescriptor)
      }
  }


  val typeRefConverter: TsTypeRefConverter = object : TsTypeRefConverter {
    private val converter = TsTypeRefConverter.Default(elementIdConverter, mapTypeConverter)
    val cache: MutableMap<SerialDescriptor, TsTypeRef> = mutableMapOf()

    override fun invoke(descriptor: SerialDescriptor): TsTypeRef =
      cache.getOrPut(descriptor) {
        when (val override = findOverride(descriptor)) {
          null             -> converter(descriptor)
          is TsLiteral     -> TsTypeRef.Literal(override, descriptor.isNullable)
          is TsDeclaration -> TsTypeRef.Declaration(override.id, null, descriptor.isNullable)
        }
      }
  }


  val elementConverter: TsElementConverter = object : TsElementConverter {
    private val converter = TsElementConverter.Default(
      elementIdConverter,
      mapTypeConverter,
      typeRefConverter,
    )
    val cache: MutableMap<SerialDescriptor, Set<TsElement>> = mutableMapOf()

    override fun invoke(descriptor: SerialDescriptor): Set<TsElement> =
      cache.getOrPut(descriptor) {
        when (val override = findOverride(descriptor)) {
          null -> converter(descriptor)
          else -> setOf(override)
        }
      }
  }


  open fun generate(vararg serializers: KSerializer<*>): String {
    return serializers
      .toSet()

      // 1. get all SerialDescriptors from a KSerializer
      .flatMap { serializer -> descriptorsExtractor(serializer) }
      .toSet()

      // 2. convert each SerialDescriptor to some TsElements
      .flatMap { descriptor -> elementConverter(descriptor) }
      .toSet()

      // 3. group by namespaces
      .groupBy { element -> sourceCodeGenerator.groupElementsBy(element) }

      // 4. convert to source code
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
