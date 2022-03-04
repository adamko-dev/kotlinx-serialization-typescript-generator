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
data class CalendarEvent(
  val attendeeNames: List<String>
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(CalendarEvent.serializer().descriptor))
}
```

> You can get the full code [here](./knit/example/example-list-primitive-01.kt).

```typescript
interface CalendarEvent {
  attendeeNames: string[];
}
```

<!--- TEST -->
