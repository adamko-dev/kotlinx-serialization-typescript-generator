package dev.adamko.kxstsgen

import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.StructureKind


fun interface TsMapTypeConverter {

  operator fun invoke(descriptor: SerialDescriptor): TsLiteral.TsMap.Type

  object Default : TsMapTypeConverter {

    override operator fun invoke(descriptor: SerialDescriptor): TsLiteral.TsMap.Type {
      return when (descriptor.kind) {
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
  }
}
