<!--- TEST_NAME PolymorphismSealedTest -->

**Table of contents**

<!--- TOC -->

* [Closed Polymorphism](#closed-polymorphism)
  * [Static types](#static-types)
  * [Sealed classes](#sealed-classes)
  * [Nested sealed classes](#nested-sealed-classes)
  * [Objects](#objects)
* [Open Polymorphism](#open-polymorphism)
  * [Generics](#generics)
  * [JSON content polymorphism](#json-content-polymorphism)

<!--- END -->


<!--- INCLUDE .*\.kt
import kotlinx.serialization.*
import dev.adamko.kxstsgen.*
-->

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

> You can get the full code [here](./code/example/example-polymorphic-static-types-01.kt).

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
  val config = KxsTsConfig(serializersModule = module)

  val tsGenerator = KxsTsGenerator(config)

  println(tsGenerator.generate(Project.serializer()))
}
```

> You can get the full code [here](./code/example/example-polymorphic-static-types-02.kt).

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

> You can get the full code [here](./code/example/example-polymorphic-sealed-class-01.kt).

```typescript
export type Project =
  | Project.DeprecatedProject
  | Project.OProj;

export namespace Project {
  export enum Type {
    DeprecatedProject = "dev.adamko.kxstsgen.example.examplePolymorphicSealedClass01.DeprecatedProject",
    OProj = "OProj",
  }

  export interface DeprecatedProject {
    type: Project.Type.DeprecatedProject;
    name: string;
    reason: string;
  }

  export interface OProj {
    type: Project.Type.OProj;
    name: string;
    owner: string;
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
  @SerialName("Dog.Mutt")
  class Mutt(override val name: String, val loveable: Boolean = true) : Dog()

  @Serializable
  sealed class Retriever : Dog() {
    abstract val colour: String

    @Serializable
    @SerialName("Dog.Retriever.Golden")
    data class Golden(
      override val name: String,
      override val colour: String,
      val cute: Boolean = true,
    ) : Retriever()

    @Serializable
    @SerialName("Dog.Retriever.NovaScotia")
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

> You can get the full code [here](./code/example/example-polymorphic-sealed-class-02.kt).

```typescript
export type Dog =
  | Dog.Golden
  | Dog.Mutt
  | Dog.NovaScotia;

export namespace Dog {
  export enum Type {
    Mutt = "Dog.Mutt",
    Golden = "Dog.Retriever.Golden",
    NovaScotia = "Dog.Retriever.NovaScotia",
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

> You can get the full code [here](./code/example/example-polymorphic-objects-01.kt).

```typescript
export type Response =
  | Response.EmptyResponse
  | Response.TextResponse;

export namespace Response {
  export enum Type {
    EmptyResponse = "dev.adamko.kxstsgen.example.examplePolymorphicObjects01.EmptyResponse",
    TextResponse = "dev.adamko.kxstsgen.example.examplePolymorphicObjects01.TextResponse",
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

Kotlinx Serialization doesn't have 'generic' SerialDescriptors, so KxsTsGen can't generate generic
TypeScript classes.

```kotlin
import kotlinx.serialization.builtins.serializer

@Serializable
class Box<T : Number>(
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

> You can get the full code [here](./code/example/example-generics-01.kt).

```typescript
export interface Box {
  value: number;
}
```

<!--- TEST -->

### JSON content polymorphism

Using a
[`JsonContentPolymorphicSerializer`](https://kotlin.github.io/kotlinx.serialization/kotlinx-serialization-json/kotlinx.serialization.json/-json-content-polymorphic-serializer/index.html)
means there's not enough data in the `SerialDescriptor` to generate a TypeScript interface. Instead,
a named type alias to 'any' will be created instead.

```kotlin
import kotlinx.serialization.json.*

@Serializable
abstract class Project {
  abstract val name: String
}

@Serializable
data class BasicProject(override val name: String) : Project()

@Serializable
data class OwnedProject(override val name: String, val owner: String) : Project()

object ProjectSerializer : JsonContentPolymorphicSerializer<Project>(Project::class) {
  override fun selectDeserializer(element: JsonElement) = when {
    "owner" in element.jsonObject -> OwnedProject.serializer()
    else                          -> BasicProject.serializer()
  }
}

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(ProjectSerializer))
}
```

> You can get the full code [here](./code/example/example-json-polymorphic-01.kt).

```typescript
export type Project = any;
```

<!--- TEST -->
