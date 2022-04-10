<!--- TEST_NAME MapsTests -->

**Table of contents**

<!--- TOC -->

* [Introduction](#introduction)
  * [Primitive maps](#primitive-maps)
  * [Enum keys](#enum-keys)
  * [Maps with Collections](#maps-with-collections)
  * [Maps with value classes](#maps-with-value-classes)
  * [Nullable keys and values](#nullable-keys-and-values)
  * [Type alias keys](#type-alias-keys)
  * [Maps with complex keys](#maps-with-complex-keys)
    * [ES6 Map](#es6-map)
    * [Maps with complex keys - Map Key class](#maps-with-complex-keys---map-key-class)
    * [Maps with complex keys - custom serializer workaround](#maps-with-complex-keys---custom-serializer-workaround)

<!--- END -->


<!--- INCLUDE .*\.kt
import kotlinx.serialization.*
import dev.adamko.kxstsgen.*
-->

## Introduction

### Primitive maps

```kotlin
@Serializable
data class Config(
  val properties: Map<String, String>
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Config.serializer()))
}
```

> You can get the full code [here](./code/example/example-map-primitive-01.kt).

```typescript
export interface Config {
  properties: { [key: string]: string };
}
```

<!--- TEST -->

### Enum keys

```kotlin
@Serializable
class Application(
  val settings: Map<SettingKeys, String>
)

@Serializable
enum class SettingKeys {
  SCREEN_SIZE,
  MAX_MEMORY,
}

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Application.serializer()))
}
```

> You can get the full code [here](./code/example/example-map-primitive-02.kt).

```typescript
export interface Application {
  settings: { [key in SettingKeys]: string };
}

export enum SettingKeys {
  SCREEN_SIZE = "SCREEN_SIZE",
  MAX_MEMORY = "MAX_MEMORY",
}
```

<!--- TEST -->

### Maps with Collections

```kotlin
@Serializable
class MapsWithLists(
  val mapOfLists: Map<String, List<String>>
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(MapsWithLists.serializer()))
}
```

> You can get the full code [here](./code/example/example-map-primitive-03.kt).

```typescript
export interface MapsWithLists {
  mapOfLists: { [key: string]: string[] };
}
```

<!--- TEST -->

### Maps with value classes

```kotlin
@Serializable
@JvmInline
value class Data(val content: String)

@Serializable
class MyDataClass(
  val mapOfLists: Map<String, Data>
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(MyDataClass.serializer()))
}
```

> You can get the full code [here](./code/example/example-map-primitive-04.kt).

```typescript
export interface MyDataClass {
  mapOfLists: { [key: string]: Data };
}

export type Data = string;
```

<!--- TEST -->

### Nullable keys and values

Nullable keys are not allowed, so are convert to an ES6 Map.

> An index signature parameter type must be 'string', 'number', 'symbol', or a template literal type

```kotlin
@Serializable
data class Config(
  val nullableVals: Map<String, String?>,
  val nullableKeys: Map<String?, String>,
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Config.serializer()))
}
```

> You can get the full code [here](./code/example/example-map-primitive-05.kt).

```typescript
export interface Config {
  nullableVals: { [key: string]: string | null };
  // [key: string | null] is not allowed
  nullableKeys: Map<string | null, string>;
}
```

<!--- TEST -->

### Type alias keys

Type aliased keys should still use an indexed type, if the type alias is suitable.

```kotlin
@Serializable
data class ComplexKey(val complex: String)

@Serializable
@JvmInline
value class SimpleKey(val simple: String)

@Serializable
@JvmInline
value class DoubleSimpleKey(val simple: SimpleKey)

@Serializable
data class Example(
  val complex: Map<ComplexKey, String>,
  val simple: Map<SimpleKey, String>,
  val doubleSimple: Map<DoubleSimpleKey, String>,
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Example.serializer()))
}
```

> You can get the full code [here](./code/example/example-map-primitive-06.kt).

```typescript
export interface Example {
  complex: Map<ComplexKey, string>;
  simple: { [key: SimpleKey]: string };
  doubleSimple: { [key: DoubleSimpleKey]: string };
}

export interface ComplexKey {
  complex: string;
}

export type SimpleKey = string;

export type DoubleSimpleKey = SimpleKey;
```

<!--- TEST -->

### Maps with complex keys

JSON maps **must** have keys that are either strings, positive integers, or enums.

See [the Kotlinx Serialization docs](https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/json.md#allowing-structured-map-keys)
.

As a workaround, maps with structured keys are generated as [ES6 maps](#es6-map).

To produce correct JSON,
either [write a custom serializer](#maps-with-complex-keys---custom-serializer-workaround)
or [use an explicit map-key class](#maps-with-complex-keys---map-key-class).

#### ES6 Map

This is the default behaviour of KxsTsGen when it encounters complex map keys.

KxsTsGen produces valid TypeScript, but the TypeScript might not produce correct JSON.

```kotlin
@Serializable
data class Colour(
  val r: UByte,
  val g: UByte,
  val b: UByte,
  val a: UByte,
)

@Serializable
data class CanvasProperties(
  val colourNames: Map<Colour, String>
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(CanvasProperties.serializer()))
}
```

> You can get the full code [here](./code/example/example-map-complex-01.kt).

```typescript
export interface CanvasProperties {
  colourNames: Map<Colour, string>;
}

export interface Colour {
  r: UByte;
  g: UByte;
  b: UByte;
  a: UByte;
}

export type UByte = number;
```

<!--- TEST -->

#### Maps with complex keys - Map Key class

This approach is less optimised, but more declarative and easier to understand than writing custom
serializers.

Because the value class `ColourMapKey` has a single string value, the descriptor is a
`PrimitiveKind.STRING`.

KxsTsGen will generate a JSON-safe mapped-type property.

```kotlin
@Serializable
data class Colour(
  val r: UByte,
  val g: UByte,
  val b: UByte,
  val a: UByte,
)

/**
 * Encode a [Colour] as an 8-character string
 *
 * Red, green, blue, and alpha are encoded as base-16 strings.
 */
@Serializable
@JvmInline
value class ColourMapKey(private val rgba: String) {
  constructor(colour: Colour) : this(
    listOf(
      colour.r,
      colour.g,
      colour.b,
      colour.a,
    ).joinToString("") {
      it.toString(16).padStart(2, '0')
    }
  )

  fun toColour(): Colour {
    val (r, g, b, a) = rgba.chunked(2).map { it.toUByte(16) }
    return Colour(r, g, b, a)
  }
}

@Serializable
data class CanvasProperties(
  val colourNames: Map<ColourMapKey, String>
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(CanvasProperties.serializer()))
}
```

> You can get the full code [here](./code/example/example-map-complex-02.kt).

Because the map now has a non-complex key, an 'indexed type' is generated.

```typescript
export interface CanvasProperties {
  colourNames: { [key: ColourMapKey]: string };
}

export type ColourMapKey = string;
```

<!--- TEST -->

#### Maps with complex keys - custom serializer workaround

Define a custom serializer for `Colour` that will encode and decode to/from a string.

When encoding or decoding values with Kotlinx Serialization, under the hood it will create suitable
map keys.

Because the custom serializer is a `PrimitiveKind.STRING`, KxsTsGen will generate a JSON-safe
mapped-type property.

```kotlin
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable(with = ColourAsStringSerializer::class)
data class Colour(
  val r: UByte,
  val g: UByte,
  val b: UByte,
  val a: UByte,
)

/**
 * Encode a [Colour] as an 8-character string
 *
 * Red, green, blue, and alpha are encoded as base-16 strings.
 */
object ColourAsStringSerializer : KSerializer<Colour> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("Colour", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, value: Colour) {
    encoder.encodeString(
      listOf(
        value.r,
        value.g,
        value.b,
        value.a,
      ).joinToString("") {
        it.toString(16).padStart(2, '0')
      }
    )
  }

  override fun deserialize(decoder: Decoder): Colour {
    val string = decoder.decodeString()
    val (r, g, b, a) = string.chunked(2).map { it.toUByte(16) }
    return Colour(r, g, b, a)
  }
}

@Serializable
data class CanvasProperties(
  val colourNames: Map<Colour, String>
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(CanvasProperties.serializer()))
}
```

> You can get the full code [here](./code/example/example-map-complex-03.kt).

```typescript
export interface CanvasProperties {
  colourNames: { [key: string]: string };
}
```

<!--- TEST -->
