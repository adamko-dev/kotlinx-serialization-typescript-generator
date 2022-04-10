package dev.adamko.kxstsgen.core

import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.elementDescriptors


fun interface TsTypeRefConverter {

  operator fun invoke(descriptor: SerialDescriptor): TsTypeRef


  open class Default(
    val elementIdConverter: TsElementIdConverter = TsElementIdConverter.Default,
    val mapTypeConverter: TsMapTypeConverter = TsMapTypeConverter.Default,
  ) : TsTypeRefConverter {

    override operator fun invoke(
      descriptor: SerialDescriptor,
    ): TsTypeRef {
      return when (val descriptorKind = descriptor.kind) {
        is PrimitiveKind     -> primitiveTypeRef(descriptor, descriptorKind)

        StructureKind.LIST   -> when {
          descriptor.elementDescriptors.count() > 1 -> declarationTypeRef(descriptor)
          else                                      -> listTypeRef(descriptor)
        }
        StructureKind.MAP    -> mapTypeRef(descriptor)

        SerialKind.CONTEXTUAL,
        PolymorphicKind.SEALED,
        PolymorphicKind.OPEN,
        SerialKind.ENUM,
        StructureKind.CLASS,
        StructureKind.OBJECT -> declarationTypeRef(descriptor)
      }
    }

    fun primitiveTypeRef(
      descriptor: SerialDescriptor,
      kind: PrimitiveKind,
    ): TsTypeRef.Literal {
      val tsPrimitive = when (kind) {
        PrimitiveKind.BOOLEAN -> TsLiteral.Primitive.TsBoolean

        PrimitiveKind.BYTE,
        PrimitiveKind.SHORT,
        PrimitiveKind.INT,
        PrimitiveKind.LONG,
        PrimitiveKind.FLOAT,
        PrimitiveKind.DOUBLE  -> TsLiteral.Primitive.TsNumber

        PrimitiveKind.CHAR,
        PrimitiveKind.STRING  -> TsLiteral.Primitive.TsString
      }
      return TsTypeRef.Literal(tsPrimitive, descriptor.isNullable)
    }


    fun mapTypeRef(descriptor: SerialDescriptor): TsTypeRef.Literal {
      val (keyDescriptor, valueDescriptor) = descriptor.elementDescriptors.toList()
      val keyTypeRef = this(keyDescriptor)
      val valueTypeRef = this(valueDescriptor)
      val type = mapTypeConverter(keyDescriptor, valueDescriptor)
      val map = TsLiteral.TsMap(keyTypeRef, valueTypeRef, type)
      return TsTypeRef.Literal(map, descriptor.isNullable)
    }


    fun listTypeRef(descriptor: SerialDescriptor): TsTypeRef.Literal {
      val elementDescriptor = descriptor.elementDescriptors.first()
      val elementTypeRef = this(elementDescriptor)
      val listRef = TsLiteral.TsList(elementTypeRef)
      return TsTypeRef.Literal(listRef, descriptor.isNullable)
    }


    fun declarationTypeRef(
      descriptor: SerialDescriptor
    ): TsTypeRef.Declaration {
      val id = elementIdConverter(descriptor)
      return TsTypeRef.Declaration(id, null, descriptor.isNullable)
    }
  }

}
