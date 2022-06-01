package dev.adamko.kxstsgen.core

import kotlin.jvm.JvmInline
import kotlinx.serialization.descriptors.SerialDescriptor


/**
 * A unique identifier for a [TsElement].
 *
 * This is usually generated from [SerialDescriptor.serialName].
 */
// Note: A value class probably isn't the best choice here. The manual String manipulation is
// restrictive and clunky, and makes nested references very difficult to decipher.
@JvmInline
value class TsElementId(private val id: String) {
  val namespace: String
    get() = id.substringBeforeLast(".")
  val name: String
    get() = id.substringAfterLast(".")

  override fun toString(): String = id
}

/**
 * Some TypeScript source code element. Either a [TsLiteral] or a [TsDeclaration].
 */
sealed interface TsElement


/**
 * Declarations are named elements that developers create in TypeScript source code.
 *
 * For example, an [interface][TsDeclaration.TsInterface] is declared. In contrast, the interface
 * may have a [string][TsLiteral.Primitive.TsString] property, which is represented as a
 * [TsLiteral].
 */
sealed interface TsDeclaration : TsElement {
  val id: TsElementId


  /** A named reference to another type. */
  data class TsTypeAlias(
    override val id: TsElementId,
    val typeRef: TsTypeRef,
  ) : TsDeclaration


  /** A named reference to one or more other types. */
  data class TsTypeUnion(
    override val id: TsElementId,
    val typeRefs: Set<TsTypeRef>,
  ) : TsDeclaration


  /**  A [tuple type](https://www.typescriptlang.org/docs/handbook/2/objects.html#tuple-types). */
  data class TsTuple(
    override val id: TsElementId,
    val elements: Set<TsProperty>,
  ) : TsDeclaration


  data class TsInterface(
    override val id: TsElementId,
    val properties: Set<TsProperty>,
  ) : TsDeclaration


  data class TsEnum(
    override val id: TsElementId,
    val members: Set<TsProperty>,
  ) : TsDeclaration


  data class TsNamespace(
    override val id: TsElementId,
    val members: Set<TsDeclaration>,
  ) : TsDeclaration

}


/** Literal built-in TypeScript elements. */
sealed interface TsLiteral : TsElement {

  sealed interface Primitive : TsLiteral {

    object TsString : Primitive
    object TsNumber : Primitive
    object TsBoolean : Primitive

    object TsObject : Primitive

    object TsAny : Primitive

    // the remaining primitives are defined, but unused
    object TsNever : Primitive
    object TsNull : Primitive
    object TsUndefined : Primitive
    object TsUnknown : Primitive
    object TsVoid : Primitive
  }


  /** A list with elements of type [valueTypeRef]. */
  data class TsList(
    val valueTypeRef: TsTypeRef,
  ) : TsLiteral


  /** A key-value map. */
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

  /** https://www.typescriptlang.org/docs/handbook/2/everyday-types.html#literal-types */
  @JvmInline
  value class Custom(val value: String) : TsLiteral
}


/**
 * A reference to some [TsElement]. The reference may be [nullable].
 *
 * A reference does not require the target to be generated.
 * This helps prevent circular dependencies causing a lock.
 */
sealed interface TsTypeRef {

  /**
   * Defines whether this reference may 'unset', and set to `null`.
   *
   * Nullability is different to optionality, which is determined by [TsProperty.optional].
   */
  val nullable: Boolean


  data class Literal(
    val element: TsLiteral,
    override val nullable: Boolean,
  ) : TsTypeRef


  data class Declaration(
    val id: TsElementId,
    val parent: Declaration?,
    override val nullable: Boolean,
  ) : TsTypeRef

}


/**
 * A property within an
 * [interface][TsDeclaration.TsInterface],
 * [tuple][TsDeclaration.TsTuple],
 * or [enum][TsDeclaration.TsEnum].
 */
data class TsProperty(
  val name: String,
  val typeRef: TsTypeRef,
  /**
   * A property may be required or optional. See the TypeScript docs:
   * ['Optional Properties'](https://www.typescriptlang.org/docs/handbook/2/objects.html#optional-properties)
   *
   * Optionality is different to nullability, which is defined by [TsTypeRef.nullable].
   */
  val optional: Boolean,
)
