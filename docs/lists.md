<!--- TEST_NAME ListsTests -->

**Table of contents**

<!--- TOC -->

* [Introduction](#introduction)
  * [Primitive lists](#primitive-lists)

<!--- END -->


<!--- INCLUDE .*\.kt
import kotlinx.serialization.*
import dev.adamko.kxstsgen.*
-->

## Introduction

### Primitive lists

```kotlin
@Serializable
data class MyLists(
  val strings: List<String>,
  val ints: List<Int>,
  val longs: List<Long>,
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(MyLists.serializer()))
}
```

> You can get the full code [here](./knit/example/example-list-primitive-01.kt).

```typescript
interface MyLists {
  strings: string[];
  ints: number[];
  longs: number[];
}
```

<!--- TEST -->
