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
      is TsDeclaration.TsType      -> generateType(element)
    }
  }

  abstract fun generateEnum(enum: TsDeclaration.TsEnum): String
  abstract fun generateInterface(element: TsDeclaration.TsInterface): String
  abstract fun generateNamespace(namespace: TsDeclaration.TsNamespace): String
  abstract fun generateType(element: TsDeclaration.TsType): String

  abstract fun generateMapTypeReference(
//    requestorId: TsElementId?,
    tsMap: TsLiteral.TsMap,
  ): String

  abstract fun generatePrimitive(primitive: TsLiteral.Primitive): String
  abstract fun generateTypeReference(
//    rootId: TsElementId?,
    typeRef: TsTypeRef,
  ): String

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

      return when (element.polymorphism) {
        is TsPolymorphism.Sealed -> generatePolyClosed(element, element.polymorphism)
        is TsPolymorphism.Open   -> generatePolyOpen(element, element.polymorphism)
        null                     -> {
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

          buildString {
            appendLine("export interface ${element.id.name} {")
            if (properties.isNotBlank()) {
              appendLine(properties.prependIndent(config.indent))
            }
            append("}")
          }
        }
      }
    }

    private fun generatePolyOpen(
      element: TsDeclaration.TsInterface,
      polymorphism: TsPolymorphism.Open,
    ): String {
      val namespaceId = element.id
      val namespaceRef = TsTypeRef.Declaration(namespaceId, null, false)

      val subInterfaceRefs = polymorphism.subclasses.map {
        TsTypeRef.Declaration(it.id, namespaceRef, false)
      }.toSet()

      val discriminatorProperty = TsProperty.Required(
        polymorphism.discriminatorName,
        TsTypeRef.Literal(TsLiteral.Primitive.TsString, false),
      )

      val subInterfaces = polymorphism
        .subclasses
        .map { it.copy(properties = it.properties + discriminatorProperty) }
        .toSet()

      val namespace = TsDeclaration.TsNamespace(
        element.id,
        subInterfaces,
      )

      val subInterfaceTypeUnion = TsDeclaration.TsType(
        element.id,
        subInterfaceRefs
      )

      return listOf(subInterfaceTypeUnion, namespace).joinToString("\n\n") {
        generateDeclaration(it)
      }
    }

    private fun generatePolyClosed(
      element: TsDeclaration.TsInterface,
      polymorphism: TsPolymorphism.Sealed,
    ): String {
      val namespaceId = element.id
      val namespaceRef = TsTypeRef.Declaration(namespaceId, null, false)

      val subInterfaceRefs: Map<TsTypeRef.Declaration, TsDeclaration.TsInterface> =
        polymorphism.subclasses.associateBy { subclass ->
          val subclassId = TsElementId(namespaceId.toString() + "." + subclass.id.name)
          TsTypeRef.Declaration(subclassId, namespaceRef, false)
        }

      val discriminatorEnum = TsDeclaration.TsEnum(
        TsElementId("${element.id.namespace}.${polymorphism.discriminatorName.replaceFirstChar { it.uppercaseChar() }}"),
        subInterfaceRefs.keys.map { it.id.name }.toSet(),
      )

      val discriminatorEnumRef = TsTypeRef.Declaration(discriminatorEnum.id, namespaceRef, false)

      val subInterfacesWithTypeProp = subInterfaceRefs.map { (subInterfaceRef, subclass) ->
        val typePropId = TsElementId(
          """
            |${discriminatorEnum.id.name}.${subInterfaceRef.id.name}
          """.trimMargin()
        )

        val typeProp = TsProperty.Required(
          polymorphism.discriminatorName,
          TsTypeRef.Declaration(typePropId, discriminatorEnumRef, false),
        )

        subclass.copy(properties = setOf(typeProp) + subclass.properties)
      }

      val subInterfaceTypeUnion = TsDeclaration.TsType(
        element.id,
        subInterfaceRefs.keys
      )

      val namespace = TsDeclaration.TsNamespace(
        namespaceId,
        buildSet {
          add(discriminatorEnum)
          addAll(subInterfacesWithTypeProp)
        }
      )

      return listOf(subInterfaceTypeUnion, namespace).joinToString("\n\n") {
        generateDeclaration(it)
      }
    }


    override fun generateType(element: TsDeclaration.TsType): String {
      val aliases =
        element.typeRefs
          .map { generateTypeReference(it) }
          .sorted()
          .joinToString(" | ")

      return when (config.typeAliasTyping) {
        KxsTsConfig.TypeAliasTypingConfig.None        ->
          """
            |export type ${element.id.name} = ${aliases};
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
            |export type ${element.id.name} = $aliases & { __${brandType}__: void };
          """.trimMargin()
        }
      }
    }


    /**
     * A type-reference, be it for the field of an interface, a type alias, or a generic type
     * constraint.
     */
    override fun generateTypeReference(
//      rootId: TsElementId?,
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
        }

        is TsTypeRef.Declaration -> {

          if (typeRef.parent != null) {
            val parentRef = generateTypeReference(typeRef.parent)
            "${parentRef}.${typeRef.id.name}"
          } else {
            typeRef.id.name
          }
        }

        is TsTypeRef.Unknown     -> generateTypeReference(typeRef.ref)
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
//      rootId: TsElementId?,
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
