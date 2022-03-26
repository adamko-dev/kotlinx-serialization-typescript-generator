<!--- TEST_NAME ValueClassesTest -->


**Table of contents**

<!--- TOC -->

* [Introduction](#introduction)
  * [Inline value classes](#inline-value-classes)
  * [Brand typing](#brand-typing)
  * [Nested value classes](#nested-value-classes)

<!--- END -->


<!--- INCLUDE .*\.kt
import kotlinx.serialization.*
import dev.adamko.kxstsgen.*
-->

## Introduction


### Inline value classes

Value classes are transformed to type aliases. The type of the value class is used.

```kotlin
@Serializable
@JvmInline
value class AuthToken(private val token: String)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(AuthToken.serializer()))
}
```

> You can get the full code [here](./knit/example/example-value-classes-01.kt).

```typescript
type AuthToken = string;
```

<!--- TEST -->


Value classes from other libraries are also transformed to their inner types. For example, Kotlin
unsigned numbers.

```kotlin
import kotlinx.serialization.builtins.serializer

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(
    tsGenerator.generate(
      UByte.serializer(),
      UShort.serializer(),
      UInt.serializer(),
      ULong.serializer(),
    )
  )
}
```

<!-- PREFIX -->

> You can get the full code [here](./knit/example/example-value-classes-02.kt).

```typescript
type UByte = number;

type UShort = number;

type UInt = number;

type ULong = number;
```

<!--- TEST -->


### Brand typing

To make value classes a little more strict, we can use brand typing


```kotlin
import kotlinx.serialization.builtins.serializer
import dev.adamko.kxstsgen.KxsTsConfig.TypeAliasTypingConfig.BrandTyping
```

<!-- IMPORT -->

```kotlin

fun main() {

  val tsConfig = KxsTsConfig(typeAliasTyping = BrandTyping)

  val tsGenerator = KxsTsGenerator(config = tsConfig)
  println(
    tsGenerator.generate(
      ULong.serializer(),
    )
  )
}
```

<!-- PREFIX -->

> You can get the full code [here](./knit/example/example-value-classes-03.kt).

```typescript
type ULong = number & { __ULong__: void };
```

<!--- TEST -->


### Nested value classes

If the value class contains another value class, then the outer class will be aliased to other value
class

```kotlin
@Serializable
@JvmInline
value class UserCount(private val count: UInt)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(UserCount.serializer()))
}
```

> You can get the full code [here](./knit/example/example-value-classes-04.kt).

```typescript
type UserCount = UInt;

type UInt = number;
```

<!--- TEST -->
