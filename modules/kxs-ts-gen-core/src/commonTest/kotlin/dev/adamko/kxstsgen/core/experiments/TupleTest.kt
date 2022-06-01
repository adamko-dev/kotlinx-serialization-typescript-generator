package dev.adamko.kxstsgen.core.experiments

import dev.adamko.kxstsgen.core.test.kxsBinary
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.longs.shouldBeExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.Codepoint
import io.kotest.property.arbitrary.az
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TupleTest : FunSpec({

  context("Coordinates") {
    test("json round trip") {
      checkAll(
        Arb.int(),
        Arb.long(),
        Arb.string(5, Codepoint.az()),
        Arb.boolean(),
      ) { x, y, d, b ->
        val initial = Coordinates(x, y, d, b)

        val encoded: String = Json.encodeToString(initial)

        val decoded: Coordinates = Json.decodeFromString(encoded)

        withClue(
          """
            initial: $initial
            decoded: $decoded
          """.trimIndent()
        ) {
          initial.x shouldBeExactly decoded.x
          initial.y shouldBeExactly decoded.y
          initial.data shouldBe d
          initial.active shouldBe b
          initial shouldNotBeSameInstanceAs decoded
        }
      }
    }

    test("Cbor round trip") {
      checkAll(
        Arb.int(),
        Arb.long(),
        Arb.string(5, Codepoint.az()),
        Arb.boolean(),
      ) { x, y, d, b ->
        val initial = Coordinates(x, y, d, b)

        val encoded: ByteArray = Cbor.encodeToByteArray(initial)
//        println("encoded: ${encoded.joinToString { it.toString().padStart(4, ' ') }}")

        val decoded: Coordinates = Cbor.decodeFromByteArray(encoded)

        withClue(
          """
            initial: $initial
            decoded: $decoded
          """.trimIndent()
        ) {
          initial.x shouldBeExactly decoded.x
          initial.y shouldBeExactly decoded.y
          initial.data shouldBe d
          initial.active shouldBe b
          initial shouldNotBeSameInstanceAs decoded
        }
      }
    }

    test("kxsBinary round trip") {
      checkAll(
        Arb.int(-50..50),
        Arb.long(-50L..50L),
        Arb.string(5, Codepoint.az()),
        Arb.boolean(),
      ) { x, y, d, b ->
        val initial = Coordinates(x, y, d, b)

        val encoded: ByteArray = kxsBinary.encodeToByteArray(initial)

        val decoded: Coordinates = kxsBinary.decodeFromByteArray(encoded)

        withClue(
          """
            initial: $initial
            decoded: $decoded
          """.trimIndent()
        ) {
          initial.x shouldBeExactly decoded.x
          initial.y shouldBeExactly decoded.y
          initial.data shouldBe d
          initial.active shouldBe b
          initial shouldNotBeSameInstanceAs decoded
        }
      }
    }
  }
}) {


  @Serializable(with = Coordinates.Serializer::class)
  data class Coordinates(
    val x: Int,
    val y: Long,
    val data: String,
    val active: Boolean
  ) {
    object Serializer : TupleSerializer<Coordinates>(
      "Coordinates",
      {
        element(Coordinates::x)
        element(Coordinates::y)
        element(Coordinates::data)
        element(Coordinates::active)
      }
    ) {
      override fun tupleConstructor(elements: Iterator<Any?>): Coordinates {
        val x = requireNotNull(elements.next() as? Int)
        val y = requireNotNull(elements.next() as? Long)
        val data = requireNotNull(elements.next() as? String)
        val active = requireNotNull(elements.next() as? Boolean)
        return Coordinates(x, y, data, active)
      }
    }
  }
}
