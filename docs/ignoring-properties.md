<!--- TEST_NAME IgnoringPropertiesTest -->


**Table of contents**

<!--- TOC -->

  * [Ignoring fields](#ignoring-fields)

<!--- END -->

<!--- INCLUDE .*\.kt
import kotlinx.serialization.*
import dev.adamko.kxstsgen.*
-->

### Ignoring fields

Just like in Kotlinx.Serialization,
[fields can be ignored](https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/basic-serialization.md#transient-properties)

> A property can be excluded from serialization by marking it with the
> [`@Transient`](https://kotlin.github.io/kotlinx.serialization/kotlinx-serialization-core/kotlinx.serialization/-transient/index.html)
> annotation
> (don't confuse it with
> [`kotlin.jvm.Transient`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-transient/)).
> Transient properties must have a default value.

```kotlin
import kotlinx.serialization.Transient

@Serializable
class SimpleTypes(
  @Transient
  val aString: String = "default-value"
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(SimpleTypes.serializer()))
}
```

> You can get the full code [here](./code/example/example-plain-class-ignored-property-01.kt).

```typescript
export interface SimpleTypes {
}
```

<!--- TEST -->
