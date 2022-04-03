package dev.adamko.kxstsgen

import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.StructureKind


fun interface TsElementIdConverter {

  operator fun invoke(descriptor: SerialDescriptor): TsElementId

  object Default : TsElementIdConverter {
    override operator fun invoke(descriptor: SerialDescriptor): TsElementId {
      val targetId = TsElementId(descriptor.serialName.removeSuffix("?"))

      return when (descriptor.kind) {
        PolymorphicKind.OPEN -> TsElementId(
          targetId.namespace + "." + targetId.name.substringAfter("<").substringBeforeLast(">")
        )
        PolymorphicKind.SEALED,
        PrimitiveKind.BOOLEAN,
        PrimitiveKind.BYTE,
        PrimitiveKind.CHAR,
        PrimitiveKind.DOUBLE,
        PrimitiveKind.FLOAT,
        PrimitiveKind.INT,
        PrimitiveKind.LONG,
        PrimitiveKind.SHORT,
        PrimitiveKind.STRING,
        SerialKind.CONTEXTUAL,
        SerialKind.ENUM,
        StructureKind.CLASS,
        StructureKind.LIST,
        StructureKind.MAP,
        StructureKind.OBJECT -> targetId
      }
    }

  }
}
