package dev.adamko.kxstsgen

import kotlin.jvm.JvmInline


@JvmInline
value class TsElementId(private val id: String) {
  val namespace: String
    get() = id.substringBeforeLast(".")
  val name: String
    get() = id.substringAfterLast(".")
}


sealed interface TsElement {
  val id: TsElementId
}


sealed class TsPrimitive(
  type: String
) : TsElement {
  final override val id: TsElementId = TsElementId(type)

  object TsString : TsPrimitive("string")
  object TsNumber : TsPrimitive("number")
  object TsBoolean : TsPrimitive("boolean")

  object TsObject : TsPrimitive("object")

  object TsAny : TsPrimitive("any")
  object TsNever : TsPrimitive("never")
  object TsNull : TsPrimitive("null")
  object TsUndefined : TsPrimitive("undefined")
  object TsUnknown : TsPrimitive("unknown")
  object TsVoid : TsPrimitive("void")
}


data class TsTypeAlias(
  override val id: TsElementId,
  val type: TsTyping,
) : TsElement


data class TsTyping(
  val type: TsElement,
  val nullable: Boolean,
)


sealed interface TsProperty {
  val name: String
  val typing: TsTyping

  data class Required(
    override val name: String,
    override val typing: TsTyping,
  ) : TsProperty

  data class Optional(
    override val name: String,
    override val typing: TsTyping,
  ) : TsProperty
}


sealed interface TsStructure : TsElement {

  data class TsInterface(
    override val id: TsElementId,
    val properties: List<TsProperty>,
  ) : TsStructure

  data class TsEnum(
    override val id: TsElementId,
    val members: Set<String>
  ) : TsStructure

  data class TsList(
    override val id: TsElementId,
    val elementsTyping: TsTyping,
  ) : TsStructure

  data class TsMap(
    override val id: TsElementId,
    val keyTyping: TsTyping,
    val valueTyping: TsTyping,
  ) : TsStructure
}


sealed interface TsPolymorphicDiscriminator {
  sealed interface Closed
  sealed interface Open
}
