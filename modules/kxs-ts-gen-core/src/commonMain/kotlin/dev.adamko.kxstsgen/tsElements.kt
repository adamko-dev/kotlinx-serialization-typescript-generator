package dev.adamko.kxstsgen

import kotlin.jvm.JvmInline


@JvmInline
value class TsElementId(private val id: String) {

  val namespace: String
    get() = id.substringBeforeLast(".")
  val name: String
    get() = id.substringAfterLast(".")
  val filename: String
    get() = namespace.lowercase().replace('.', '-') + ".d.ts"
}

sealed interface TsElement {
  val id: TsElementId
//  val namespace: String
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
  val types: Set<TsTypeReference>,
) : TsElement
//{
//  constructor(id: TsElementId, vararg types: TsElementId)
//    : this(id, types.map { it.id }.toSet())
//}


data class TsTypeReference(
  val id: TsElementId,
  val nullable: Boolean,
)


sealed interface TsProperty {
  val name: String
  val typeReference: TsTypeReference
//  val optional: Boolean

  data class Required(
    override val name: String,
    override val typeReference: TsTypeReference,
  ) : TsProperty
//  {
//    override val optional: Boolean = false
//  }

  data class Optional(
    override val name: String,
    override val typeReference: TsTypeReference,
  ) : TsProperty
//  {
//    override val optional: Boolean = true
//  }
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
    val elementsTsType: TsTypeReference,
  ) : TsStructure

  data class TsMap(
    override val id: TsElementId,
    val keyTsType: TsTypeReference,
    val valueTsType: TsTypeReference,
  ) : TsStructure
}

//@JvmInline
//value class TsProperties(
//  private val properties: List<TsProperty>,
//) {
//  override fun toString(): String = properties.joinToString("\n") { it.output + ";" }
//}

sealed interface TsPolymorphicDiscriminator {
  sealed interface Closed
  sealed interface Open
}
