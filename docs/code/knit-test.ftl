<#-- @ftlvariable name="test.name" type="java.lang.String" -->
<#-- @ftlvariable name="test.package" type="java.lang.String" -->
// This file was automatically generated from ${file.name} by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package ${test.package}

import dev.adamko.kxstsgen.util.*
import io.kotest.assertions.*
import io.kotest.matchers.*
import io.kotest.matchers.string.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test

class ${test.name} {
<#list cases as case><#assign method = test["mode.${case.param}"]!"custom">
  @Test
  fun test${case.name}() {
    val actual = captureOutput("${case.name}") {
      ${case.knit.package}.${case.knit.name}.main()
    }.normalizeJoin()

    actual.shouldBe(
      // language=TypeScript
      """
        <#list case.lines as line>
        |${line}
        </#list>
      """.trimMargin()
      .normalize()
    )

    typescriptCompile(actual).asClue { tscOutput ->
      tscOutput.shouldNotBeEmpty()
      tscOutput shouldNotContain "error"
    }
  }
<#sep>

</#list>
}
