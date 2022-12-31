# Edge cases

Demonstrations of how edge cases are handled.

<!--- TEST_NAME EdgeCasesTest -->
<!--- INCLUDE .*\.kt
import kotlinx.serialization.*
import dev.adamko.kxstsgen.*
-->

### Recursive references

`A` references `B` which references `A` which references `B`... should be handled properly

#### Classes

```kotlin
@Serializable
class A(val b: B)

@Serializable
class B(val a: A)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(A.serializer(), B.serializer()))
}
```

> You can get the full code [here](./code/example/example-edgecase-recursive-references-01.kt).

```typescript
export interface A {
  b: B;
}

export interface B {
  a: A;
}
```

<!--- TEST -->

#### Lists

```kotlin
@Serializable
class A(
  val list: List<B>
)

@Serializable
class B(
  val list: List<A>
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(A.serializer(), B.serializer()))
}
```

> You can get the full code [here](./code/example/example-edgecase-recursive-references-02.kt).

```typescript
export interface A {
  list: B[];
}

export interface B {
  list: A[];
}
```

<!--- TEST -->

#### Map

```kotlin
@Serializable
class A(
  val map: Map<String, B>
)

@Serializable
class B(
  val map: Map<String, A>
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(A.serializer(), B.serializer()))
}
```

> You can get the full code [here](./code/example/example-edgecase-recursive-references-03.kt).

```typescript
export interface A {
  map: { [key: string]: B };
}

export interface B {
  map: { [key: string]: A };
}
```

<!--- TEST -->
