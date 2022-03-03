<!--- TEST_NAME EnumClassTest -->

## Introduction

Lorem ipsum...

### Plain class with a single field

<!--- INCLUDE .*\.kt
import kotlinx.serialization.*
import dev.adamko.kxstsgen.*
-->

```kotlin
@Serializable
enum class SomeType {
  Alpha,
  Beta,
  Gamma
}

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(SomeType.serializer().descriptor))
}
```

> You can get the full code [here](./knit/example/example-enum-class-01.kt).

```typescript
enum SomeType {
  Alpha = "Alpha",
  Beta = "Beta",
  Gamma = "Gamma",
}
```

<!--- TEST -->

### Plain class with primitive fields

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
  println(tsGenerator.generate(SomeType2.serializer().descriptor))
}
```

> You can get the full code [here](./knit/example/example-enum-class-02.kt).

```typescript
enum SomeType2 {
  Alpha = "Alpha",
  Beta = "Beta",
  Gamma = "Gamma",
}
```

<!--- TEST -->
