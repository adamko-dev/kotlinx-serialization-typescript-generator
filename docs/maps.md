<!--- TEST_NAME MapsTests -->

**Table of contents**

<!--- TOC -->

* [Introduction](#introduction)
  * [Primitive maps](#primitive-maps)
  * [Enum keys](#enum-keys)
  * [Nullable keys and values](#nullable-keys-and-values)
  * [Maps with complex keys](#maps-with-complex-keys)

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
  println(tsGenerator.generate(Config.serializer().descriptor))
}
```

> You can get the full code [here](./knit/example/example-map-primitive-01.kt).

```typescript
interface Config {
  properties: { [key: string]: string };
}
```

<!--- TEST -->

### Enum keys

```kotlin
@Serializable
enum class SettingKeys {
  SCREEN_SIZE,
  MAX_MEMORY,
}

@Serializable
class Application(
  val settings: Map<SettingKeys, String>
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Application.serializer().descriptor))
}
```

> You can get the full code [here](./knit/example/example-map-primitive-02.kt).

```typescript
enum SettingKeys {
  SCREEN_SIZE = "SCREEN_SIZE",
  MAX_MEMORY = "MAX_MEMORY",
}

interface Application {
  settings: { [key in SettingKeys]: string };
}
```

<!--- TEST -->

### Nullable keys and values

```kotlin
@Serializable
data class Config(
  val properties: Map<String?, String?>
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Config.serializer().descriptor))
}
```

> You can get the full code [here](./knit/example/example-map-primitive-03.kt).

```typescript
interface Config {
  properties: { [key: string | null]: string | null };
}
```

<!--- TEST -->

### Maps with complex keys

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
  println(tsGenerator.generate(CanvasProperties.serializer().descriptor))
}
```

> You can get the full code [here](./knit/example/example-map-complex-01.kt).

```typescript
type UByte = number;

interface Colour {
  r: UByte;
  g: UByte;
  b: UByte;
  a: UByte;
}

interface CanvasProperties {
  colourNames: Map<Colour, string>;
}
```

<!--- TEST -->
