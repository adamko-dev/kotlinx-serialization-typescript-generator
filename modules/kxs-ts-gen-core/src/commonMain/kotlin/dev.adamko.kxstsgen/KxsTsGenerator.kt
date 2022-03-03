package dev.adamko.kxstsgen

import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.elementDescriptors
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

class KxsTsGenerator(
  private val config: KxsTsConfig = KxsTsConfig(),
  private val serializersModule: SerializersModule = EmptySerializersModule,
) {

  private val codeGenerator: KxsTsSourceCodeGenerator = KxsTsSourceCodeGenerator(config)


  fun generate(vararg descriptors: SerialDescriptor): String {
//    val allDescriptors = getAllDescriptors(descriptors, setOf())


//    serializersModule.getContextualDescriptor()
//    serializersModule.getPolymorphicDescriptors()

    val elements = descriptors
      .fold(mapOf<TsElementId, TsElement>()) { acc, descriptor ->
        TsConverter(descriptor, acc).result
      }
      .values
      .toSet()

    return codeGenerator.joinElementsToString(elements)
  }


//  private fun convertSealedPolymorphic(
//    descriptor: SerialDescriptor,
//    discriminator: TsStructure.Enum,
//  ): TsElement {
//  }

}

class TsConverter(
  target: SerialDescriptor,
  existingElements: Map<TsElementId, TsElement>,
  private val serializersModule: SerializersModule = EmptySerializersModule,
) {

  private val existingElements = existingElements.toMutableMap()
  val result: Map<TsElementId, TsElement>
    get() = existingElements

  private val _dependencies = mutableMapOf<TsElementId, MutableSet<TsElementId>>()
  private val dependencies: Map<TsElementId, Set<TsElementId>>
    get() = _dependencies

  init {
    convertToTsElement(null, target)
  }

  private fun convertToTsElement(requestor: TsElementId?, target: SerialDescriptor): TsElementId {

    val targetId = TsElementId(target.serialName.removeSuffix("?"))

    return existingElements.getOrPut(targetId) {

      val result: TsElement = when (target.kind) {

        PrimitiveKind.BOOLEAN  -> TsPrimitive.TsBoolean

        PrimitiveKind.BYTE,
        PrimitiveKind.SHORT,
        PrimitiveKind.INT,
        PrimitiveKind.LONG,
        PrimitiveKind.FLOAT,
        PrimitiveKind.DOUBLE   -> TsPrimitive.TsNumber

        PrimitiveKind.CHAR,
        PrimitiveKind.STRING   -> TsPrimitive.TsString

        StructureKind.LIST     -> convertList(targetId, target)
        StructureKind.MAP      -> convertMap(targetId, target)

        StructureKind.CLASS,
        StructureKind.OBJECT   -> convertStructure(targetId, target)

        SerialKind.ENUM        -> convertEnum(targetId, target)

        PolymorphicKind.SEALED -> {
          // TODO PolymorphicKind.SEALED
          convertStructure(targetId, target)
        }
        PolymorphicKind.OPEN   -> {
          // TODO PolymorphicKind.SEALED
          val openTargetId = TsElementId(
            targetId.namespace + "." + targetId.name.substringAfter("<").substringBeforeLast(">")
          )
          convertStructure(openTargetId, target)
        }
        SerialKind.CONTEXTUAL  -> {
          // TODO SerialKind.CONTEXTUAL
          TsPrimitive.TsUnknown
        }
      }

      if (requestor != null) {
        dependencies.getOrElse(requestor) { mutableSetOf() }.plus(result)
      }

      result
    }.id
  }


  private fun convertStructure(
    targetId: TsElementId,
    structDescriptor: SerialDescriptor,
  ): TsElement {

    if (structDescriptor.isInline) {
      val fieldDescriptor = structDescriptor.elementDescriptors.first()
      val typeId = convertToTsElement(targetId, fieldDescriptor)
      val typeReference = TsTypeReference(typeId, fieldDescriptor.isNullable)
      return TsTypeAlias(targetId, setOf(typeReference))
    } else {

      val properties = structDescriptor.elementDescriptors.mapIndexed { index, field ->
        val name = structDescriptor.getElementName(index)
        val fieldTypeId = convertToTsElement(targetId, field)
        val fieldTypeReference = TsTypeReference(fieldTypeId, field.isNullable)
        when {
          structDescriptor.isElementOptional(index) -> TsProperty.Optional(name, fieldTypeReference)
          else                                      -> TsProperty.Required(name, fieldTypeReference)
        }
      }

      return TsStructure.TsInterface(targetId, properties)
    }
  }

  private fun convertEnum(
    targetId: TsElementId,
    enumDescriptor: SerialDescriptor,
  ): TsElement {
//    if (descriptor.elementsCount > 0) {
//      convertStructure(descriptor)
//    }

    return TsStructure.TsEnum(
      targetId,
      enumDescriptor.elementNames.toSet(),
    )
  }

  private fun convertList(
    targetId: TsElementId,
    listDescriptor: SerialDescriptor,
  ): TsStructure.TsList {

    val typeDescriptor = listDescriptor.elementDescriptors.first()
    val typeId =
      TsTypeReference(convertToTsElement(targetId, typeDescriptor), typeDescriptor.isNullable)

    return TsStructure.TsList(targetId, typeId)
  }

  private fun convertMap(
    targetId: TsElementId,
    mapDescriptor: SerialDescriptor,
  ): TsStructure.TsMap {

    val (keyDescriptor, valueDescriptor) = mapDescriptor.elementDescriptors.toList()
    val keyType =
      TsTypeReference(convertToTsElement(targetId, keyDescriptor), keyDescriptor.isNullable)
    val valueType =
      TsTypeReference(convertToTsElement(targetId, valueDescriptor), valueDescriptor.isNullable)
    return TsStructure.TsMap(targetId, keyType, valueType)
  }
}
