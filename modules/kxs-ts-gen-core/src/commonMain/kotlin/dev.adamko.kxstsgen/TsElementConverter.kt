package dev.adamko.kxstsgen


import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.elementDescriptors
import kotlinx.serialization.descriptors.elementNames

fun interface TsElementConverter {

  operator fun invoke(
    context: KxsTsConvertorContext,
    descriptor: SerialDescriptor,
  ): TsElement

  object Default : TsElementConverter {

    override operator fun invoke(
      context: KxsTsConvertorContext,
      descriptor: SerialDescriptor,
    ): TsElement {
      return when (descriptor.kind) {
        SerialKind.ENUM       -> convertEnum(context, descriptor)
        SerialKind.CONTEXTUAL -> {
          // TODO contextual
          TsLiteral.Primitive.TsAny
        }

        PrimitiveKind.BOOLEAN -> TsLiteral.Primitive.TsBoolean

        PrimitiveKind.CHAR,
        PrimitiveKind.STRING  -> TsLiteral.Primitive.TsString

        PrimitiveKind.BYTE,
        PrimitiveKind.SHORT,
        PrimitiveKind.INT,
        PrimitiveKind.LONG,
        PrimitiveKind.FLOAT,
        PrimitiveKind.DOUBLE  -> TsLiteral.Primitive.TsNumber

        StructureKind.LIST    -> convertList(context, descriptor)
        StructureKind.MAP     -> convertMap(context, descriptor)

        StructureKind.CLASS,
        StructureKind.OBJECT,
        PolymorphicKind.SEALED,
        PolymorphicKind.OPEN  -> when {
          descriptor.isInline -> convertTypeAlias(context, descriptor)
          else                -> convertInterface(context, descriptor)
        }
      }
    }


    private fun convertTypeAlias(
      context: KxsTsConvertorContext,
      structDescriptor: SerialDescriptor,
    ): TsDeclaration {
      val resultId = context.elementId(structDescriptor)
      val fieldDescriptor = structDescriptor.elementDescriptors.first()
      val fieldTypeRef = context.typeRef(fieldDescriptor)
      return TsDeclaration.TsTypeAlias(resultId, fieldTypeRef)

    }


    private fun convertInterface(
      context: KxsTsConvertorContext,
      structDescriptor: SerialDescriptor,
    ): TsDeclaration {
      val resultId = context.elementId(structDescriptor)

      val properties = structDescriptor.elementDescriptors.mapIndexed { index, fieldDescriptor ->
        val name = structDescriptor.getElementName(index)
        val fieldTypeRef = context.typeRef(fieldDescriptor)
        when {
          structDescriptor.isElementOptional(index) -> TsProperty.Optional(name, fieldTypeRef)
          else                                      -> TsProperty.Required(name, fieldTypeRef)
        }
      }.toSet()
      return TsDeclaration.TsInterface(resultId, properties, TsPolymorphicDiscriminator.Open)
    }


    private fun convertEnum(
      context: KxsTsConvertorContext,
      enumDescriptor: SerialDescriptor,
    ): TsDeclaration.TsEnum {
      val resultId = context.elementId(enumDescriptor)
      return TsDeclaration.TsEnum(resultId, enumDescriptor.elementNames.toSet())
    }


    private fun convertList(
      context: KxsTsConvertorContext,
      listDescriptor: SerialDescriptor,
    ): TsLiteral.TsList {
      val elementDescriptor = listDescriptor.elementDescriptors.first()
      val elementTypeRef = context.typeRef(elementDescriptor)
      return TsLiteral.TsList(elementTypeRef)
    }


    private fun convertMap(
      context: KxsTsConvertorContext,
      mapDescriptor: SerialDescriptor,
    ): TsLiteral.TsMap {

      val (keyDescriptor, valueDescriptor) = mapDescriptor.elementDescriptors.toList()

      val keyTypeRef = context.typeRef(keyDescriptor)
      val valueTypeRef = context.typeRef(valueDescriptor)

      val type = context.mapType(keyDescriptor)

      return TsLiteral.TsMap(keyTypeRef, valueTypeRef, type)
    }
  }
}
