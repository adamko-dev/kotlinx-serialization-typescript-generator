package dev.adamko.kxstsgen.core.experiments

import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TupleTest : FunSpec({

  context("Coordinates") {
    test("json round trip") {
      checkAll(Arb.int(), Arb.int()) { x, y ->
        val initial = Coordinates(x, y)

        val encoded = Json.encodeToString(initial)

        val decoded = Json.decodeFromString<Coordinates>(encoded)

        withClue(
          """
            initial: $initial
            decoded: $decoded
          """.trimIndent()
        ) {
          initial.x shouldBeExactly decoded.x
          initial.y shouldBeExactly decoded.y
          initial shouldNotBeSameInstanceAs decoded
        }
      }
    }
  }
}) {


  @Serializable(with = Coordinates.Serializer::class)
  data class Coordinates(
    val x: Int,
    val y: Int,
  ) {
    object Serializer : TupleSerializer<Coordinates>(
      "Coordinates",
      {
        element(Coordinates::x)
        element(Coordinates::y)
      }
    ) {
      override fun tupleConstructor(elements: Iterator<Any?>): Coordinates {
        val x = requireNotNull(elements.next() as? Int)
        val y = requireNotNull(elements.next() as? Int)
        return Coordinates(x, y)
      }
    }
  }
}
