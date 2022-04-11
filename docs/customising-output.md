<!--- TEST_NAME CustomisingOutputTest -->


**Table of contents**

<!--- TOC -->

* [Introduction](#introduction)
  * [Overriding output](#overriding-output)

<!--- END -->


<!--- INCLUDE .*\.kt
import kotlinx.serialization.*
import dev.adamko.kxstsgen.*
-->

## Introduction

### Overriding output

If you want to override what KxsTsGen produces, then you can provide overrides.

By default, `Double` is transformed to `number`, but now we want to alias `Double` to `double`.

```kotlin
import kotlinx.serialization.builtins.serializer
import dev.adamko.kxstsgen.core.*

@Serializable
data class Item(
  val price: Double,
  val count: Int,
)

fun main() {
  val tsGenerator = KxsTsGenerator()

  tsGenerator.descriptorOverrides +=
    Double.serializer().descriptor to TsDeclaration.TsTypeAlias(
      id = TsElementId("Double"),
      typeRef = TsTypeRef.Declaration(
        id = TsElementId("double"),
        parent = null,
        nullable = false,
      )
    )

  println(tsGenerator.generate(Item.serializer()))
}
```

> You can get the full code [here](./code/example/example-customising-output-01.kt).

```typescript
export interface Item {
  price: Double;
  count: number;
}

export type Double = double; // assume that 'double' will be provided by another library
```

<!--- TEST TS_COMPILE_OFF -->
