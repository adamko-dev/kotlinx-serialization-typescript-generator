package dev.adamko.kxstsgen

import kotlinx.serialization.KSerializer


open class KxsTsGenerator(
  val config: KxsTsConfig = KxsTsConfig(),

  val descriptorsExtractor: SerializerDescriptorsExtractor = SerializerDescriptorsExtractor.Default,

  val elementIdConverter: TsElementIdConverter = TsElementIdConverter.Default,

  val mapTypeConverter: TsMapTypeConverter = TsMapTypeConverter.Default,

  val typeRefConverter: TsTypeRefConverter =
    TsTypeRefConverter.Default(elementIdConverter, mapTypeConverter),

  val elementConverter: TsElementConverter =
    TsElementConverter.Default(
      elementIdConverter,
      mapTypeConverter,
      typeRefConverter,
    ),

  val sourceCodeGenerator: KxsTsSourceCodeGenerator = KxsTsSourceCodeGenerator.Default(config)
) {

  fun generate(vararg serializers: KSerializer<*>): String {

    val descriptors = serializers.flatMap { descriptorsExtractor(it) }.toSet()

    val elements = descriptors.map { elementConverter(it) }

    return elements
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
