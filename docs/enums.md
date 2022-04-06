<!--- TEST_NAME EnumClassTest -->


**Table of contents**

<!--- TOC -->

* [Introduction](#introduction)
  * [Simple enum](#simple-enum)
  * [Enum with properties](#enum-with-properties)

<!--- END -->


<!--- INCLUDE .*\.kt
import kotlinx.serialization.*
import dev.adamko.kxstsgen.*
-->

## Introduction

### Simple enum

```kotlin
@Serializable
enum class SomeType {
  Alpha,
  Beta,
  Gamma
}

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(SomeType.serializer()))
}
```

> You can get the full code [here](./code/example/example-enum-class-01.kt).

```typescript
export enum SomeType {
  Alpha = "Alpha",
  Beta = "Beta",
  Gamma = "Gamma",
}
```

<!--- TEST -->

### Enum with properties

Because enums are static, fields aren't converted.

```kotlin
@Serializable
enum class SomeType2(val coolName: String) {
  Alpha("alpha") {
    val extra: Long = 123L
  },
  Beta("be_beta"),
  Gamma("gamma 3 3 3")
}

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(SomeType2.serializer()))
}
```

> You can get the full code [here](./code/example/example-enum-class-02.kt).

```typescript
export enum SomeType2 {
  Alpha = "Alpha",
  Beta = "Beta",
  Gamma = "Gamma",
}
```

<!--- TEST -->
