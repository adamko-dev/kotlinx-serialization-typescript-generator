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

  private fun convertToTsElement(requestor: TsElementId?, target: SerialDescriptor): TsElement {

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
    }
  }


  private fun convertStructure(
    targetId: TsElementId,
    structDescriptor: SerialDescriptor,
  ): TsElement {

    if (structDescriptor.isInline) {
      val fieldDescriptor = structDescriptor.elementDescriptors.first()
      val fieldType = convertToTsElement(targetId, fieldDescriptor)
      val fieldTyping = TsTyping(fieldType, fieldDescriptor.isNullable)
      return TsTypeAlias(targetId, fieldTyping)
    } else {

      val properties = structDescriptor.elementDescriptors.mapIndexed { index, fieldDescriptor ->
        val name = structDescriptor.getElementName(index)
        val fieldType = convertToTsElement(targetId, fieldDescriptor)
        val fieldTyping = TsTyping(fieldType, fieldDescriptor.isNullable)
        when {
          structDescriptor.isElementOptional(index) -> TsProperty.Optional(name, fieldTyping)
          else                                      -> TsProperty.Required(name, fieldTyping)
        }
      }

      return TsStructure.TsInterface(targetId, properties)
    }
  }

  private fun convertEnum(
    targetId: TsElementId,
    enumDescriptor: SerialDescriptor,
  ): TsElement {
    return TsStructure.TsEnum(
      targetId,
      enumDescriptor.elementNames.toSet(),
    )
  }

  private fun convertList(
    targetId: TsElementId,
    listDescriptor: SerialDescriptor,
  ): TsStructure.TsList {
    val elementDescriptor = listDescriptor.elementDescriptors.first()
    val elementType = convertToTsElement(targetId, elementDescriptor)
    val elementTyping = TsTyping(elementType, elementDescriptor.isNullable)
    return TsStructure.TsList(targetId, elementTyping)
  }

  private fun convertMap(
    targetId: TsElementId,
    mapDescriptor: SerialDescriptor,
  ): TsStructure.TsMap {

    val (keyDescriptor, valueDescriptor) = mapDescriptor.elementDescriptors.toList()

    val keyType = convertToTsElement(targetId, keyDescriptor)
    val keyTyping = TsTyping(keyType, keyDescriptor.isNullable)

    val valueType = convertToTsElement(targetId, valueDescriptor)
    val valueTyping = TsTyping(valueType, valueDescriptor.isNullable)

    return TsStructure.TsMap(targetId, keyTyping, valueTyping)
  }
}
