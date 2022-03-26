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


/*
//fun TsTypeRef.toTsTypeParamConstant(): TsTypeParam.Constant = TsTypeParam.Constant(this)

///**
// * A generic type parameter, either constant or generic.
// *
// * The output depends on whether it is used in a [TsStructure.TsInterface] or [TsProperty].
// *
// * ```typescript
// * // T is 'TsTypeParam.Generic'
// * interface Box<T> { value: T; }
// * interface Pet<T: Animal> { pet: T; }
// * interface NamedBox<T extends Named> { namedValue: T; }
// * interface NamedPetBox<T: Animal> { pet: T; }
// *
// * // 'string' and 'Dog' are 'TsTypeParam.Constant'
// * interface Person { aliases: string[] }
// * interface DogBox extends PetBox<Dog> { override pet: Dog }
// * ```
// */
//// source: introspection
//sealed interface TsTypeParam {
//
//  data class Generic(
//    val name: String,
//    val constraints: Set<TsTypeParam>? = null
//  ) : TsTypeParam
//
//  data class Constant(
//    val typeRef: TsTypeRef
//  ) : TsTypeParam
//
//}


///** Defines how many generic types can be accepted. */
//// source: introspection
//sealed interface TsGenericSlot {
//
//  /** Enums, primitives, properties can't have generic slots. */
//  object None : TsGenericSlot
//
//  /** A single generic type, e.g. `List<V>` */
//  data class Value(
//    val valueTyping: TsTypeParam,
//  ) : TsGenericSlot
//
//  /** The generic types of a `Map<K, V>` */
//  data class KeyValue(
//    val keyTyping: TsTypeParam,
//    val valueTyping: TsTypeParam,
//  ) : TsGenericSlot
//
//  /** Can have zero-to-many [TsTypeParam]s. */
//  data class Multiple(
//    val typings: Set<TsTypeParam>,
//  ) : TsGenericSlot
//
//}
*/
