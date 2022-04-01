package dev.adamko.kxstsgen

import dev.adamko.kxstsgen.util.MutableMapWithDefaultPut
import kotlinx.serialization.descriptors.SerialDescriptor

interface KxsTsConvertorContext {
//  val config: KxsTsConfig

  fun elementId(descriptor: SerialDescriptor): TsElementId

  fun typeRef(descriptor: SerialDescriptor): TsTypeRef

  fun mapType(descriptor: SerialDescriptor): TsLiteral.TsMap.Type

  fun element(descriptor: SerialDescriptor): TsElement

  class Default(
    elementIds: Map<SerialDescriptor, TsElementId> = mapOf(),
    typeRefs: Map<SerialDescriptor, TsTypeRef> = mapOf(),
    mapTypes: Map<SerialDescriptor, TsLiteral.TsMap.Type> = mapOf(),
    convertedElements: Map<SerialDescriptor, TsElement> = mapOf(),
    private val elementIdConverter: TsElementIdConverter = TsElementIdConverter.Default,
    private val elementConverter: TsElementConverter = TsElementConverter.Default,
    private val typeRefConverter: TsTypeRefConverter = TsTypeRefConverter.Default,
    private val mapTypeConverter: TsMapTypeConverter = TsMapTypeConverter.Default,
  ) : KxsTsConvertorContext {

    private val elementIds: MutableMap<SerialDescriptor, TsElementId>
      by MutableMapWithDefaultPut(elementIds) { elementIdConverter(it) }

    private val typeRefs: MutableMap<SerialDescriptor, TsTypeRef>
      by MutableMapWithDefaultPut(typeRefs) { typeRefConverter(this, it) }

    private val mapTypes: MutableMap<SerialDescriptor, TsLiteral.TsMap.Type>
      by MutableMapWithDefaultPut(mapTypes) { mapTypeConverter(it) }

    private val convertedElements: MutableMap<SerialDescriptor, TsElement>
      by MutableMapWithDefaultPut(convertedElements) { elementConverter(this, it) }

    override fun elementId(descriptor: SerialDescriptor): TsElementId =
      elementIds.getValue(descriptor)

    override fun typeRef(descriptor: SerialDescriptor): TsTypeRef =
      typeRefs.getValue(descriptor)

    override fun mapType(descriptor: SerialDescriptor): TsLiteral.TsMap.Type =
      mapTypes.getValue(descriptor)

    override fun element(descriptor: SerialDescriptor): TsElement =
      convertedElements.getValue(descriptor)

  }

}
