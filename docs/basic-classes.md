<!--- TEST_NAME BasicClassesTest -->

**Table of contents**

<!--- TOC -->

* [Basic classes](#basic-classes)
  * [Plain class with a single field](#plain-class-with-a-single-field)
  * [Plain class with primitive fields](#plain-class-with-primitive-fields)
  * [Data class with primitive fields](#data-class-with-primitive-fields)

<!--- END -->

## Basic classes

### Plain class with a single field

<!--- INCLUDE .*\.kt
import kotlinx.serialization.*
import dev.adamko.kxstsgen.*
-->

```kotlin
@Serializable
class Color(val rgb: Int)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Color.serializer()))
}
```

> You can get the full code [here](./code/example/example-plain-class-single-field-01.kt).

```typescript
export interface Color {
  rgb: number;
}
```

<!--- TEST -->

### Plain class with primitive fields

```kotlin
@Serializable
class SimpleTypes(
  val aString: String,
  var anInt: Int,
  val aDouble: Double,
  val bool: Boolean,
  private val privateMember: String,
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(SimpleTypes.serializer()))
}
```

> You can get the full code [here](./code/example/example-plain-class-primitive-fields-01.kt).

```typescript
export interface SimpleTypes {
  aString: string;
  anInt: number;
  aDouble: number;
  bool: boolean;
  privateMember: string;
}
```

<!--- TEST -->

### Data class with primitive fields

```kotlin
@Serializable
data class SomeDataClass(
  val aString: String,
  var anInt: Int,
  val aDouble: Double,
  val bool: Boolean,
  private val privateMember: String,
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(SomeDataClass.serializer()))
}
```

> You can get the full code [here](./code/example/example-plain-data-class-01.kt).

```typescript
export interface SomeDataClass {
  aString: string;
  anInt: number;
  aDouble: number;
  bool: boolean;
  privateMember: string;
}
```

<!--- TEST -->
