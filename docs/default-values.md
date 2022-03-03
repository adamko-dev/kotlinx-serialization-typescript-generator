<!--- TEST_NAME DefaultValuesTest -->

<!--- INCLUDE .*\.kt
import kotlinx.serialization.*
import dev.adamko.kxstsgen.*
-->

### Default values

If a value has a default value, then it is not required for creating an encoded message. Therefore,
it will be marked as optional using the `?:` notation.

```kotlin
@Serializable
class Color(val rgb: Int = 12345)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Color.serializer().descriptor))
}
```

> You can get the full code [here](./knit/example/example-default-values-single-field-01.kt).

```typescript
interface Color {
  rgb?: number;
}
```

<!--- TEST -->

### Default and nullable

```kotlin
@Serializable
data class ContactDetails(
  val email: String?,
  val phoneNumber: String? = null,
  val active: Boolean? = true,
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(ContactDetails.serializer().descriptor))
}
```

> You can get the full code [here](./knit/example/example-default-values-primitive-fields-01.kt).

Email has no default, so it is not marked as optional.

Phone number and is nullable, and has a default, so i

```typescript
interface ContactDetails {
  email: string | null;
  phoneNumber?: string | null;
  active?: boolean | null;
}
```

<!--- TEST -->
