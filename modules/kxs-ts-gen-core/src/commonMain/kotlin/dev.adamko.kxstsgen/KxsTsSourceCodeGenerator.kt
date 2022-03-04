package dev.adamko.kxstsgen


class KxsTsSourceCodeGenerator(
  private val config: KxsTsConfig
) {

  private val indent by config::indent

  fun joinElementsToString(elements: Set<TsElement>): String {

    return elements
      .groupBy {
        when (config.namespaceConfig) {
          is KxsTsConfig.NamespaceConfig.Static               -> config.namespaceConfig.namespace
          KxsTsConfig.NamespaceConfig.Disabled                -> ""
          KxsTsConfig.NamespaceConfig.UseDescriptorNamePrefix -> it.id.namespace
        }
      }
      .mapValues { (_, elements) ->

        elements.mapNotNull { element ->
          when (element) {
            is TsStructure.TsMap,
            is TsStructure.TsList,
            is TsPrimitive             -> null
            is TsStructure.TsEnum      -> generateEnum(element)
            is TsStructure.TsInterface -> generateInterface(element)
            is TsTypeAlias             -> generateTypeAlias(element)
          }
        }
          .joinToString("\n\n")

      }.entries
      .filter { it.value.isNotBlank() }
      .joinToString("\n\n") { (namespace, namespaceContent) ->

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

  private fun generateEnum(enum: TsStructure.TsEnum): String {

    val enumMembers = enum.members.joinToString("\n") { member ->
      """
        |${indent}$member = "$member",
      """.trimMargin()
    }

    return """
      |enum ${enum.id.name} {
      |${enumMembers}
      |}
    """.trimMargin()
  }

  private fun generateInterface(element: TsStructure.TsInterface): String {

    val properties = element.properties.joinToString("\n") { property ->
      val separator = when (property) {
        is TsProperty.Optional -> "?: "
        is TsProperty.Required -> ": "
      }
      val propertyType = generateTypeReference(property.typing)
      // generate `  name: Type;`
      // or       `  name:? Type;`
      "${indent}${property.name}${separator}${propertyType};"
    }

    return """
      |interface ${element.id.name} {
      |${properties}
      |}
    """.trimMargin()
  }

  private fun generateTypeAlias(element: TsTypeAlias): String {
    val aliases = generateTypeReference(element.type)

    return when (config.typeAliasTyping) {
      KxsTsConfig.TypeAliasTypingConfig.None        ->
        """
          |type ${element.id.name} = ${aliases};
        """.trimMargin()
      KxsTsConfig.TypeAliasTypingConfig.BrandTyping -> {

        val brandType = element.id.name
          .replace(".", "_")
          .filter { it.isLetter() || it == '_' }

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
  private fun generateTypeReference(typing: TsTyping): String {
    return buildString {
      when (typing.type) {
        is TsStructure.TsEnum,
        is TsStructure.TsInterface,
        is TsTypeAlias,
        is TsPrimitive        -> {
          append(typing.type.id.name)
          if (typing.nullable) {
            append(" | " + TsPrimitive.TsNull.id.name)
          }
        }
        is TsStructure.TsList -> {
          append(generateTypeReference(typing.type.elementsTyping))
          append("[]")
        }
        is TsStructure.TsMap  -> {
          val keyTypeRef = generateTypeReference(typing.type.keyTyping)
          val valueTypeRef = generateTypeReference(typing.type.valueTyping)
          append("{ [key: $keyTypeRef]: $valueTypeRef }")
        }
      }
    }
  }
}
