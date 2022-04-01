package dev.adamko.kxstsgen


import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.elementDescriptors


fun interface TsTypeRefConverter {

  operator fun invoke(context: KxsTsConvertorContext, descriptor: SerialDescriptor): TsTypeRef

  object Default : TsTypeRefConverter {
    override fun invoke(
      context: KxsTsConvertorContext,
      descriptor: SerialDescriptor,
    ): TsTypeRef {
      return when (descriptor.kind) {
        is PrimitiveKind     -> {
          val tsPrimitive = when (descriptor.kind as PrimitiveKind) {
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
          TsTypeRef.Literal(tsPrimitive, descriptor.isNullable)
        }

        StructureKind.LIST   -> {
          val elementDescriptor = descriptor.elementDescriptors.first()
          val elementTypeRef = context.typeRef(elementDescriptor)
          val listRef = TsLiteral.TsList(elementTypeRef)
          TsTypeRef.Literal(listRef, descriptor.isNullable)
        }
        StructureKind.MAP    -> {
          val (keyDescriptor, valueDescriptor) = descriptor.elementDescriptors.toList()
          val keyTypeRef = context.typeRef(keyDescriptor)
          val valueTypeRef = context.typeRef(valueDescriptor)
          val type = context.mapType(keyDescriptor)
          val map = TsLiteral.TsMap(keyTypeRef, valueTypeRef, type)
          TsTypeRef.Literal(map, descriptor.isNullable)
        }

        SerialKind.CONTEXTUAL,
        PolymorphicKind.SEALED,
        PolymorphicKind.OPEN,
        SerialKind.ENUM,
        StructureKind.CLASS,
        StructureKind.OBJECT -> {
          val id = context.elementId(descriptor)
          TsTypeRef.Named(id, descriptor.isNullable)
        }
      }
    }
  }
}
