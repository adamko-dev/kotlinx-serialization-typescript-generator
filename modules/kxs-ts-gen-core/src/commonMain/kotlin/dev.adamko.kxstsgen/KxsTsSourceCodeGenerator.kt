package dev.adamko.kxstsgen


class KxsTsSourceCodeGenerator(
  private val config: KxsTsConfig
) {

  private val indent by config::indent

  fun joinElementsToString(elements: Set<TsElement>): String {

    return elements
      .groupBy {
        when (config.namespaceConfig) {
          is KxsTsConfig.NamespaceConfig.Hardcoded            -> config.namespaceConfig.namespace
          KxsTsConfig.NamespaceConfig.None                    -> ""
          KxsTsConfig.NamespaceConfig.UseDescriptorNamePrefix -> it.id.namespace
        }
      }
      .mapValues { (_, elements) ->

        elements.mapNotNull { element ->
          when (element) {
            is TsPrimitive             -> null
            is TsStructure.TsEnum      -> generateEnum(element)
            is TsStructure.TsInterface -> generateInterface(element)
            is TsStructure.TsMap       -> generateMap(element)
            is TsStructure.TsList      -> generateList(element)
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
      val propertyType = generateTypeReference(property.typeReference)
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
    val aliases = generateTypeReference(element.types)
    return """
      |type ${element.id.name} = ${aliases};
    """.trimMargin()
  }

  private fun generateTypeReference(typeRefs: Collection<TsTypeReference>) =
    generateTypeReference(*typeRefs.toTypedArray())

  /**
   * A type-reference, be it for the field of an interface, a type alias, or a generic type
   * constraint.
   */
  private fun generateTypeReference(vararg typeRefs: TsTypeReference): String {

    val includeNull = typeRefs.any { it.nullable }

    return typeRefs
      .map { it.id.name }
      .sorted()
      .plus(if (includeNull) "null" else null)
      .filterNotNull()
      .joinToString(separator = " | ")
  }

  private fun generateList(element: TsStructure.TsList): String {
    val elementsTypeRef = generateTypeReference(element.elementsTsType)
    return """
      |${elementsTypeRef}[]
    """.trimMargin()
  }

  private fun generateMap(element: TsStructure.TsMap): String {
    val keyTypeRef = generateTypeReference(element.keyTsType)
    val valueTypeRef = generateTypeReference(element.keyTsType)
    return """
      |{ [key: $keyTypeRef]: $valueTypeRef }
    """.trimMargin()
  }
}
