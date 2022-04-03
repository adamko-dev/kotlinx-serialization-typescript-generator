<!--- TEST_NAME DefaultValuesTest -->

**Table of contents**

<!--- TOC -->

* [Introduction](#introduction)
  * [Default values](#default-values)
  * [Nullable values](#nullable-values)
  * [Default and nullable](#default-and-nullable)

<!--- END -->


<!--- INCLUDE .*\.kt
import kotlinx.serialization.*
import dev.adamko.kxstsgen.*
-->

## Introduction

Some properties of a class are optional, or nullable, or both.

### Default values

If a value has a default value, then it is not required for creating an encoded message. Therefore,
it will be marked as optional using the `?:` notation.

```kotlin
@Serializable
class Colour(val rgb: Int = 12345)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Colour.serializer()))
}
```

> You can get the full code [here](./code/example/example-default-values-single-field-01.kt).

```typescript
export interface Colour {
  rgb?: number;
}
```

<!--- TEST -->

### Nullable values

Properties might be required, but the value can be nullable. In TypeScript that is represented with
a type union that includes `null`.

```kotlin
@Serializable
class Colour(val rgb: Int?) // 'rgb' is required, but the value can be null

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Colour.serializer()))
}
```

> You can get the full code [here](./code/example/example-default-values-single-field-02.kt).

```typescript
export interface Colour {
  rgb: number | null;
}
```

<!--- TEST -->

### Default and nullable

A property can be both nullable and optional, which gives four possible options.

```kotlin
@Serializable
data class ContactDetails(
  // nullable: ❌, optional: ❌
  val name: String,
  // nullable: ✅, optional: ❌
  val email: String?,
  // nullable: ❌, optional: ✅
  val active: Boolean = true,
  // nullable: ✅, optional: ✅
  val phoneNumber: String? = null,
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(ContactDetails.serializer()))
}
```

> You can get the full code [here](./code/example/example-default-values-primitive-fields-01.kt).

```typescript
export interface ContactDetails {
  name: string;
  email: string | null;
  active?: boolean;
  phoneNumber?: string | null;
}
```

<!--- TEST -->
