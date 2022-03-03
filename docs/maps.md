<!--- TEST_NAME MapsTests -->

### Primitive lists

<!--- INCLUDE .*\.kt
import kotlinx.serialization.*
import dev.adamko.kxstsgen.*
-->

```kotlin
@Serializable
class Config {
  val properties: Map<String, String> = mapOf()
}

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Config.serializer().descriptor))
}
```

> You can get the full code [here](./knit/example/example-map-primitive-01.kt).

```typescript
interface Config {
  properties: { [key: string]: string };
}
```

<!--- TEST -->
