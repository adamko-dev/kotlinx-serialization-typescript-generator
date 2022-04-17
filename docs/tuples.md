<!--- TEST_NAME TuplesTest -->


**Table of contents**

<!--- TOC -->

* [Tuples](#tuples)
  * [Tuple example](#tuple-example)
  * [Tuple labels](#tuple-labels)
  * [Optional elements in tuples](#optional-elements-in-tuples)
  * [Properties all the same type](#properties-all-the-same-type)
  * [Tuples as interface properties](#tuples-as-interface-properties)

<!--- END -->


<!--- INCLUDE .*\.kt
import dev.adamko.kxstsgen.*
import dev.adamko.kxstsgen.core.experiments.TupleSerializer
import kotlinx.serialization.*
-->

## Tuples

In TypeScript, tuples are a compact format for data structures. They're like fixed-length arrays
that only contain the type. This is especially useful when using JSON, as including property names
means the messages are much larger.

Tuples are a bit difficult to create in Kotlinx Serialization, but KxTsGen includes
[TupleSerializer](../modules/kxs-ts-gen-core/src/commonMain/kotlin/dev/adamko/kxstsgen/core/experiments/tuple.kt)
which can help. It requires a name, an ordered list of elements, and a constructor for
deserializing.

### Tuple example

Let's say we have a class, `SimpleTypes`, that we want to serialize. We need to create a bespoke
tuple serializer for it, and override the plugin-generated serializer.

```kotlin
@Serializable(with = SimpleTypes.SimpleTypesSerializer::class)
data class SimpleTypes(
  val aString: String,
  var anInt: Int,
  val aDouble: Double?,
  val bool: Boolean,
  private val privateMember: String,
) {
  // Create `SimpleTypesSerializer` inside `SimpleTypes`, so it
  // has access to the private property `privateMember`.
  object SimpleTypesSerializer : TupleSerializer<SimpleTypes>(
    "SimpleTypes",
    {
      // Provide all tuple elements, in order, using the 'elements' helper method.
      element(SimpleTypes::aString)
      element(SimpleTypes::anInt)
      element(SimpleTypes::aDouble)
      element(SimpleTypes::bool)
      element(SimpleTypes::privateMember)
    }
  ) {
    override fun tupleConstructor(elements: Iterator<*>): SimpleTypes {
      // When deserializing, the elements will be available as a list, in the order defined above
      return SimpleTypes(
        elements.next() as String,
        elements.next() as Int,
        elements.next() as Double,
        elements.next() as Boolean,
        elements.next() as String,
      )
    }
  }
}

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(SimpleTypes.serializer()))
}
```

> You can get the full code [here](./code/example/example-tuple-01.kt).

```typescript
export type SimpleTypes = [
  aString: string,
  anInt: number,
  aDouble: number | null,
  bool: boolean,
  privateMember: string,
];
```

<!--- TEST -->

### Tuple labels

By default, the tuple elements are labelled with the names of properties, not the `@SerialName`,
which will be ignored. This isn't important for serialization because the tuple will be serialized
without the name of the property.

The name of the label can be overridden if desired while defining the elements.

```kotlin
@Serializable(with = PostalAddressUSA.Serializer::class)
data class PostalAddressUSA(
  @SerialName("num") // 'SerialName' will be ignored in 'Tuple' form
  val houseNumber: String,
  val streetName: String,
  val postcode: String,
) {
  object Serializer : TupleSerializer<PostalAddressUSA>(
    "PostalAddressUSA",
    {
      element(PostalAddressUSA::houseNumber)
      // custom labels for 'streetName', 'postcode'
      element("street", PostalAddressUSA::streetName)
      element("zip", PostalAddressUSA::postcode)
    }
  ) {
    override fun tupleConstructor(elements: Iterator<*>): PostalAddressUSA {
      return PostalAddressUSA(
        elements.next() as String,
        elements.next() as String,
        elements.next() as String,
      )
    }
  }
}

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(PostalAddressUSA.serializer()))
}
```

> You can get the full code [here](./code/example/example-tuple-02.kt).

```typescript
export type PostalAddressUSA = [
  houseNumber: string, // @SerialName("num") was ignored
  street: string, // custom name
  zip: string, // custom name
];
```

<!--- TEST -->

### Optional elements in tuples

`TupleSerializer` does not consider whether a field is optional. This is intentional, partly because
it's quite complicated to set up already, and more options won't help! Also, tuples require that
optional elements must come at the end, which again would make defining tuples more complicated.

If optional tuple elements is important to you, please make an issue and explaining your usecase,
requirements, and potential solutions or outcomes.

```kotlin
@Serializable(with = OptionalFields.Serializer::class)
data class OptionalFields(
  val requiredString: String,
  val optionalString: String = "",
  val nullableString: String?,
  val nullableOptionalString: String? = "",
) {
  object Serializer : TupleSerializer<OptionalFields>(
    "OptionalFields",
    {
      element(OptionalFields::requiredString)
      element(OptionalFields::optionalString)
      element(OptionalFields::nullableString)
      element(OptionalFields::nullableOptionalString)
    }
  ) {
    override fun tupleConstructor(elements: Iterator<*>): OptionalFields {
      val iter = elements.iterator()
      return OptionalFields(
        iter.next() as String,
        iter.next() as String,
        iter.next() as String,
        iter.next() as String,
      )
    }
  }
}

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(OptionalFields.serializer()))
}
```

> You can get the full code [here](./code/example/example-tuple-03.kt).

```typescript
export type OptionalFields = [
  requiredString: string,
  optionalString: string,
  nullableString: string | null,
  nullableOptionalString: string | null,
];
```

<!--- TEST -->

### Properties all the same type

```kotlin
@Serializable(with = Coordinates.Serializer::class)
data class Coordinates(
  val x: Int,
  val y: Int,
  val z: Int,
) {
  object Serializer : TupleSerializer<Coordinates>(
    "Coordinates",
    {
      element(Coordinates::x)
      element(Coordinates::y)
      element(Coordinates::z)
    }
  ) {
    override fun tupleConstructor(elements: Iterator<*>): Coordinates {
      return Coordinates(
        elements.next() as Int,
        elements.next() as Int,
        elements.next() as Int,
      )
    }
  }
}

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Coordinates.serializer()))
}
```

> You can get the full code [here](./code/example/example-tuple-04.kt).

```typescript
export type Coordinates = [
  x: number,
  y: number,
  z: number,
];
```

<!--- TEST -->

### Tuples as interface properties

```kotlin
import dev.adamko.kxstsgen.example.exampleTuple04.Coordinates

@Serializable
class GameLocations(
  val homeLocation: Coordinates,
  val allLocations: List<Coordinates>,
  val namedLocations: Map<String, Coordinates>,
  val locationsInfo: Map<Coordinates, String>,
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(GameLocations.serializer()))
}
```

> You can get the full code [here](./code/example/example-tuple-05.kt).

```typescript
export interface GameLocations {
  homeLocation: Coordinates;
  allLocations: Coordinates[];
  namedLocations: { [key: string]: Coordinates };
  locationsInfo: Map<Coordinates, string>;
}

export type Coordinates = [
  x: number,
  y: number,
  z: number,
];
```

<!--- TEST -->
