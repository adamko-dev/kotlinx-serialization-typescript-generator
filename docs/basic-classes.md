<!--- TEST_NAME BasicClassesTest -->

**Table of contents**

<!--- TOC -->

* [Introduction](#introduction)
  * [Plain class with a single field](#plain-class-with-a-single-field)
  * [Plain class with primitive fields](#plain-class-with-primitive-fields)
  * [Data class with primitive fields](#data-class-with-primitive-fields)
  * [Ignoring fields with `@Transitive`](#ignoring-fields-with-@transitive)

<!--- END -->

## Introduction

Lorem ipsum...

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

### Ignoring fields with `@Transitive`

Just like in Kotlinx.Serialization,
[fields can be ignored](https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/basic-serialization.md#transient-properties)

> A property can be excluded from serialization by marking it with the
> [`@Transient`](https://kotlin.github.io/kotlinx.serialization/kotlinx-serialization-core/kotlinx.serialization/-transient/index.html)
> annotation
> (don't confuse it with
> [`kotlin.jvm.Transient`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-transient/)).
> Transient properties must have a default value.

```kotlin
import kotlinx.serialization.Transient

@Serializable
class SimpleTypes(
  @Transient
  val aString: String = "default-value"
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(SimpleTypes.serializer()))
}
```

> You can get the full code [here](./code/example/example-plain-class-primitive-fields-02.kt).

```typescript
export interface SimpleTypes {
}
```

<!--- TEST -->
