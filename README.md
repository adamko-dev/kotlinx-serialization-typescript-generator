# Kotlinx Serialization TypeScript Generator

Create TypeScript interfaces from Kotlinx Serialization classes.

```kotlin
@Serializable
data class PlayerDetails(
  val name: String,
  val health: Float,
)

println(
  KxsTsGenerator().generate(Color.serializer())
)
```

```typescript
interface PlayerDetails {
  name: string;
  health: number;
}
```

The aim is to create TypeScript interfaces that can accurately produce JSON that Kotlinx
Serialization can parse.

See [the docs](./docs) for working examples.

## Status

This is a proof-of-concept.

|                                       | Status                                                   | Notes                                                                                             |
|---------------------------------------|----------------------------------------------------------|:--------------------------------------------------------------------------------------------------|
| Basic classes                         | ✅       [example](./docs/basic-classes.md)               |                                                                                                   |
| Nullable and default-value properties | ✅       [example](./docs/default-values.md)              |                                                                                                   |
| Value classes                         | ✅       [example](./docs/value-classes.md)               |                                                                                                   |
| Enums                                 | ✅       [example](./docs/enums.md)                       |                                                                                                   |
| Lists                                 | ✅       [example](./docs/lists.md)                       |                                                                                                   |
| Maps                                  | ✅/⚠     [example](./docs/maps.md)                        | Maps with complex keys are converted to an ES6 Map.  [See](./docs/maps.md#maps-with-complex-keys) |
| Polymorphism - Sealed classes         | ✅/⚠     [example](./docs/polymorphism.md#sealed-classes) | Nested sealed classes are ignored                                                                 |
| Polymorphism - Open classes           | ❌       [example](./docs/abstract-classes.md)            | Not implemented. Converted to `type MyClass = any`                                                |
| Edge cases - circular dependencies    | ✅       [example](./docs/edgecases.md)                   |                                                                                                   |
