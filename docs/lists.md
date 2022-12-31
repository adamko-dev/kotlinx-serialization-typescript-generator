# Lists

<!--- TEST_NAME ListsTests -->
<!--- INCLUDE .*\.kt
import kotlinx.serialization.*
import dev.adamko.kxstsgen.*
-->


### Primitive lists

A collection will get converted to array.

```kotlin
@Serializable
data class MyLists(
  val strings: List<String>,
  val ints: Set<Int>,
  val longs: Collection<Long>,
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(MyLists.serializer()))
}
```

> You can get the full code [here](./code/example/example-list-primitive-01.kt).

```typescript
export interface MyLists {
  strings: string[];
  ints: number[];
  longs: number[];
}
```

<!--- TEST -->

### Lists of objects

```kotlin
@Serializable
data class Colour(
  val rgb: String
)

@Serializable
data class MyLists(
  val colours: List<Colour>,
  val colourGroups: Set<List<Colour>>,
  val colourGroupGroups: LinkedHashSet<List<List<Colour>>>,
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(MyLists.serializer()))
}
```

> You can get the full code [here](./code/example/example-list-objects-01.kt).

```typescript
export interface MyLists {
  colours: Colour[];
  colourGroups: Colour[][];
  colourGroupGroups: Colour[][][];
}

export interface Colour {
  rgb: string;
}
```

<!--- TEST -->

### Lists of collections

```kotlin
@Serializable
data class Colour(
  val rgb: String
)

@Serializable
data class MyLists(
  val listOfMaps: List<Map<String, Int>>,
  val listOfColourMaps: List<Map<String, Colour>>,
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(MyLists.serializer()))
}
```

> You can get the full code [here](./code/example/example-list-objects-02.kt).

```typescript
export interface MyLists {
  listOfMaps: { [key: string]: number }[];
  listOfColourMaps: { [key: string]: Colour }[];
}

export interface Colour {
  rgb: string;
}
```

<!--- TEST -->
