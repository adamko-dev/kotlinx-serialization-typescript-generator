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
    descriptor: SerialDescriptor,
  ): TsElement


  open class Default(
    val elementIdConverter: TsElementIdConverter,
    val mapTypeConverter: TsMapTypeConverter,
    val typeRefConverter: TsTypeRefConverter,
  ) : TsElementConverter {

    override operator fun invoke(
      descriptor: SerialDescriptor,
    ): TsElement {
      return when (descriptor.kind) {
        SerialKind.ENUM        -> convertEnum(descriptor)

        PrimitiveKind.BOOLEAN  -> TsLiteral.Primitive.TsBoolean

        PrimitiveKind.CHAR,
        PrimitiveKind.STRING   -> TsLiteral.Primitive.TsString

        PrimitiveKind.BYTE,
        PrimitiveKind.SHORT,
        PrimitiveKind.INT,
        PrimitiveKind.LONG,
        PrimitiveKind.FLOAT,
        PrimitiveKind.DOUBLE   -> TsLiteral.Primitive.TsNumber

        StructureKind.LIST     -> convertList(descriptor)
        StructureKind.MAP      -> convertMap(descriptor)

        StructureKind.CLASS,
        StructureKind.OBJECT   -> when {
          descriptor.isInline -> convertTypeAlias(descriptor)
          else                -> convertInterface(descriptor, null)
        }

        PolymorphicKind.SEALED -> convertPolymorphic(descriptor)

        // TODO handle contextual
        // TODO handle polymorphic open
        SerialKind.CONTEXTUAL,
        PolymorphicKind.OPEN   -> {
          val resultId = elementIdConverter(descriptor)
          val fieldTypeRef = TsTypeRef.Literal(TsLiteral.Primitive.TsAny, false)
          TsDeclaration.TsTypeAlias(resultId, fieldTypeRef)
        }
      }
    }


    fun convertPolymorphic(
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
        .map { this(it) }
        .filterIsInstance<TsDeclaration.TsInterface>()
        .map { it.copy(id = TsElementId("${descriptor.serialName}.${it.id.name}")) }
        .toSet()

      val polymorphism = when (descriptor.kind) {
        PolymorphicKind.SEALED -> TsPolymorphism.Sealed(discriminatorName, subclassInterfaces)
        PolymorphicKind.OPEN   -> TsPolymorphism.Open(discriminatorName, subclassInterfaces)
        else                   -> error("Can't convert non-polymorphic SerialKind ${descriptor.kind} to polymorphic interface")
      }

      return convertInterface(descriptor, polymorphism)
    }


    fun convertTypeAlias(
      structDescriptor: SerialDescriptor,
    ): TsDeclaration {
      val resultId = elementIdConverter(structDescriptor)
      val fieldDescriptor = structDescriptor.elementDescriptors.first()
      val fieldTypeRef = typeRefConverter(fieldDescriptor)
      return TsDeclaration.TsTypeAlias(resultId, fieldTypeRef)
    }


    fun convertInterface(
      descriptor: SerialDescriptor,
      polymorphism: TsPolymorphism?,
    ): TsDeclaration {
      val resultId = elementIdConverter(descriptor)

      val properties = descriptor.elementDescriptors.mapIndexed { index, fieldDescriptor ->
        val name = descriptor.getElementName(index)
        val fieldTypeRef = typeRefConverter(fieldDescriptor)
        when {
          descriptor.isElementOptional(index) -> TsProperty.Optional(name, fieldTypeRef)
          else                                -> TsProperty.Required(name, fieldTypeRef)
        }
      }.toSet()
      return TsDeclaration.TsInterface(resultId, properties, polymorphism)
    }


    fun convertEnum(
      enumDescriptor: SerialDescriptor,
    ): TsDeclaration.TsEnum {
      val resultId = elementIdConverter(enumDescriptor)
      return TsDeclaration.TsEnum(resultId, enumDescriptor.elementNames.toSet())
    }


    fun convertList(
      listDescriptor: SerialDescriptor,
    ): TsLiteral.TsList {
      val elementDescriptor = listDescriptor.elementDescriptors.first()
      val elementTypeRef = typeRefConverter(elementDescriptor)
      return TsLiteral.TsList(elementTypeRef)
    }


    fun convertMap(
      mapDescriptor: SerialDescriptor,
    ): TsLiteral.TsMap {

      val (keyDescriptor, valueDescriptor) = mapDescriptor.elementDescriptors.toList()

      val keyTypeRef = typeRefConverter(keyDescriptor)
      val valueTypeRef = typeRefConverter(valueDescriptor)

      val type = mapTypeConverter(keyDescriptor)

      return TsLiteral.TsMap(keyTypeRef, valueTypeRef, type)
    }
  }
}
