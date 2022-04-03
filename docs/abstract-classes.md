<!--- TEST_NAME AbstractClassesTest -->

**Table of contents**

<!--- TOC -->

* [Introduction](#introduction)
  * [Abstract class with a single field](#abstract-class-with-a-single-field)
  * [Abstract class with primitive fields](#abstract-class-with-primitive-fields)
  * [Abstract class, abstract value](#abstract-class-abstract-value)

<!--- END -->


<!--- INCLUDE .*\.kt
import kotlinx.serialization.*
import dev.adamko.kxstsgen.*
-->

## Introduction

### Abstract class with a single field

```kotlin
@Serializable
abstract class Color(val rgb: Int)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Color.serializer()))
}
```

> You can get the full code [here](./code/example/example-abstract-class-single-field-01.kt).

```typescript
export type Color = any;
// interface Color {
//   rgb: number;
// }
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
  println(tsGenerator.generate(SimpleTypes.serializer()))
}
```

> You can get the full code [here](./code/example/example-abstract-class-primitive-fields-01.kt).

```typescript
export type SimpleTypes = any;
// export interface SimpleTypes {
//   aString: string;
//   anInt: number;
//   aDouble: number;
//   bool: boolean;
//   privateMember: string;
// }
```

<!--- TEST -->

### Abstract class, abstract value

```kotlin
@Serializable
abstract class AbstractSimpleTypes {
  abstract val aString: String
  abstract var anInt: Int
  abstract val aDouble: Double
  abstract val bool: Boolean
}

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(AbstractSimpleTypes.serializer()))
}
```

> You can get the full code [here](./code/example/example-abstract-class-abstract-field-01.kt).

```typescript
export type AbstractSimpleTypes = any;
// export interface AbstractSimpleTypes {
//   rgb: number;
// }
```

<!--- TEST -->
