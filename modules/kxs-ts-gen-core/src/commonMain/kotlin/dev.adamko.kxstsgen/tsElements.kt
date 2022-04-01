package dev.adamko.kxstsgen

import kotlin.jvm.JvmInline


@JvmInline
value class TsElementId(private val id: String) {
  val namespace: String
    get() = id.substringBeforeLast(".")
  val name: String
    get() = id.substringAfterLast(".")
}


sealed interface TsElement


sealed interface TsDeclaration : TsElement {
  val id: TsElementId

  data class TsTypeAlias(
    override val id: TsElementId,
    val typeRef: TsTypeRef,
  ) : TsDeclaration


  data class TsInterface(
    override val id: TsElementId,
    val properties: Set<TsProperty>,
    val polymorphism: TsPolymorphicDiscriminator,
  ) : TsDeclaration


  data class TsEnum(
    override val id: TsElementId,
    val members: Set<String>,
  ) : TsDeclaration


  data class TsNamespace(
    override val id: TsElementId,
    val members: Set<TsDeclaration>,
  ) : TsDeclaration

}


sealed interface TsLiteral : TsElement {

  sealed interface Primitive : TsLiteral {

    object TsString : Primitive
    object TsNumber : Primitive
    object TsBoolean : Primitive

    object TsObject : Primitive

    object TsAny : Primitive
    object TsNever : Primitive
    object TsNull : Primitive
    object TsUndefined : Primitive
    object TsUnknown : Primitive
    object TsVoid : Primitive
  }

  data class TsList(
    val valueTypeRef: TsTypeRef,
  ) : TsLiteral

  data class TsMap(
    val keyTypeRef: TsTypeRef,
    val valueTypeRef: TsTypeRef,
    val type: Type,
  ) : TsLiteral {
    enum class Type {
      INDEX_SIGNATURE,
      MAPPED_OBJECT,
      MAP,
    }
  }

}


sealed interface TsTypeRef {
  val nullable: Boolean

  data class Literal(val element: TsLiteral, override val nullable: Boolean) : TsTypeRef

  data class Named(val id: TsElementId, override val nullable: Boolean) : TsTypeRef

  object Unknown : TsTypeRef {
    val ref: Literal = Literal(TsLiteral.Primitive.TsUnknown, false)
    override val nullable: Boolean by ref::nullable
  }

}


// source: kxs
sealed interface TsProperty {
  val name: String
  val typeRef: TsTypeRef

  data class Required(
    override val name: String,
    override val typeRef: TsTypeRef,
  ) : TsProperty

  data class Optional(
    override val name: String,
    override val typeRef: TsTypeRef,
  ) : TsProperty
}


// source: kxs & introspection
sealed interface TsPolymorphicDiscriminator {
  // source: introspection
  data class Sealed(
    // source: kxs
    val discriminator: TsDeclaration.TsEnum,
    // source: introspection
    val children: Set<TsDeclaration.TsInterface> = setOf(),
  ) : TsPolymorphicDiscriminator

  // source: introspection
  object Open : TsPolymorphicDiscriminator
}
