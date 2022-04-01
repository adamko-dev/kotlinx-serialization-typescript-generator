package dev.adamko.kxstsgen


/**
 * Writes [TsElement]s as TypeScript source code.
 */
abstract class KxsTsSourceCodeGenerator(
  val config: KxsTsConfig,
  val context: KxsTsConvertorContext,
) {

  abstract fun groupElementsBy(element: TsElement): String?

  open fun generateDeclaration(element: TsDeclaration): String {
    return when (element) {
      is TsDeclaration.TsEnum      -> generateEnum(element)
      is TsDeclaration.TsInterface -> generateInterface(element)
      is TsDeclaration.TsNamespace -> generateNamespace(element)
      is TsDeclaration.TsTypeAlias -> generateTypeAlias(element)
    }
  }

  abstract fun generateEnum(enum: TsDeclaration.TsEnum): String
  abstract fun generateInterface(element: TsDeclaration.TsInterface): String
  abstract fun generateNamespace(namespace: TsDeclaration.TsNamespace): String
  abstract fun generateTypeAlias(element: TsDeclaration.TsTypeAlias): String

  abstract fun generateMapTypeReference(tsMap: TsLiteral.TsMap): String
  abstract fun generatePrimitive(primitive: TsLiteral.Primitive): String
  abstract fun generateTypeReference(typeRef: TsTypeRef): String

  open class Default(
    config: KxsTsConfig,
    context: KxsTsConvertorContext,
  ) : KxsTsSourceCodeGenerator(config, context) {


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
          .joinToString(config.structureSeparator) { declaration ->
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
          |${config.indent}$member = "$member",
        """.trimMargin()
      }

      return """
        |export enum ${enum.id.name} {
        |${enumMembers}
        |}
      """.trimMargin()
    }

    override fun generateInterface(element: TsDeclaration.TsInterface): String {

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
          "${property.name}${separator}${propertyType};"
        }

      return buildString {
        appendLine("interface ${element.id.name} {")
        if (properties.isNotBlank()) {
          appendLine(properties.prependIndent(config.indent))
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

    override fun generateTypeAlias(element: TsDeclaration.TsTypeAlias): String {
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
    override fun generateTypeReference(typeRef: TsTypeRef): String {
      val plainType: String = when (typeRef) {
        is TsTypeRef.Literal -> when (typeRef.element) {
          is TsLiteral.Primitive -> generatePrimitive(typeRef.element)
          is TsLiteral.TsList    -> {
            val valueTypeRef = generateTypeReference(typeRef.element.valueTypeRef)
            "$valueTypeRef[]"
          }
          is TsLiteral.TsMap     -> generateMapTypeReference(typeRef.element)
        }
        is TsTypeRef.Named   -> typeRef.id.name
        is TsTypeRef.Unknown -> generateTypeReference(typeRef.ref)
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


    override fun generateMapTypeReference(tsMap: TsLiteral.TsMap): String {
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
