<!--- TEST_NAME TsExportFormatTest -->


**Table of contents**

<!--- TOC -->

* [Introduction](#introduction)
  * [Tuple](#tuple)

<!--- END -->


<!--- INCLUDE .*\.kt
import kotlinx.serialization.*
import dev.adamko.kxstsgen.*
-->

## Introduction

### Tuple

```kotlin
@Serializable
@TsExport(format = TsExport.Format.TUPLE)
class SimpleTypes(
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

> You can get the full code [here](./code/example/example-format-tuple-01.kt).

```typescript
export type SimpleTypes = [string, number, number, boolean, string];
```

<!--- TEST -->
