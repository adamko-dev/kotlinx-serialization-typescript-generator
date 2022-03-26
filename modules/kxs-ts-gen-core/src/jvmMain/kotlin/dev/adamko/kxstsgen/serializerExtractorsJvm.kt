@file:OptIn(InternalSerializationApi::class) // TODO make GitHub issue
package dev.adamko.kxstsgen

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.functions
import kotlin.reflect.jvm.isAccessible
import kotlinx.serialization.ContextualSerializer
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SealedClassSerializer
import kotlinx.serialization.modules.SerializersModule


actual fun <T : Any> extractSealedSubclassSerializers(
  serializer: SealedClassSerializer<T>
): Collection<KSerializer<out T>> {
  @Suppress("UNCHECKED_CAST")
  val class2Serializer = class2SerializerAccessor
    .get(serializer) as Map<KClass<out T>, KSerializer<out T>>
  return class2Serializer.values
}


/** Access the private `class2Serializer` field in [SealedClassSerializer] */
private val class2SerializerAccessor: KProperty1<SealedClassSerializer<*>, *> =
  SealedClassSerializer::class
    .declaredMemberProperties
    .firstOrNull { it.name == "class2Serializer" }
    ?.apply { isAccessible = true }
    ?: error("Can't access ContextualSerializer.serializer()")


@Suppress("UNCHECKED_CAST")
actual fun <T : Any> extractContextualDescriptor(
  serializer: ContextualSerializer<T>,
  serializersModule: SerializersModule,
): KSerializer<T> {
  return contextualSerializerAccessor(serializersModule) as KSerializer<T>
}


/** Access the private `.serializer()` function in [ContextualSerializer] */
@Suppress("UNCHECKED_CAST")
private val contextualSerializerAccessor: (SerializersModule) -> KSerializer<*> =
  ContextualSerializer::class
    .functions
    .firstOrNull { it.name == "serializer" }
    ?.apply { isAccessible = true }
    as? (SerializersModule) -> KSerializer<*>
    ?: error("Can't access ContextualSerializer.serializer()")
