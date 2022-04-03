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

The aim is to create TypeScript interfaces that can accurately produce Kotlinx Serialization
compatible JSON.

The Kotlinx Serialization API should be used to generate TypeScript. The
[`SerialDescriptor`s](https://kotlin.github.io/kotlinx.serialization/kotlinx-serialization-core/kotlinx.serialization.descriptors/-serial-descriptor/index.html)
are flexible and comprehensive enough to allow for accurate TypeScript code, without any deviation.

See [the docs](./docs) for working examples.

## Status

This is a proof-of-concept.

|                                       | Status                                                   | Notes                                                                                            |
|---------------------------------------|----------------------------------------------------------|:-------------------------------------------------------------------------------------------------|
| Kotlin multiplatform                  | ❓                                                        | The codebase is multiplatform, but only JVM has been tested                                      |
| `@SerialName`                         | ✅/⚠                                                      | The serial name is directly converted and might produce invalid TypeScript                       |
| Basic classes                         | ✅       [example](./docs/basic-classes.md)               |                                                                                                  |
| Nullable and default-value properties | ✅       [example](./docs/default-values.md)              |                                                                                                  |
| Value classes                         | ✅       [example](./docs/value-classes.md)               |                                                                                                  |
| Enums                                 | ✅       [example](./docs/enums.md)                       |                                                                                                  |
| Lists                                 | ✅       [example](./docs/lists.md)                       |                                                                                                  |
| Maps                                  | ✅/⚠     [example](./docs/maps.md)                        | Maps with complex keys are converted to an ES6 Map, [see](./docs/maps.md#maps-with-complex-keys) |
| Polymorphism - Sealed classes         | ✅/⚠     [example](./docs/polymorphism.md#sealed-classes) | Nested sealed classes are ignored, [see](./docs/polymorphism.md#nested-sealed-classes)           |
| Polymorphism - Open classes           | ❌       [example](./docs/abstract-classes.md)            | Not implemented. Converted to `type MyClass = any`                                               |
| `@JsonClassDiscriminator`             | ❌                                                        | Not implemented                                                                                  |
| Edge cases - circular dependencies    | ✅       [example](./docs/edgecases.md)                   |                                                                                                  |
