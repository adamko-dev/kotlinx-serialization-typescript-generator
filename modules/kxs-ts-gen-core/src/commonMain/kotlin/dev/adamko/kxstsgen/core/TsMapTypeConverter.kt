package dev.adamko.kxstsgen.core

import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.elementDescriptors


fun interface TsMapTypeConverter {

  operator fun invoke(
    keyDescriptor: SerialDescriptor,
    valDescriptor: SerialDescriptor,
  ): TsLiteral.TsMap.Type

  object Default : TsMapTypeConverter {

    override operator fun invoke(
      keyDescriptor: SerialDescriptor,
      valDescriptor: SerialDescriptor,
    ): TsLiteral.TsMap.Type {
      return when {
        keyDescriptor.isNullable -> TsLiteral.TsMap.Type.MAP
        keyDescriptor.isInline   -> extractInlineType(keyDescriptor)
        else                     -> serialKindMapType(keyDescriptor.kind)
      }
    }

    /** Determine a map type based on [kind] */
    fun serialKindMapType(
      kind: SerialKind,
    ): TsLiteral.TsMap.Type {
      return when (kind) {
        SerialKind.ENUM      -> TsLiteral.TsMap.Type.MAPPED_OBJECT

        PrimitiveKind.STRING -> TsLiteral.TsMap.Type.INDEX_SIGNATURE

        SerialKind.CONTEXTUAL,
        PrimitiveKind.BOOLEAN,
        PrimitiveKind.BYTE,
        PrimitiveKind.CHAR,
        PrimitiveKind.SHORT,
        PrimitiveKind.INT,
        PrimitiveKind.LONG,
        PrimitiveKind.FLOAT,
        PrimitiveKind.DOUBLE,
        StructureKind.CLASS,
        StructureKind.LIST,
        StructureKind.MAP,
        StructureKind.OBJECT,
        PolymorphicKind.SEALED,
        PolymorphicKind.OPEN -> TsLiteral.TsMap.Type.MAP
      }
    }


    tailrec fun extractInlineType(keyDescriptor: SerialDescriptor): TsLiteral.TsMap.Type {
      return when {
        !keyDescriptor.isInline
          || keyDescriptor.elementsCount == 0 -> serialKindMapType(keyDescriptor.kind)
        else                                  -> {
          val inlineKeyDescriptor = keyDescriptor.elementDescriptors.first()
          extractInlineType(inlineKeyDescriptor)
        }
      }
    }
  }
}
