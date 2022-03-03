# Kotlinx Serialization TypeScript Generator

Create TypeScript interfaces from Kotlin classes

```kotlin
@Serializable
data class PlayerDetails(
  val name: String,
  val health: Float,
)

println(
  KxsTsGenerator().generate(Color.serializer().descriptor)
)
```

```typescript
interface PlayerDetails {
  name: string;
  health: number;
}
```

See [the docs](./docs) for working examples.
