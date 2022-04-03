<!--- TEST_NAME PolymorphismTest -->

**Table of contents**

<!--- TOC -->

* [Introduction](#introduction)
  * [Abstract class with primitive fields](#abstract-class-with-primitive-fields)
* [Closed Polymorphism](#closed-polymorphism)
  * [Static types](#static-types)
  * [Sealed classes](#sealed-classes)
  * [Nested sealed classes](#nested-sealed-classes)
  * [Objects](#objects)
* [Open Polymorphism](#open-polymorphism)
  * [Generics](#generics)

<!--- END -->


<!--- INCLUDE .*\.kt
import kotlinx.serialization.*
import dev.adamko.kxstsgen.*
-->

## Introduction

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
  println(tsGenerator.generate(SimpleTypes.serializer()))
}
```

> You can get the full code [here](./knit/example/example-polymorphic-abstract-class-primitive-fields-01.kt).

```typescript
export type SimpleTypes = any;
// export interface SimpleTypes {
//   aString: string;
//   anInt: number;
//   aDouble: number;
//   bool: boolean;
//   privateMember: string;
// }
```

<!--- TEST -->

## Closed Polymorphism

https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/polymorphism.md#closed-polymorphism

### Static types

```kotlin
@Serializable
open class Project(val name: String)

class OwnedProject(name: String, val owner: String) : Project(name)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Project.serializer()))
}
```

> You can get the full code [here](./knit/example/example-polymorphic-static-types-01.kt).

Only the Project class properties are generated.

```typescript
export interface Project {
  name: string;
}
```

<!--- TEST -->

```kotlin
import kotlinx.serialization.modules.*

@Serializable
abstract class Project {
  abstract val name: String
}

@Serializable
class OwnedProject(override val name: String, val owner: String) : Project()

val module = SerializersModule {
  polymorphic(Project::class) {
    subclass(OwnedProject::class)
  }
}

fun main() {
  val tsGenerator = KxsTsGenerator(serializersModule = module)
  println(tsGenerator.generate(Project.serializer()))
}
```

> You can get the full code [here](./knit/example/example-polymorphic-static-types-02.kt).

```typescript
export type Project = any;
// export interface Project {
//   name: string;
// }
//
// export interface OwnedProject extends Project {
//   name: string;
//   owner: string;
// }
```

<!--- TEST -->

### Sealed classes

Sealed classes are the best way to generate TypeScript interface so far, because all subclasses are
defined in the `SerialDescriptor`.

A sealed class will be converted as a
[union enum, with enum member types](https://www.typescriptlang.org/docs/handbook/enums.html#union-enums-and-enum-member-types)
.

This has many benefits that closely match how sealed classes work in Kotlin.

```kotlin
@Serializable
sealed class Project {
  abstract val name: String
}

@Serializable
@SerialName("OProj")
class OwnedProject(override val name: String, val owner: String) : Project()

@Serializable
class DeprecatedProject(override val name: String, val reason: String) : Project()

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Project.serializer()))
}
```

> You can get the full code [here](./knit/example/example-polymorphic-sealed-class-01.kt).

```typescript
export type Project = Project.DeprecatedProject | Project.OProj;

export namespace Project {
  export enum Type {
    OProj = "OProj",
    DeprecatedProject = "DeprecatedProject",
  }

  export interface OProj {
    type: Project.Type.OProj;
    name: string;
    owner: string;
  }

  export interface DeprecatedProject {
    type: Project.Type.DeprecatedProject;
    name: string;
    reason: string;
  }
}
```

<!--- TEST -->

### Nested sealed classes

Nested sealed classes are 'invisible' to Kotlinx Serialization. In this
example, `sealed class Retriever` is ignored.

For now, it's recommended to avoid nested sealed classes.

```kotlin
@Serializable
sealed class Dog {
  abstract val name: String

  @Serializable
  class Mutt(override val name: String, val loveable: Boolean = true) : Dog()

  @Serializable
  sealed class Retriever : Dog() {
    abstract val colour: String

    @Serializable
    data class Golden(
      override val name: String,
      override val colour: String,
      val cute: Boolean = true,
    ) : Retriever()

    @Serializable
    data class NovaScotia(
      override val name: String,
      override val colour: String,
      val adorable: Boolean = true,
    ) : Retriever()
  }
}

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Dog.serializer()))
}
```

> You can get the full code [here](./knit/example/example-polymorphic-sealed-class-02.kt).

```typescript
export type Dog = Dog.Golden | Dog.Mutt | Dog.NovaScotia;

export namespace Dog {
  export enum Type {
    Mutt = "Mutt",
    Golden = "Golden",
    NovaScotia = "NovaScotia",
  }

  export interface Mutt {
    type: Dog.Type.Mutt;
    name: string;
    loveable?: boolean;
  }

  export interface Golden {
    type: Dog.Type.Golden;
    name: string;
    colour: string;
    cute?: boolean;
  }

  export interface NovaScotia {
    type: Dog.Type.NovaScotia;
    name: string;
    colour: string;
    adorable?: boolean;
  }
}
// Nested sealed classes don't work at the moment :(
// export type Dog = Dog.Mutt | Dog.Retriever
//
// export namespace Dog {
//   export enum Type {
//     Mutt = "Mutt",
//   }
//
//   export interface Mutt {
//     type: Type.Mutt;
//     name: string;
//     loveable?: boolean;
//   }
//
//   export type Retriever = Retriever.Golden | Retriever.NovaScotia
//
//   export namespace Retriever {
//     export enum Type {
//       Golden = "Golden",
//       NovaScotia = "NovaScotia",
//     }
//
//     export interface Golden {
//       type: Type.Golden;
//       name: string;
//       cute?: boolean;
//     }
//
//     export interface NovaScotia {
//       type: Type.NovaScotia;
//       name: string;
//       adorable?: boolean;
//     }
//   }
// }
```

<!--- TEST -->

### Objects

```kotlin
@Serializable
sealed class Response

@Serializable
object EmptyResponse : Response()

@Serializable
class TextResponse(val text: String) : Response()

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(
    tsGenerator.generate(Response.serializer())
  )
}
```

> You can get the full code [here](./knit/example/example-polymorphic-objects-01.kt).

```typescript
export type Response = Response.EmptyResponse | Response.TextResponse;

export namespace Response {
  export enum Type {
    EmptyResponse = "EmptyResponse",
    TextResponse = "TextResponse",
  }

  export interface EmptyResponse {
    type: Response.Type.EmptyResponse;
  }

  export interface TextResponse {
    type: Response.Type.TextResponse;
    text: string;
  }
}
```

<!--- TEST -->

## Open Polymorphism

### Generics

<!--- INCLUDE
import kotlinx.serialization.builtins.serializer
-->

```kotlin
@Serializable
class Box<T>(
  val value: T,
)

fun main() {
  val tsGenerator = KxsTsGenerator()

  println(
    tsGenerator.generate(
      Box.serializer(Double.serializer()),
    )
  )
}
```

> You can get the full code [here](./knit/example/example-generics-01.kt).

```typescript
export type Double = number & { __kotlin_Double__: void }

export interface Box {
  value: Double
}
```
