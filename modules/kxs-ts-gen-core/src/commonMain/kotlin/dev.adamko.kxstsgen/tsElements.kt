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

  data class TsType(
    override val id: TsElementId,
    val typeRefs: Set<TsTypeRef>,
  ) : TsDeclaration {
    constructor(id: TsElementId, typeRef: TsTypeRef, vararg typeRefs: TsTypeRef) :
      this(id, typeRefs.toSet() + typeRef)
  }


  data class TsInterface(
    override val id: TsElementId,
    val properties: Set<TsProperty>,
    val polymorphism: TsPolymorphism?,
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

  data class Declaration(
    val id: TsElementId,
    override val nullable: Boolean,
  ) : TsTypeRef

  /** A property within another declaration (e.g. an enum value, or type within a namespace) */
  data class Property(
    val id: TsElementId,
    val declaration: Declaration,
    override val nullable: Boolean,
  ) : TsTypeRef

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


sealed interface TsPolymorphism {

  val discriminatorName: String
  val subclasses: Set<TsDeclaration.TsInterface>

  data class Sealed(
    override val discriminatorName: String,
    override val subclasses: Set<TsDeclaration.TsInterface>,
  ) : TsPolymorphism

  data class Open(
    override val discriminatorName: String,
    override val subclasses: Set<TsDeclaration.TsInterface>,
  ) : TsPolymorphism

//  object None : TsPolymorphism {
//    override val discriminatorName: Nothing = error("not implemented")
//    override val subclasses: Nothing = error("not implemented")
//  }
}
