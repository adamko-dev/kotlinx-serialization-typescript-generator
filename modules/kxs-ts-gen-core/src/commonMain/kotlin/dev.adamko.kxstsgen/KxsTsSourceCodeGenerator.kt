package dev.adamko.kxstsgen


/**
 * Writes [TsElement]s as TypeScript source code.
 */
class KxsTsSourceCodeGenerator(
  private val config: KxsTsConfig,
  private val elements: Set<TsElement>,
) {

  private val indent by config::indent

//  private val mapIdToElement: MutableMap<TsElementId, TsDeclaration> =
//    mutableMapOf<TsElementId, TsDeclaration>().withDefault { req ->
//      elements.first { it.id == req }
//    }

  private val typeReferenceState: MutableMap<TsTypeRef, String> = mutableMapOf()

  fun joinElementsToString(): String {

    return elements
      .groupBy {
        when (config.namespaceConfig) {
          is KxsTsConfig.NamespaceConfig.Static            -> config.namespaceConfig.namespace
          KxsTsConfig.NamespaceConfig.Disabled             -> ""
          KxsTsConfig.NamespaceConfig.DescriptorNamePrefix -> "it.id.namespace"
        }
      }
      .mapValues { (_, elements) ->
        elements
          .filterIsInstance<TsDeclaration>()
          .joinToString(config.structureSeparator) { declaration ->
            when (declaration) {
              is TsDeclaration.TsEnum      -> generateEnum(declaration)
              is TsDeclaration.TsInterface -> generateInterface(declaration)
              is TsDeclaration.TsTypeAlias -> generateTypeAlias(declaration)
            }
          }
      }.entries
      .filter { it.value.isNotBlank() }
      .joinToString(config.structureSeparator) { (namespace, namespaceContent) ->

        if (namespace.isBlank()) {
          namespaceContent
        } else {
          """
            |export namespace $namespace {
            |${namespaceContent.prependIndent(config.indent)}
            |}
          """.trimMargin()
        }
      }
  }

  private fun generateEnum(enum: TsDeclaration.TsEnum): String {

    val enumMembers = enum.members.joinToString("\n") { member ->
      """
        |${indent}$member = "$member",
      """.trimMargin()
    }

    return """
      |export enum ${enum.id.name} {
      |${enumMembers}
      |}
    """.trimMargin()
  }

  private fun generateInterface(element: TsDeclaration.TsInterface): String {

    val properties = element
      .properties
      .joinToString(separator = "\n") { property ->
        val separator = when (property) {
          is TsProperty.Optional -> "?: "
          is TsProperty.Required -> ": "
        }
        val propertyType = generateTypeReference(property.typeRef)
        // generate `  name: Type;`
        // or       `  name:? Type;`
        "${indent}${property.name}${separator}${propertyType};"
      }

    return buildString {
      appendLine("interface ${element.id.name} {")
      if (properties.isNotBlank()) {
        appendLine(properties)
      }
      append("}")
    }
  }

//  private fun generateSealedSubInterfaces(sealed: TsPolymorphicDiscriminator.Sealed): String {
//
//    val enumDiscriminator = generateEnum(sealed.discriminator)
//
//    val sealedType = sealed.children.map { it }
//  }

  private fun generateTypeAlias(element: TsDeclaration.TsTypeAlias): String {
    val aliases = generateTypeReference(element.typeRef)

    return when (config.typeAliasTyping) {
      KxsTsConfig.TypeAliasTypingConfig.None        ->
        """
          |type ${element.id.name} = ${aliases};
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
          |type ${element.id.name} = $aliases & { __${brandType}__: void };
        """.trimMargin()
      }
    }
  }


  /**
   * A type-reference, be it for the field of an interface, a type alias, or a generic type
   * constraint.
   */
  private fun generateTypeReference(typeRef: TsTypeRef): String {
    return typeReferenceState.getOrPut(typeRef) {
      val plainType: String = when (typeRef) {
        is TsTypeRef.Literal -> when (typeRef.element) {
          is TsLiteral.Primitive -> primitiveReference(typeRef.element)
          is TsLiteral.TsList    -> {
            val valueTypeRef = generateTypeReference(typeRef.element.valueTypeRef)
            "$valueTypeRef[]"
          }
          is TsLiteral.TsMap     -> generateMapTypeReference(typeRef.element)
        }
        is TsTypeRef.Named   -> typeRef.id.name
        is TsTypeRef.Unknown -> generateTypeReference(typeRef.ref)
      }

      buildString {
        append(plainType)
        if (typeRef.nullable) {
          append(" | ")
          append(primitiveReference(TsLiteral.Primitive.TsNull))
        }
      }
    }
  }


  private fun primitiveReference(primitive: TsLiteral.Primitive): String {
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


  private fun generateMapTypeReference(tsMap: TsLiteral.TsMap): String {
    val keyTypeRef = generateTypeReference(tsMap.keyTypeRef)
    val valueTypeRef = generateTypeReference(tsMap.valueTypeRef)

    return when (tsMap.type) {
      TsLiteral.TsMap.Type.INDEX_SIGNATURE -> "{ [key: $keyTypeRef]: $valueTypeRef }"
      TsLiteral.TsMap.Type.MAPPED_OBJECT   -> "{ [key in $keyTypeRef]: $valueTypeRef }"
      TsLiteral.TsMap.Type.MAP             -> "Map<$keyTypeRef, $valueTypeRef>"
    }
  }

}
