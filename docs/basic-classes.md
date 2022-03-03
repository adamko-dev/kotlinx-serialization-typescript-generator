<!--- TEST_NAME BasicClassesTest -->

**Table of contents**

<!--- TOC -->

* [Introduction](#introduction)
  * [Plain class with a single field](#plain-class-with-a-single-field)
  * [Plain class with primitive fields](#plain-class-with-primitive-fields)
  * [Data class with primitive fields](#data-class-with-primitive-fields)

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
  println(tsGenerator.generate(Color.serializer().descriptor))
}
```

> You can get the full code [here](./knit/example/example-plain-class-single-field-01.kt).

```typescript
interface Color {
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
  println(tsGenerator.generate(SimpleTypes.serializer().descriptor))
}
```

> You can get the full code [here](./knit/example/example-plain-class-primitive-fields-01.kt).

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
  println(tsGenerator.generate(SomeDataClass.serializer().descriptor))
}
```

> You can get the full code [here](./knit/example/example-plain-data-class-01.kt).

```typescript
interface SomeDataClass {
  aString: string;
  anInt: number;
  aDouble: number;
  bool: boolean;
  privateMember: string;
}
```

<!--- TEST -->
