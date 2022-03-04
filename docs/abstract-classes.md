<!--- TEST_NAME AbstractClassesTest -->

<!--- INCLUDE .*\.kt
import kotlinx.serialization.*
import dev.adamko.kxstsgen.*
-->

### Abstract class with a single field

```kotlin
@Serializable
abstract class Color(val rgb: Int)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Color.serializer().descriptor))
}
```

> You can get the full code [here](./knit/example/example-abstract-class-single-field-01.kt).

```typescript
interface Color {
  rgb: number;
}
```

<!--- TEST -->

### Abstract class with primitive fields

```kotlin
@Serializable
abstract class SimpleTypes(
  val aString: String,
  var anInt: Int,
  val aDouble: Double,
  val bool: Boolean,
  private val privateMember: String,
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(SimpleTypes.serializer().descriptor))
}
```

> You can get the full code [here](./knit/example/example-abstract-class-primitive-fields-01.kt).

```typescript
interface SimpleTypes {
  aString: string;
  anInt: number;
  aDouble: number;
  bool: boolean;
  privateMember: string;
}
```

<!--- TEST -->

### Abstract class, abstract value

```kotlin
@Serializable
abstract class Color {
  abstract val rgb: Int
}

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Color.serializer().descriptor))
}
```

> You can get the full code [here](./knit/example/example-abstract-class-abstract-field-01.kt).

```typescript
interface Color {
  rgb: number;
}
```

<!--- TEST -->
