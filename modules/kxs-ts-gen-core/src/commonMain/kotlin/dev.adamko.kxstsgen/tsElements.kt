package dev.adamko.kxstsgen

import dev.adamko.kxstsgen.TsProperty.Optional
import dev.adamko.kxstsgen.TsProperty.Required
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


  /** A named reference to one or more other types. */
  data class TsTypeAlias(
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

}


/**
 * A reference to some [TsElement]. The reference may be [nullable].
 *
 * A reference does not require the target to be generated.
 * This helps prevent circular dependencies causing a lock.
 */
sealed interface TsTypeRef {
  val nullable: Boolean


  data class Literal(val element: TsLiteral, override val nullable: Boolean) : TsTypeRef


  data class Declaration(
    val id: TsElementId,
    val parent: Declaration?,
    override val nullable: Boolean,
  ) : TsTypeRef

}


/**
 * A property within an [interface][TsDeclaration.TsInterface]
 *
 *  In  property may be [Required] or [Optional]. See the TypeScript docs:
 *  ['Optional Properties'](https://www.typescriptlang.org/docs/handbook/2/objects.html#optional-properties)
 */
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


/**
 * Meta-data about the polymorphism of a [TsDeclaration.TsInterface].
 */
sealed interface TsPolymorphism {

  /** The name of the field used to discriminate between [subclasses]. */
  val discriminatorName: String
  val subclasses: Set<TsDeclaration.TsInterface>


  data class Sealed(
    override val discriminatorName: String,
    override val subclasses: Set<TsDeclaration.TsInterface>,
  ) : TsPolymorphism


  /** Note: [Open] is not implemented correctly */
  data class Open(
    override val discriminatorName: String,
    override val subclasses: Set<TsDeclaration.TsInterface>,
  ) : TsPolymorphism
}
