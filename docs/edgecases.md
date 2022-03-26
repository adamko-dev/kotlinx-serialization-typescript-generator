<!--- TEST_NAME EdgeCasesTest -->

**Table of contents**

<!--- TOC -->

* [Introduction](#introduction)
  * [Recursive references](#recursive-references)
    * [Classes](#classes)
    * [Lists](#lists)
    * [Map](#map)

<!--- END -->

<!--- INCLUDE .*\.kt
import kotlinx.serialization.*
import dev.adamko.kxstsgen.*
-->

## Introduction

Lorem ipsum...

### Recursive references

A references B which references A which references B... should be handled properly

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

> You can get the full code [here](./knit/example/example-edgecase-recursive-references-01.kt).

```typescript
interface A {
  b: B;
}

interface B {
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

> You can get the full code [here](./knit/example/example-edgecase-recursive-references-02.kt).

```typescript
interface A {
  list: B[];
}

interface B {
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

> You can get the full code [here](./knit/example/example-edgecase-recursive-references-03.kt).

```typescript
interface A {
  map: { [key: string]: B };
}

interface B {
  map: { [key: string]: A };
}
```

<!--- TEST -->
