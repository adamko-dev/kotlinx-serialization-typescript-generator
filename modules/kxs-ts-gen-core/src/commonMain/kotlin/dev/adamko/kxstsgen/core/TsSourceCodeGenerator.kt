package dev.adamko.kxstsgen.core

import dev.adamko.kxstsgen.KxsTsConfig


/**
 * Writes [TsElement]s as TypeScript source code.
 */
abstract class TsSourceCodeGenerator(
  val config: KxsTsConfig = KxsTsConfig(),
) {

  abstract fun groupElementsBy(element: TsElement): String?

  open fun generateDeclaration(element: TsDeclaration): String {
    return when (element) {
      is TsDeclaration.TsEnum      -> generateEnum(element)
      is TsDeclaration.TsInterface -> generateInterface(element)
      is TsDeclaration.TsNamespace -> generateNamespace(element)
      is TsDeclaration.TsTypeUnion -> generateTypeUnion(element)
      is TsDeclaration.TsTypeAlias -> generateTypeAlias(element)
      is TsDeclaration.TsTuple     -> generateTuple(element)
    }
  }

  abstract fun generateEnum(enum: TsDeclaration.TsEnum): String
  abstract fun generateInterface(element: TsDeclaration.TsInterface): String
  abstract fun generateNamespace(namespace: TsDeclaration.TsNamespace): String
  abstract fun generateTypeAlias(element: TsDeclaration.TsTypeAlias): String
  abstract fun generateTypeUnion(element: TsDeclaration.TsTypeUnion): String
  abstract fun generateTuple(tuple: TsDeclaration.TsTuple): String

  abstract fun generateMapTypeReference(tsMap: TsLiteral.TsMap): String

  abstract fun generatePrimitive(primitive: TsLiteral.Primitive): String
  abstract fun generateTypeReference(typeRef: TsTypeRef): String

  open class Default(
    config: KxsTsConfig,
  ) : TsSourceCodeGenerator(config) {


    override fun groupElementsBy(element: TsElement): String {
      return when (config.namespaceConfig) {
        is KxsTsConfig.NamespaceConfig.Static            -> config.namespaceConfig.namespace
        KxsTsConfig.NamespaceConfig.Disabled             -> ""
        KxsTsConfig.NamespaceConfig.DescriptorNamePrefix ->
          when (element) {
            is TsLiteral     -> ""
            is TsDeclaration -> element.id.namespace
          }
      }
    }


    override fun generateNamespace(namespace: TsDeclaration.TsNamespace): String {
      val namespaceContent =
        namespace
          .members
          .joinToString(config.declarationSeparator) { declaration ->
            generateDeclaration(declaration)
          }

      return buildString {
        appendLine("export namespace ${namespace.id.name} {")
        if (namespaceContent.isNotBlank()) {
          appendLine(namespaceContent.prependIndent(config.indent))
        }
        append("}")
      }
    }


    override fun generateEnum(enum: TsDeclaration.TsEnum): String {

      val enumMembers = enum.members.joinToString("\n") { member ->
        """
          |${member.name} = "${generateTypeReference(member.typeRef)}",
        """.trimMargin()
          .prependIndent(config.indent)
      }

      return """
        |export enum ${enum.id.name} {
        |${enumMembers}
        |}
      """.trimMargin()
    }


    override fun generateInterface(element: TsDeclaration.TsInterface): String {

      val properties = element.properties
        .joinToString(separator = "\n") { generateInterfaceProperty(it) }

      return buildString {
        appendLine("export interface ${element.id.name} {")
        if (properties.isNotBlank()) {
          appendLine(properties.prependIndent(config.indent))
        }
        append("}")
      }
    }


    /**
     * Generate
     * ```typescript
     * name: Type;
     * ```
     * or
     * ```typescript
     * name:? Type;
     * ```
     */
    open fun generateInterfaceProperty(
      property: TsProperty
    ): String {
      val separator = if (property.optional) "?: " else ": "
      val propertyType = generateTypeReference(property.typeRef)
      return "${property.name}${separator}${propertyType};"
    }


    override fun generateTypeAlias(element: TsDeclaration.TsTypeAlias): String {
      val aliasedRef = generateTypeReference(element.typeRef)

      return when (config.typeAliasTyping) {
        KxsTsConfig.TypeAliasTypingConfig.None        ->
          """
            |export type ${element.id.name} = ${aliasedRef};
          """.trimMargin()
        KxsTsConfig.TypeAliasTypingConfig.BrandTyping -> {

          val brandType = element.id.name
            .mapNotNull { c ->
              when {
                c == '.'      -> '_'
                !c.isLetter() -> null
                else          -> c
              }
            }.joinToString("")

          """
            |export type ${element.id.name} = $aliasedRef & { __${brandType}__: void };
          """.trimMargin()
        }
      }
    }


    override fun generateTypeUnion(element: TsDeclaration.TsTypeUnion): String {
      return if (element.typeRefs.isEmpty()) {
        """
          |export type ${element.id.name} = ${generatePrimitive(TsLiteral.Primitive.TsUnknown)};
        """.trimMargin()
      } else {
        val aliases = element.typeRefs
          .map { "${config.indent}| ${generateTypeReference(it)}" }
          .sorted()
          .joinToString("\n")

        """
          ¦export type ${element.id.name} =
          ¦$aliases;
        """.trimMargin("¦")
      }
    }


    override fun generateTuple(tuple: TsDeclaration.TsTuple): String {

      val types = tuple.elements.joinToString(separator = "\n") { property ->
        val optionalMarker = if (property.optional) "?" else ""
        val typeRef = generateTypeReference(property.typeRef)
        "${config.indent}${property.name}: $typeRef$optionalMarker,"
      }

      return """
        ¦export type ${tuple.id.name} = [
        ¦$types
        ¦];
      """.trimMargin("¦")
    }


    /**
     * A type-reference, be it for the field of an interface, a type alias, or a generic type
     * constraint.
     */
    override fun generateTypeReference(
      typeRef: TsTypeRef,
    ): String {
      val plainType: String = when (typeRef) {
        is TsTypeRef.Literal     -> when (typeRef.element) {
          is TsLiteral.Primitive -> generatePrimitive(typeRef.element)

          is TsLiteral.TsList    -> {
            val valueTypeRef = generateTypeReference(typeRef.element.valueTypeRef)
            "$valueTypeRef[]"
          }

          is TsLiteral.TsMap     -> generateMapTypeReference(typeRef.element)

          is TsLiteral.Custom    -> typeRef.element.value
        }

        is TsTypeRef.Declaration -> {

          if (typeRef.parent != null) {
            val parentRef = generateTypeReference(typeRef.parent)
            "${parentRef}.${typeRef.id.name}"
          } else {
            typeRef.id.name
          }
        }
      }

      return buildString {
        append(plainType)
        if (typeRef.nullable) {
          append(" | ")
          append(generatePrimitive(TsLiteral.Primitive.TsNull))
        }
      }
    }


    override fun generatePrimitive(primitive: TsLiteral.Primitive): String {
      return when (primitive) {
        TsLiteral.Primitive.TsString    -> "string"
        TsLiteral.Primitive.TsNumber    -> "number"
        TsLiteral.Primitive.TsBoolean   -> "boolean"
        TsLiteral.Primitive.TsObject    -> "object"
        TsLiteral.Primitive.TsAny       -> "any"
        TsLiteral.Primitive.TsNever     -> "never"
        TsLiteral.Primitive.TsNull      -> "null"
        TsLiteral.Primitive.TsUndefined -> "undefined"
        TsLiteral.Primitive.TsUnknown   -> "unknown"
        TsLiteral.Primitive.TsVoid      -> "void"
      }
    }


    override fun generateMapTypeReference(
      tsMap: TsLiteral.TsMap
    ): String {
      val keyTypeRef = generateTypeReference(tsMap.keyTypeRef)
      val valueTypeRef = generateTypeReference(tsMap.valueTypeRef)

      return when (tsMap.type) {
        TsLiteral.TsMap.Type.INDEX_SIGNATURE -> "{ [key: $keyTypeRef]: $valueTypeRef }"
        TsLiteral.TsMap.Type.MAPPED_OBJECT   -> "{ [key in $keyTypeRef]: $valueTypeRef }"
        TsLiteral.TsMap.Type.MAP             -> "Map<$keyTypeRef, $valueTypeRef>"
      }
    }
  }
}
