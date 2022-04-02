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
    descriptorData: DescriptorData?,
  ): TsElement

  object Default : TsElementConverter {

    override operator fun invoke(
      context: KxsTsConvertorContext,
      descriptor: SerialDescriptor,
      descriptorData: DescriptorData?,
    ): TsElement {
      return convertMonomorphicDescriptor(context, descriptor)

//      return when (descriptorData) {
//        is DescriptorData.Polymorphic -> convertPolymorphicDescriptor(
//          context,
//          descriptorData,
//        )
//        null,
//        is DescriptorData.Monomorphic -> setOf(convertMonomorphicDescriptor(context, descriptor))
//      }

    }


    private fun convertMonomorphicDescriptor(
      context: KxsTsConvertorContext,
      descriptor: SerialDescriptor,
    ): TsElement {
      return when (descriptor.kind) {
        SerialKind.ENUM       -> convertEnum(context, descriptor)

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
        StructureKind.OBJECT  -> when {
          descriptor.isInline -> convertTypeAlias(context, descriptor)
          else                -> convertInterface(context, descriptor, null)
        }

        // TODO handle contextual
        SerialKind.CONTEXTUAL -> TsLiteral.Primitive.TsAny

        PolymorphicKind.SEALED,
        PolymorphicKind.OPEN  -> convertPolymorphic(context, descriptor)
      }
    }

    private fun convertPolymorphic(
      context: KxsTsConvertorContext,
      descriptor: SerialDescriptor,
    ): TsDeclaration {

      val discriminatorIndex = descriptor.elementDescriptors
        .indexOfFirst { it.kind == PrimitiveKind.STRING }
      val discriminatorName = descriptor.getElementName(discriminatorIndex)

      val subclasses = descriptor
        .elementDescriptors
        .first { it.kind == SerialKind.CONTEXTUAL }
        .elementDescriptors

      val subclassInterfaces = subclasses
        .map { convertMonomorphicDescriptor(context, it) }
        .filterIsInstance<TsDeclaration.TsInterface>()
        .map { it.copy(id = TsElementId("${descriptor.serialName}.${it.id.name}")) }
        .toSet()

      val polymorphism = when (descriptor.kind) {
        PolymorphicKind.SEALED -> TsPolymorphism.Sealed(discriminatorName, subclassInterfaces)
        PolymorphicKind.OPEN   -> TsPolymorphism.Open(discriminatorName, subclassInterfaces)
        else                   -> error("unexpected SerialKind ${descriptor.kind}") // TODO 'else' branch shouldn't be needed
      }

      return convertInterface(context, descriptor, polymorphism)
    }


    private fun convertTypeAlias(
      context: KxsTsConvertorContext,
      structDescriptor: SerialDescriptor,
    ): TsDeclaration {
      val resultId = context.elementId(structDescriptor)
      val fieldDescriptor = structDescriptor.elementDescriptors.first()
      val fieldTypeRef = context.typeRef(fieldDescriptor)
      return TsDeclaration.TsType(resultId, fieldTypeRef)
    }


    private fun convertInterface(
      context: KxsTsConvertorContext,
      descriptor: SerialDescriptor,
      polymorphism: TsPolymorphism?,
    ): TsDeclaration {
      val resultId = context.elementId(descriptor)

      val properties = descriptor.elementDescriptors.mapIndexed { index, fieldDescriptor ->
        val name = descriptor.getElementName(index)
        val fieldTypeRef = context.typeRef(fieldDescriptor)
        when {
          descriptor.isElementOptional(index) -> TsProperty.Optional(name, fieldTypeRef)
          else                                -> TsProperty.Required(name, fieldTypeRef)
        }
      }.toSet()
      return TsDeclaration.TsInterface(resultId, properties, polymorphism)
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
