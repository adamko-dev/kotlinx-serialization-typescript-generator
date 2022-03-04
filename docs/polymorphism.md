<!--- TEST_NAME PolymorphismTest -->

**Table of contents**

<!--- TOC -->

* [Introduction](#introduction)
  * [Abstract class with primitive fields](#abstract-class-with-primitive-fields)
* [Closed Polymorphism](#closed-polymorphism)
  * [Static types](#static-types)
  * [Sealed classes](#sealed-classes)
  * [Objects](#objects)

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
  println(tsGenerator.generate(SimpleTypes.serializer().descriptor))
}
```

> You can get the full code [here](./knit/example/example-polymorphic-abstract-class-primitive-fields-01.kt).

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

## Closed Polymorphism

https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/polymorphism.md#closed-polymorphism

### Static types

```kotlin
@Serializable
open class Project(val name: String)

class OwnedProject(name: String, val owner: String) : Project(name)

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(Project.serializer().descriptor))
}
```

> You can get the full code [here](./knit/example/example-polymorphic-static-types-01.kt).

Only the Project class properties are generated.

```typescript
interface Project {
  name: string;
}
```

<!--- TEST -->

```kotlin
@Serializable
abstract class Project {
  abstract val name: String
}

@Serializable
class OwnedProject(override val name: String, val owner: String) : Project()

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(OwnedProject.serializer().descriptor))
}
```

> You can get the full code [here](./knit/example/example-polymorphic-static-types-02.kt).

```typescript
interface Project {
  name: string;
}

interface OwnedProject extends Project {
  name: string;
  owner: string;
}
```

<!--- TEST -->

### Sealed classes

```kotlin
@Serializable
sealed class Project {
  abstract val name: String
}

@Serializable
class OwnedProject(override val name: String, val owner: String) : Project()

@Serializable
class DeprecatedProject(override val name: String, val reason: String) : Project()

fun main() {
  val tsGenerator = KxsTsGenerator()
  println(tsGenerator.generate(OwnedProject.serializer().descriptor))
}
```

> You can get the full code [here](./knit/example/example-polymorphic-sealed-class-01.kt).

```typescript
enum ProjectKind {
  OwnedProject,
  DeprecatedProject,
}

interface Project {
  type: ProjectKind;
}

interface OwnedProject extends Project {
  type: ProjectKind.OwnedProject;
  name: string;
  owner: string;
}

interface DeprecatedProject extends Project {
  type: ProjectKind.DeprecatedProject;
  name: string;
  reason: string;
}
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
    tsGenerator.generate(
      EmptyResponse.serializer().descriptor,
      TextResponse.serializer().descriptor,
    )
  )
}
```

> You can get the full code [here](./knit/example/example-polymorphic-objects-01.kt).

```typescript
export enum ResponseKind {
  EmptyResponse = "EmptyResponse",
  TextResponse = "TextResponse",
}

interface Response {
  type: ResponseKind;
}

interface EmptyResponse extends Response {
  type: ResponseKind.EmptyResponse;
}

interface TextResponse extends Response {
  type: ResponseKind.TextResponse;
  text: string;
}
```

<!--- TEST -->
