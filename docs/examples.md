# Examples

[kxs-ts-gen](https://github.com/adamko-dev/kotlinx-serialization-typescript-generator)
can convert `@Serializable` Kotlin classes to TypeScript interfaces.

#### Classes

* [Basic classes and data classes](basic-classes.md)
* [Enums](enums.md)
* [Value classes](value-classes.md)

* Polymorphic classes
  * [Sealed classes (closed polymorphism)](polymorphism-sealed.md) are well-supported
  * [Open polymorphism (abstract class)](polymorphism-open.md) is handled, but does not produce
    useful TypeScript

#### Collections

* [Collections](lists.md)
* [Maps](maps.md)

#### Customising TypeScript

The generated TypeScript can be customised

* [Ignore class properties](ignoring-properties.md)
* [Default property values](default-values.md)
* [Customising output](customising-output.md)

Kotlin does not have a direct equivalent of TypeScript tuples,
[however it is possible to convert a Kotlin class to a TypeScript tuple](tuples.md).

#### Edge cases

[Demonstrations of how edge cases are handled](edgecases.md).
