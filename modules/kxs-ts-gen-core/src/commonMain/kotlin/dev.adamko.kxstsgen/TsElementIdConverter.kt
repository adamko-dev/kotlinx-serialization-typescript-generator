package dev.adamko.kxstsgen

import kotlinx.serialization.descriptors.SerialDescriptor


fun interface TsElementIdConverter {

  operator fun invoke(descriptor: SerialDescriptor): TsElementId

  object Default : TsElementIdConverter {
    override operator fun invoke(descriptor: SerialDescriptor): TsElementId {

      val serialName = descriptor.serialName.removeSuffix("?")

      val namespace = serialName.substringBeforeLast('.')

      val id = serialName
        .substringAfterLast('.')
        .substringAfter("<")
        .substringBeforeLast(">")

      return when {
        namespace.isBlank() -> TsElementId("$id")
        else                -> TsElementId("$namespace.$id")
      }
    }
  }
}
