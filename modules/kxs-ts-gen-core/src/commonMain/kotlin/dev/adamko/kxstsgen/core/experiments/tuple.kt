@file:OptIn(InternalSerializationApi::class)

package dev.adamko.kxstsgen.core.experiments

import kotlin.reflect.KProperty1
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeCollection
import kotlinx.serialization.serializer


data class TupleElement<T, E>(
  val name: String,
  val index: Int,
  val elementSerializer: KSerializer<E>,
  val elementAccessor: T.() -> E,
) {
  internal val descriptor: SerialDescriptor
    get() = elementSerializer.descriptor

  fun encodeElement(encoder: CompositeEncoder, value: T) {
    encoder.encodeSerializableElement(
      descriptor,
      index,
      elementSerializer,
      value.elementAccessor(),
    )
  }

  fun decodeElement(decoder: CompositeDecoder): E {
    return decoder.decodeSerializableElement(
      descriptor,
      index,
      elementSerializer,
    )
  }
}


inline fun <T, reified E> tupleElement(
  index: Int,
  name: String,
  noinline elementAccessor: T.() -> E,
  serializer: KSerializer<E> = serializer(),
): TupleElement<T, E> {
  return TupleElement(
    name,
    index,
    serializer,
    elementAccessor,
  )
}


fun <T> tupleElements(
  builder: TupleElementsBuilder<T>.() -> Unit
): List<TupleElement<T, *>> {
  val tupleElementsBuilder = TupleElementsBuilder<T>()
  tupleElementsBuilder.builder()
  return tupleElementsBuilder.elements
}


class TupleElementsBuilder<T> {

  private val _elements: ArrayDeque<TupleElement<T, *>> = ArrayDeque()
  val elements: List<TupleElement<T, *>>
    get() = _elements.toList()

  @PublishedApi
  internal val elementsSize by _elements::size

  inline fun <reified E> element(
    property: KProperty1<T, E>
  ) {
    element(property.name, property)
  }

  inline fun <reified E> element(
    name: String,
    noinline elementAccessor: T.() -> E,
  ) {
    element(tupleElement(elementsSize, name, elementAccessor))
  }

  fun element(element: TupleElement<T, *>) {
    _elements.addLast(element)
  }
}


abstract class TupleSerializer<T>(
  serialName: String,
  buildElements: TupleElementsBuilder<T>.() -> Unit
) : KSerializer<T> {

  val tupleElements: List<TupleElement<T, *>> = run {
    val tupleElementsBuilder = TupleElementsBuilder<T>()
    tupleElementsBuilder.buildElements()
    tupleElementsBuilder.elements.sortedBy { it.index }
  }
  private val indexedTupleElements = tupleElements.associateBy { it.index }

  abstract fun tupleConstructor(elements: Iterator<*>): T

  override val descriptor: SerialDescriptor = buildSerialDescriptor(
    serialName = serialName,
    kind = StructureKind.LIST,
    typeParameters = emptyArray(),
  ) {
    tupleElements
      .sortedBy { it.index }
      .forEach { tupleElement ->
        element(tupleElement.name, tupleElement.descriptor)
      }
  }

  override fun serialize(encoder: Encoder, value: T) {
    encoder.encodeCollection(descriptor, tupleElements.size) {
      tupleElements.forEach { tupleElement ->
        tupleElement.encodeElement(this, value)
      }
    }
  }

  override fun deserialize(decoder: Decoder): T = decoder.decodeStructure(descriptor) {

    // the collection size isn't required here, but we need to decode it to get it out of the way
    decodeCollectionSize(descriptor)

    val elements = if (decodeSequentially()) {
      tupleElements.asSequence().map {
        it.decodeElement(this@decodeStructure)
      }
    } else {
      generateSequence { decodeElementIndex(descriptor) }
        .takeWhile { index ->
          when (index) {
            CompositeDecoder.UNKNOWN_NAME         -> error("unknown name at index:$index")
            CompositeDecoder.DECODE_DONE          -> false
            !in indexedTupleElements.keys.indices -> error("unexpected index:$index")
            else                                  -> true
          }
        }.map { index ->
          val tupleElement = indexedTupleElements.getOrElse(index) {
            error("no tuple element at index:$index")
          }
          tupleElement.decodeElement(this@decodeStructure)
        }
    }
    // elements sequence *must* be collected inside 'decodeStructure'
    tupleConstructor(elements.iterator())
  }
}
