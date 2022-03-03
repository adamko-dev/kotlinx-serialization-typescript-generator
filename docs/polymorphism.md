<!--- TEST_NAME PolymorphismTest -->

### Sealed classes

<!--- INCLUDE .*\.kt
import kotlinx.serialization.*
import dev.adamko.kxstsgen.*
-->

```kotlin
@Serializable
sealed class Project {
  abstract val name: String
}

@Serializable
data class OwnedProject(
  override val name: String,
  val owner: String,
) : Project()

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(
    tsGenerator.generate(
      Project.serializer().descriptor,
      OwnedProject.serializer().descriptor,
    )
  )
}
```

> You can get the full code [here](./knit/example/example-polymorphism-sealed-01.kt).

```typescript
interface Color {
  rgb: number;
}
```

<!--- TEST -->

### Abstract class with primitive fields

```kotlin
@Serializable
abstract class SimpleTypes(
  val aString: String,
  var anInt: Int,
  val aDouble: Double,
  val bool: Boolean,
  private val privateMember: String,
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(SimpleTypes.serializer().descriptor))
}
```

> You can get the full code [here](./knit/example/example-abstract-class-primitive-fields-01.kt).

```typescript
interface SimpleTypes {
  aString: string;
  anInt: number;
  aDouble: number;
  bool: boolean;
  privateMember: string;
}
```

<!--- TEST -->
