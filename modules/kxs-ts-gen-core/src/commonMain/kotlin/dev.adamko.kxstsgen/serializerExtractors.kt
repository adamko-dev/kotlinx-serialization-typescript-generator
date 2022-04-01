@file:OptIn(InternalSerializationApi::class)

package dev.adamko.kxstsgen

import kotlinx.serialization.ContextualSerializer
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SealedClassSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.modules.SerializersModule


// https://github.com/Kotlin/kotlinx.serialization/issues/1865
expect fun <T : Any> extractSealedSubclassSerializers(
  serializer: SealedClassSerializer<T>
): Collection<KSerializer<out T>>


/** Hacky exploit to capture the [KSerializer] of a [ContextualSerializer]. */
fun extractContextualSerializer(
  serializer: ContextualSerializer<*>,
  kxsTsConfig: KxsTsConfig,
): KSerializer<*>? {
  return try {
    val decoder = ContextualSerializerCaptorDecoder(kxsTsConfig.serializersModule)
    serializer.deserialize(decoder)
    null // this should never be hit, decoder should always throw an exception
  } catch (e: SerializerCaptorException) {
    e.serializer
  } catch (e: Throwable) {
    null
  }
}

private class ContextualSerializerCaptorDecoder(
  override val serializersModule: SerializersModule
) : AbstractDecoder() {

  override fun decodeElementIndex(descriptor: SerialDescriptor): Nothing =
    error("intentionally unimplemented, I don't expect ContextualSerializer to call this method")

  override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): Nothing =
    throw SerializerCaptorException(deserializer as KSerializer<T>)
}


private class SerializerCaptorException(val serializer: KSerializer<*>) : Exception()


expect fun <T : Any> extractContextualDescriptor(
  serializer: ContextualSerializer<T>,
  serializersModule: SerializersModule,
): KSerializer<T>
