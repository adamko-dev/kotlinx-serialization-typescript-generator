[![Status](https://img.shields.io/badge/status-proof--of--concept-blueviolet?style=flat-square)](https://github.com/adamko-dev/kotlinx-serialization-typescript-generator#status)
[![GitHub license](https://img.shields.io/github/license/adamko-dev/kotlinx-serialization-typescript-generator?style=flat-square)](https://github.com/adamko-dev/kotlinx-serialization-typescript-generator/blob/main/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/dev.adamko.txstsgen/kxs-ts-gen-core?style=flat-square)](https://search.maven.org/search?q=g:dev.adamko.kxstsgen)
[![](https://jitpack.io/v/adamko-dev/kotlinx-serialization-typescript-generator.svg?style=flat-square)](https://jitpack.io/#adamko-dev/kotlinx-serialization-typescript-generator)

# Kotlinx Serialization TypeScript Generator

[Kotlinx Serialization TypeScript Generator](https://github.com/adamko-dev/kotlinx-serialization-typescript-generator)
creates TypeScript interfaces from
[Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization/)
classes.

```kotlin
@Serializable
class MyClass(
  val aString: String,
  var anInt: Int,
  val aDouble: Double,
  val bool: Boolean,
  private val privateMember: String,
)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(MyClass.serializer()))
}
```

Generated TypeScript interface:

```typescript
export interface MyClass {
  aString: string;
  anInt: number;
  aDouble: number;
  bool: boolean;
  privateMember: string;
}
```

Only Kotlinx Serialization
[`SerialDescriptor`s](https://kotlin.github.io/kotlinx.serialization/kotlinx-serialization-core/kotlinx.serialization.descriptors/-serial-descriptor/index.html)
are used to generate TypeScript. They are flexible and comprehensive enough to allow for accurate TypeScript code, without any deviation.

See [the docs](./docs) for working examples.

## Status

This is a proof-of-concept.

The aim is to create TypeScript interfaces that can accurately produce Kotlinx Serialization
compatible JSON.

|                                       | Status                                                          | Notes                                                                                                          |
|---------------------------------------|-----------------------------------------------------------------|:---------------------------------------------------------------------------------------------------------------|
| Kotlin multiplatform                  | ❓                                                               | The codebase is multiplatform, but only JVM has been tested                                                    |
| `@SerialName`                         | ✅/⚠                                                             | The serial name is directly converted and might produce invalid TypeScript                                     |
| Basic classes                         | ✅   [example](./docs/basic-classes.md)                          |                                                                                                                |
| Nullable and default-value properties | ✅   [example](./docs/default-values.md)                         |                                                                                                                |
| Value classes                         | ✅   [example](./docs/value-classes.md)                          |                                                                                                                |
| Enums                                 | ✅   [example](./docs/enums.md)                                  |                                                                                                                |
| Lists                                 | ✅   [example](./docs/lists.md)                                  |                                                                                                                |
| Maps                                  | ✅/⚠ [example](./docs/maps.md)                                   | Maps with complex keys are converted to an ES6 Map, [see documentation](./docs/maps.md#maps-with-complex-keys) |
| Polymorphism - Sealed classes         | ✅/⚠ [example](./docs/polymorphism.md#sealed-classes)            | Nested sealed classes are ignored, [see documentation](./docs/polymorphism.md#nested-sealed-classes)           |
| Polymorphism - Open classes           | ❌   [example](./docs/abstract-classes.md)                       | Not implemented. Converted to `type MyClass = any`                                                             |
| `@JsonClassDiscriminator`             | ❌                                                               | Not implemented                                                                                                |
| JSON Content polymorphism             | ❌   [example](./docs/polymorphism.md#json-content-polymorphism) | Not implemented. Converted to `type MyClass = any`                                                             |
| Edge cases - circular dependencies    | ✅   [example](./docs/edgecases.md)                              |                                                                                                                |
