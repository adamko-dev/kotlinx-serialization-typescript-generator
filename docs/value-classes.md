<!--- TEST_NAME ValueClassesTest -->

<!--- INCLUDE .*\.kt
import kotlinx.serialization.*
import dev.adamko.kxstsgen.*
-->

### Inline value classes

Value classes are transformed to type aliases. The type of the value class is used.

```kotlin
@Serializable
@JvmInline
value class AuthToken(private val token: String)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(AuthToken.serializer().descriptor))
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
      UByte.serializer().descriptor,
      UShort.serializer().descriptor,
      UInt.serializer().descriptor,
      ULong.serializer().descriptor,
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

(At present this is not very useful as Typescript will make no distinction between any of these
numbers, even though they are distinct in Kotlin. 'Brand typing' might be introduced in the future.)

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
  println(tsGenerator.generate(UserCount.serializer().descriptor))
}
```

> You can get the full code [here](./knit/example/example-value-classes-03.kt).

```typescript
type UInt = number;

type UserCount = UInt;
```

<!--- TEST -->
