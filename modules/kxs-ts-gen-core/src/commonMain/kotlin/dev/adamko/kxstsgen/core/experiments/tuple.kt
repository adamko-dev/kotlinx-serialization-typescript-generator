@file:OptIn(InternalSerializationApi::class)

package dev.adamko.kxstsgen.core.experiments

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


open class TupleElement<T, E>(
  open val index: Int,
  open val elementSerializer: KSerializer<E>,
  open val elementAccessor: T.() -> E,
) {
  val descriptor: SerialDescriptor
    get() = elementSerializer.descriptor

  open fun encodeElement(encoder: CompositeEncoder, value: T) {
    encoder.encodeSerializableElement(
      descriptor,
      index,
      elementSerializer,
      value.elementAccessor()
    )
  }

  open fun decodeElement(decoder: CompositeDecoder): E {
    return decoder.decodeSerializableElement(
      descriptor,
      index,
      elementSerializer,
    )
  }
}


inline fun <T, reified E> tupleElement(
  index: Int,
  noinline elementAccessor: T.() -> E,
  serializer: KSerializer<E> = serializer(),
): TupleElement<T, E> {
  return TupleElement(
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
  val elementsSize by _elements::size

  inline fun <reified E> element(
    noinline elementAccessor: T.() -> E,
  ) {
    element(tupleElement(elementsSize, elementAccessor))
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
        element("element${tupleElement.index}", tupleElement.descriptor)
      }
  }

  override fun serialize(encoder: Encoder, value: T) {
    encoder.encodeCollection(descriptor, tupleElements.size) {
      tupleElements.forEach { tupleElement ->
        tupleElement.encodeElement(this, value)
      }
    }
  }

  override fun deserialize(decoder: Decoder): T {
    val elements = decoder.decodeStructure(descriptor) {
      generateSequence {
        val index = decodeElementIndex(descriptor)
        indexedTupleElements[index]?.decodeElement(this)
      }.iterator()
    }
    return tupleConstructor(elements)
  }
}
