package dev.adamko.kxstsgen.core.test

import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import okio.Buffer
import okio.BufferedSink
import okio.BufferedSource


val kxsBinary: KxsBinary = KxsBinary()

/** [`https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/formats.md#efficient-binary-format`](https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/formats.md#efficient-binary-format) */
class KxsBinary(
  override val serializersModule: SerializersModule = EmptySerializersModule
) : BinaryFormat {

  override fun <T> encodeToByteArray(serializer: SerializationStrategy<T>, value: T): ByteArray {
    val output = Buffer()
    val encoder = KxsDataOutputEncoder(output, serializersModule)
    encoder.encodeSerializableValue(serializer, value)
    return output.readByteArray()
  }

  override fun <T> decodeFromByteArray(
    deserializer: DeserializationStrategy<T>,
    bytes: ByteArray
  ): T {
    val input = Buffer().write(bytes)
    val decoder = KxsDataInputDecoder(input, serializersModule)
    return decoder.decodeSerializableValue(deserializer)
  }
}

private val byteArraySerializer = ByteArraySerializer()

class KxsDataOutputEncoder(
  private val output: BufferedSink,
  override val serializersModule: SerializersModule = EmptySerializersModule,
) : AbstractEncoder() {

  private operator fun BufferedSink.invoke(action: BufferedSink.() -> Unit): Unit = action()

  override fun encodeBoolean(value: Boolean) = output { writeByte(if (value) 1 else 0) }
  override fun encodeByte(value: Byte) = output { writeByte(value.toInt()) }
  override fun encodeChar(value: Char) = output { writeUtf8CodePoint(value.code) }
  override fun encodeDouble(value: Double) = output { writeLong(value.toRawBits()) }
  override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) = output { writeInt(index) }
  override fun encodeFloat(value: Float) = output { writeInt(value.toRawBits()) }
  override fun encodeInt(value: Int) = output { writeInt(value) }
  override fun encodeLong(value: Long) = output { writeLong(value) }
  override fun encodeShort(value: Short) = output { writeShort(value.toInt()) }
  override fun encodeString(value: String) = output {
    encodeCompactSize(value.length)
    writeUtf8(value)
  }

  override fun beginCollection(
    descriptor: SerialDescriptor,
    collectionSize: Int
  ): CompositeEncoder {
    encodeCompactSize(collectionSize)
    return this
  }

  override fun encodeNull() = encodeBoolean(false)
  override fun encodeNotNullMark() = encodeBoolean(true)

  override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T) {
    when (serializer.descriptor) {
      byteArraySerializer.descriptor -> encodeByteArray(value as ByteArray)
      else                           -> super.encodeSerializableValue(serializer, value)
    }
  }

  private fun encodeByteArray(bytes: ByteArray) {
    encodeCompactSize(bytes.size)
    output.write(bytes)
  }

  private fun encodeCompactSize(value: Int) {
    if (value < 0xff) {
      output.writeByte(value)
    } else {
      output.writeByte(0xff)
      output.writeInt(value)
    }
  }
}


class KxsDataInputDecoder(
  private val input: BufferedSource,
  override val serializersModule: SerializersModule = EmptySerializersModule,
  private var elementsCount: Int = 0,
) : AbstractDecoder() {

  private var elementIndex = 0

  override fun decodeBoolean(): Boolean = input.readByte().toInt() != 0
  override fun decodeByte(): Byte = input.readByte()
  override fun decodeChar(): Char = input.readUtf8CodePoint().toChar()
  override fun decodeDouble(): Double = Double.fromBits(input.readLong())
  override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = input.readInt()
  override fun decodeFloat(): Float = Float.fromBits(input.readInt())
  override fun decodeInt(): Int = input.readInt()
  override fun decodeLong(): Long = input.readLong()
  override fun decodeShort(): Short = input.readShort()
  override fun decodeString(): String {
    val size = decodeCompactSize()
    return input.readUtf8(size.toLong())
  }

  override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
    if (elementIndex == elementsCount) return CompositeDecoder.DECODE_DONE
    return elementIndex++
  }

  override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder =
    KxsDataInputDecoder(input, serializersModule, descriptor.elementsCount)

  override fun decodeSequentially(): Boolean = true

  override fun decodeCollectionSize(descriptor: SerialDescriptor): Int =
    decodeCompactSize().also { elementsCount = it }

  override fun decodeNotNullMark(): Boolean = decodeBoolean()

  override fun <T> decodeSerializableValue(
    deserializer: DeserializationStrategy<T>,
    previousValue: T?
  ): T {
    @Suppress("UNCHECKED_CAST")
    return when (deserializer.descriptor) {
      byteArraySerializer.descriptor -> decodeByteArray() as T
      else                           -> super.decodeSerializableValue(deserializer, previousValue)
    }
  }

  private fun decodeByteArray(): ByteArray {
    val bytes = ByteArray(decodeCompactSize())
    input.readFully(bytes)
    return bytes
  }

  private fun decodeCompactSize(): Int {
    val byte = input.readByte().toInt() and 0xff
    if (byte < 0xff) return byte
    return input.readInt()
  }

}
