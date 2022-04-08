package dev.adamko.kxstsgen.core

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
  ): Set<TsElement>


  open class Default(
    val elementIdConverter: TsElementIdConverter,
    val mapTypeConverter: TsMapTypeConverter,
    val typeRefConverter: TsTypeRefConverter,
  ) : TsElementConverter {

    override operator fun invoke(
      descriptor: SerialDescriptor,
    ): Set<TsElement> {
      return when (descriptor.kind) {
        SerialKind.ENUM        -> setOf(convertEnum(descriptor))

        PrimitiveKind.BOOLEAN  -> setOf(TsLiteral.Primitive.TsBoolean)

        PrimitiveKind.CHAR,
        PrimitiveKind.STRING   -> setOf(TsLiteral.Primitive.TsString)

        PrimitiveKind.BYTE,
        PrimitiveKind.SHORT,
        PrimitiveKind.INT,
        PrimitiveKind.LONG,
        PrimitiveKind.FLOAT,
        PrimitiveKind.DOUBLE   -> setOf(TsLiteral.Primitive.TsNumber)

        StructureKind.LIST     -> setOf(
          when {
            descriptor.elementDescriptors.count() > 1 -> convertTuple(descriptor)
            else                                      -> convertList(descriptor)
          }
        )

        StructureKind.MAP      -> setOf(convertMap(descriptor))

        StructureKind.CLASS,
        StructureKind.OBJECT   -> setOf(
          when {
            descriptor.isInline -> convertTypeAlias(descriptor)
            else                -> convertInterface(descriptor)
          }
        )

        PolymorphicKind.SEALED -> convertDiscriminatedInterface(descriptor)

        // TODO handle contextual
        // TODO handle polymorphic open
        SerialKind.CONTEXTUAL,
        PolymorphicKind.OPEN   -> setOf(createTypeAliasAny(descriptor))
      }
    }


    /**
     * Handle sealed-polymorphic descriptors.
     *
     * Generate
     *
     * 1. a namespace that contains
     *   a. a 'type' enum, for each subclass
     *   b. the subclasses, as [TsDeclaration.TsInterface], with an additional 'type' field
     * 2. a type union of all subclasses
     */
    open fun convertDiscriminatedInterface(
      descriptor: SerialDescriptor,
    ): Set<TsDeclaration> {
      // namespace details
      val namespaceId = elementIdConverter(descriptor)
      val namespaceRef = TsTypeRef.Declaration(namespaceId, null, false)

      // discriminator name
      val discriminatorIndex = descriptor.elementDescriptors
        .indexOfFirst { it.kind == PrimitiveKind.STRING }
      val discriminatorName = descriptor.elementNames.elementAtOrNull(discriminatorIndex)

      // subclasses details
      val subclassInterfaces = descriptor
        .elementDescriptors
        .firstOrNull { it.kind == SerialKind.CONTEXTUAL }
        ?.elementDescriptors
        ?.flatMap { this(it) }
        ?.filterIsInstance<TsDeclaration.TsInterface>()
        ?.map { it.copy(id = TsElementId("${descriptor.serialName}.${it.id.name}")) }
        ?.toSet()
        ?: emptySet()

      val subInterfaceRefs: Map<TsTypeRef.Declaration, TsDeclaration.TsInterface> =
        subclassInterfaces.associateBy { subclass ->
          val subclassId = TsElementId(namespaceId.toString() + "." + subclass.id.name)
          TsTypeRef.Declaration(subclassId, namespaceRef, false)
        }

      // verify a discriminated interface can be created
      if (subInterfaceRefs.isEmpty() || discriminatorName.isNullOrBlank()) {
        return setOf(createTypeAliasAny(descriptor))
      } else {
        // discriminator enum
        val discriminatorEnum = TsDeclaration.TsEnum(
          TsElementId("${namespaceId.namespace}.${discriminatorName.replaceFirstChar { it.uppercaseChar() }}"),
          subInterfaceRefs.keys.map { it.id.name }.toSet(),
        )
        val discriminatorEnumRef = TsTypeRef.Declaration(discriminatorEnum.id, namespaceRef, false)

        // add discriminator property to subclasses
        val subInterfacesWithTypeProp = subInterfaceRefs.map { (subInterfaceRef, subclass) ->

          val literalTypeRef = TsTypeRef.Declaration(
            TsElementId("${discriminatorEnum.id.name}.${subInterfaceRef.id.name}"),
            discriminatorEnumRef,
            false,
          )

          val literalTypeProperty = TsProperty.Named(discriminatorName, false, literalTypeRef)

          subclass.copy(properties = setOf(literalTypeProperty) + subclass.properties)
        }

        // create type union and namespace
        val subInterfaceTypeUnion = TsDeclaration.TsTypeAlias(
          namespaceId,
          subInterfaceRefs.keys
        )

        val namespace = TsDeclaration.TsNamespace(
          namespaceId,
          buildSet {
            add(discriminatorEnum)
            addAll(subInterfacesWithTypeProp)
          }
        )

        return setOf(subInterfaceTypeUnion, namespace)
      }
    }


    open fun convertTypeAlias(
      structDescriptor: SerialDescriptor,
    ): TsDeclaration {
      val resultId = elementIdConverter(structDescriptor)
      val fieldDescriptor = structDescriptor.elementDescriptors.first()
      val fieldTypeRef = typeRefConverter(fieldDescriptor)
      return TsDeclaration.TsTypeAlias(resultId, fieldTypeRef)
    }


    open fun convertInterface(
      descriptor: SerialDescriptor,
    ): TsDeclaration {
      val resultId = elementIdConverter(descriptor)

      val properties = descriptor.elementDescriptors.mapIndexed { index, fieldDescriptor ->
        val name = descriptor.getElementName(index)
        val fieldTypeRef = typeRefConverter(fieldDescriptor)
        val optional = descriptor.isElementOptional(index)
        TsProperty.Named(name, optional, fieldTypeRef)
      }.toSet()

      return TsDeclaration.TsInterface(resultId, properties)
    }


    open fun convertTuple(
      descriptor: SerialDescriptor,
    ): TsDeclaration.TsTuple {
      val resultId = elementIdConverter(descriptor)

      val properties = descriptor.elementDescriptors.mapIndexed { index, fieldDescriptor ->
        val fieldTypeRef = typeRefConverter(fieldDescriptor)
        val optional = descriptor.isElementOptional(index)
        TsProperty.Unnamed(optional, fieldTypeRef)
      }

      return TsDeclaration.TsTuple(resultId, properties)
    }


    open fun convertEnum(
      enumDescriptor: SerialDescriptor,
    ): TsDeclaration.TsEnum {
      val resultId = elementIdConverter(enumDescriptor)
      return TsDeclaration.TsEnum(resultId, enumDescriptor.elementNames.toSet())
    }


    open fun convertList(
      listDescriptor: SerialDescriptor,
    ): TsLiteral.TsList {
      val elementDescriptor = listDescriptor.elementDescriptors.first()
      val elementTypeRef = typeRefConverter(elementDescriptor)
      return TsLiteral.TsList(elementTypeRef)
    }


    open fun convertMap(
      mapDescriptor: SerialDescriptor,
    ): TsLiteral.TsMap {

      val (keyDescriptor, valueDescriptor) = mapDescriptor.elementDescriptors.toList()

      val keyTypeRef = typeRefConverter(keyDescriptor)
      val valueTypeRef = typeRefConverter(valueDescriptor)

      val type = mapTypeConverter(keyDescriptor)

      return TsLiteral.TsMap(keyTypeRef, valueTypeRef, type)
    }


    open fun createTypeAliasAny(
      descriptor: SerialDescriptor,
    ): TsDeclaration.TsTypeAlias {
      val resultId = elementIdConverter(descriptor)
      val fieldTypeRef = TsTypeRef.Literal(TsLiteral.Primitive.TsAny, false)
      return TsDeclaration.TsTypeAlias(resultId, fieldTypeRef)
    }
  }
}
