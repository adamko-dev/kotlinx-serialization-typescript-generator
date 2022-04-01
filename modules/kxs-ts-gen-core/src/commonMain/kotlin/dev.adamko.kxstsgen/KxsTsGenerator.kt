@file:OptIn(InternalSerializationApi::class)

package dev.adamko.kxstsgen

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModule


class KxsTsGenerator(
  private val config: KxsTsConfig = KxsTsConfig(),
) {

  constructor(
    serializersModule: SerializersModule,
  ) : this(KxsTsConfig(serializersModule = serializersModule))


  fun generate(vararg serializers: KSerializer<*>): String {

    val processor = KxsTsProcessor(config)

    serializers.forEach { processor.addSerializer(it) }

    return processor.process()
  }
}


//class TsConverter(
//  private val kxsTsConfig: KxsTsConfig,
//  elementsState: Map<SerialDescriptor, TsElement> = emptyMap(),
//) {
//  private val elementsState: MutableMap<SerialDescriptor, TsElement> = elementsState.toMutableMap()
//
//  private val descriptorExtractor = DescriptorExtractor(kxsTsConfig)
//
//  val convertedElements: Set<TsElement>
//    get() = elementsState.values.toSet()
//
//  operator fun invoke(serializer: KSerializer<*>) {
//    val descriptors: Map<SerialDescriptor, TsTypeRef> = descriptorExtractor(serializer)
//    descriptors.keys.forEach { descriptor -> convertToTsElement(descriptor, descriptors) }
//  }
//
//  private fun convertToTsElement(
//    descriptor: SerialDescriptor,
//    typeRefs: Map<SerialDescriptor, TsTypeRef>,
//  ): TsElement {
//    return elementsState.getOrPut(descriptor) {
//
//      when (descriptor.kind) {
//        SerialKind.ENUM       -> convertEnum(descriptor)
//        SerialKind.CONTEXTUAL -> {
//          // TODO contextual
//          TsLiteral.Primitive.TsAny
//        }
//
//        PrimitiveKind.BOOLEAN -> TsLiteral.Primitive.TsBoolean
//
//        PrimitiveKind.CHAR,
//        PrimitiveKind.STRING  -> TsLiteral.Primitive.TsString
//
//        PrimitiveKind.BYTE,
//        PrimitiveKind.SHORT,
//        PrimitiveKind.INT,
//        PrimitiveKind.LONG,
//        PrimitiveKind.FLOAT,
//        PrimitiveKind.DOUBLE  -> TsLiteral.Primitive.TsNumber
//
//        StructureKind.LIST    -> convertList(descriptor, typeRefs)
//        StructureKind.MAP     -> convertMap(descriptor, typeRefs)
//
//        StructureKind.CLASS,
//        StructureKind.OBJECT,
//        PolymorphicKind.SEALED,
//        PolymorphicKind.OPEN  -> when {
//          descriptor.isInline -> convertTypeAlias(descriptor, typeRefs)
//          else                -> convertInterface(descriptor, typeRefs)
//        }
//      }
//    }
//  }
//
//
//  private fun convertTypeAlias(
//    structDescriptor: SerialDescriptor,
//    typeRefs: Map<SerialDescriptor, TsTypeRef>,
//  ): TsDeclaration {
//    val resultId = createElementId(structDescriptor)
//    val fieldDescriptor = structDescriptor.elementDescriptors.first()
//    val fieldTypeRef = typeRefs[fieldDescriptor] ?: TsTypeRef.Unknown
//    return TsDeclaration.TsTypeAlias(resultId, fieldTypeRef)
//
//  }
//
//
//  private fun convertInterface(
//    structDescriptor: SerialDescriptor,
//    typeRefs: Map<SerialDescriptor, TsTypeRef>,
//  ): TsDeclaration {
//    val resultId = createElementId(structDescriptor)
//
//    val properties = structDescriptor.elementDescriptors.mapIndexed { index, fieldDescriptor ->
//      val name = structDescriptor.getElementName(index)
//      val fieldTypeRef = typeRefs[fieldDescriptor] ?: TsTypeRef.Unknown
//      when {
//        structDescriptor.isElementOptional(index) -> TsProperty.Optional(name, fieldTypeRef)
//        else                                      -> TsProperty.Required(name, fieldTypeRef)
//      }
//    }.toSet()
//    return TsDeclaration.TsInterface(resultId, properties, TsPolymorphicDiscriminator.Open)
//  }
//
//
//  private fun convertEnum(
//    enumDescriptor: SerialDescriptor,
//  ): TsDeclaration.TsEnum {
//    val resultId = createElementId(enumDescriptor)
//    return TsDeclaration.TsEnum(resultId, enumDescriptor.elementNames.toSet())
//  }
//
//
//  private fun convertList(
//    listDescriptor: SerialDescriptor,
//    typeRefs: Map<SerialDescriptor, TsTypeRef>,
//  ): TsLiteral.TsList {
//    val elementDescriptor = listDescriptor.elementDescriptors.first()
//    val elementTypeRef = typeRefs[elementDescriptor] ?: TsTypeRef.Unknown
//    return TsLiteral.TsList(elementTypeRef)
//  }
//
//
//  private fun convertMap(
//    mapDescriptor: SerialDescriptor,
//    typeRefs: Map<SerialDescriptor, TsTypeRef>,
//  ): TsLiteral.TsMap {
//
//    val (keyDescriptor, valueDescriptor) = mapDescriptor.elementDescriptors.toList()
//
//    val keyTypeRef = typeRefs[keyDescriptor] ?: TsTypeRef.Unknown
//    val valueTypeRef = typeRefs[valueDescriptor] ?: TsTypeRef.Unknown
//
//    val type = convertMapType(keyDescriptor)
//
//    return TsLiteral.TsMap(keyTypeRef, valueTypeRef, type)
//  }
//
//
//}
//
//
//class DescriptorExtractor(
//  private val kxsTsConfig: KxsTsConfig,
//) {
//
//  operator fun invoke(serializer: KSerializer<*>): Map<SerialDescriptor, TsTypeRef> {
//    return sequence {
//      when (serializer) {
//        is PolymorphicSerializer<*> -> {
//          val x = kxsTsConfig.serializersModule.getPolymorphicDescriptors(serializer.descriptor)
//          yieldAll(x)
//        }
//        is SealedClassSerializer<*> ->
//          yield(serializer.descriptor)
//        is ContextualSerializer<*>  ->
//          yield(extractContextualSerializer(serializer, kxsTsConfig)?.descriptor)
//        else                        ->
//          yield(serializer.descriptor)
//      }
//    }.filterNotNull()
//      .flatMap { descriptor -> extractAll(descriptor) }
//      .distinct()
//      .associateWith { descriptor ->
//        createTypeRef(descriptor)
//      }
//  }
//
//  private fun extractAll(descriptor: SerialDescriptor): Sequence<SerialDescriptor> {
//    return sequence {
//      val seen = mutableSetOf<SerialDescriptor>()
//      val deque = ArrayDeque<SerialDescriptor>()
//      deque.addLast(descriptor)
//      while (deque.isNotEmpty()) {
//        val next = deque.removeFirst()
//
//        val nextElementDescriptors = extractElementDescriptors(next)
//
//        nextElementDescriptors
//          .filter { it !in seen }
//          .forEach { deque.addLast(it) }
//
//        seen.add(next)
//        yield(next)
//      }
//    }.distinct()
//  }
//
//
//  private fun extractElementDescriptors(serialDescriptor: SerialDescriptor): Iterable<SerialDescriptor> {
//    return when (serialDescriptor.kind) {
//      SerialKind.ENUM       -> emptyList()
//
//      SerialKind.CONTEXTUAL -> emptyList()
//
//      PrimitiveKind.BOOLEAN,
//      PrimitiveKind.BYTE,
//      PrimitiveKind.CHAR,
//      PrimitiveKind.SHORT,
//      PrimitiveKind.INT,
//      PrimitiveKind.LONG,
//      PrimitiveKind.FLOAT,
//      PrimitiveKind.DOUBLE,
//      PrimitiveKind.STRING  -> emptyList()
//
//      StructureKind.CLASS,
//      StructureKind.LIST,
//      StructureKind.MAP,
//      StructureKind.OBJECT  -> serialDescriptor.elementDescriptors
//
//      PolymorphicKind.SEALED,
//      PolymorphicKind.OPEN  -> serialDescriptor
//        .elementDescriptors
//        .filter { it.kind is PolymorphicKind }
//        .flatMap { it.elementDescriptors }
//    }
//  }
//
//
//  private fun createTypeRef(descriptor: SerialDescriptor): TsTypeRef {
//    return when (descriptor.kind) {
//      is PrimitiveKind     -> {
//        val tsPrimitive = when (descriptor.kind as PrimitiveKind) {
//          PrimitiveKind.BOOLEAN -> TsLiteral.Primitive.TsBoolean
//
//          PrimitiveKind.BYTE,
//          PrimitiveKind.SHORT,
//          PrimitiveKind.INT,
//          PrimitiveKind.LONG,
//          PrimitiveKind.FLOAT,
//          PrimitiveKind.DOUBLE  -> TsLiteral.Primitive.TsNumber
//
//          PrimitiveKind.CHAR,
//          PrimitiveKind.STRING  -> TsLiteral.Primitive.TsString
//        }
//        TsTypeRef.Literal(tsPrimitive, descriptor.isNullable)
//      }
//
//      StructureKind.LIST   -> {
//        val elementDescriptor = descriptor.elementDescriptors.first()
//        val elementTypeRef = createTypeRef(elementDescriptor)
//        val listRef = TsLiteral.TsList(elementTypeRef)
//        TsTypeRef.Literal(listRef, descriptor.isNullable)
//      }
//      StructureKind.MAP    -> {
//        val (keyDescriptor, valueDescriptor) = descriptor.elementDescriptors.toList()
//        val keyTypeRef = createTypeRef(keyDescriptor)
//        val valueTypeRef = createTypeRef(valueDescriptor)
//        val type = convertMapType(keyDescriptor)
//        val map = TsLiteral.TsMap(keyTypeRef, valueTypeRef, type)
//        TsTypeRef.Literal(map, descriptor.isNullable)
//      }
//
//      SerialKind.CONTEXTUAL,
//      PolymorphicKind.SEALED,
//      PolymorphicKind.OPEN,
//      SerialKind.ENUM,
//      StructureKind.CLASS,
//      StructureKind.OBJECT -> {
//        val id = createElementId(descriptor)
//        TsTypeRef.Named(id, descriptor.isNullable)
//      }
//    }
//  }
//
//}
//
//
//
//
//private fun createElementId(descriptor: SerialDescriptor): TsElementId {
//
//  val targetId = TsElementId(descriptor.serialName.removeSuffix("?"))
//
//  return when (descriptor.kind) {
//    PolymorphicKind.OPEN -> TsElementId(
//      targetId.namespace + "." + targetId.name.substringAfter("<").substringBeforeLast(">")
//    )
//    PolymorphicKind.SEALED,
//    PrimitiveKind.BOOLEAN,
//    PrimitiveKind.BYTE,
//    PrimitiveKind.CHAR,
//    PrimitiveKind.DOUBLE,
//    PrimitiveKind.FLOAT,
//    PrimitiveKind.INT,
//    PrimitiveKind.LONG,
//    PrimitiveKind.SHORT,
//    PrimitiveKind.STRING,
//    SerialKind.CONTEXTUAL,
//    SerialKind.ENUM,
//    StructureKind.CLASS,
//    StructureKind.LIST,
//    StructureKind.MAP,
//    StructureKind.OBJECT -> targetId
//  }
//}
//
//private fun convertMapType(keyDescriptor: SerialDescriptor): TsLiteral.TsMap.Type {
//  return when (keyDescriptor.kind) {
//    SerialKind.ENUM      -> TsLiteral.TsMap.Type.MAPPED_OBJECT
//
//    PrimitiveKind.STRING -> TsLiteral.TsMap.Type.INDEX_SIGNATURE
//
//    SerialKind.CONTEXTUAL,
//    PrimitiveKind.BOOLEAN,
//    PrimitiveKind.BYTE,
//    PrimitiveKind.CHAR,
//    PrimitiveKind.SHORT,
//    PrimitiveKind.INT,
//    PrimitiveKind.LONG,
//    PrimitiveKind.FLOAT,
//    PrimitiveKind.DOUBLE,
//    StructureKind.CLASS,
//    StructureKind.LIST,
//    StructureKind.MAP,
//    StructureKind.OBJECT,
//    PolymorphicKind.SEALED,
//    PolymorphicKind.OPEN -> TsLiteral.TsMap.Type.MAP
//  }
//}
