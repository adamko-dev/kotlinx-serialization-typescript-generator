<!--- TEST_NAME MapsTests -->

<!--- INCLUDE .*\.kt
import kotlinx.serialization.*
import dev.adamko.kxstsgen.*
-->

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

> You can get the full code [here](./knit/example/example-map-primitive-02.kt).

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
  colourNames: { [key: Colour]: string };
}
```

<!--- TEST -->
