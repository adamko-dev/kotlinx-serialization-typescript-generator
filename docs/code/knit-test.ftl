<#-- @ftlvariable name="test.name" type="java.lang.String" -->
<#-- @ftlvariable name="test.package" type="java.lang.String" -->
// This file was automatically generated from ${file.name} by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package ${test.package}

import io.kotest.matchers.*
import kotlinx.knit.test.*
import org.junit.jupiter.api.Test
import dev.adamko.kxstsgen.util.*

class ${test.name} {
<#list cases as case><#assign method = test["mode.${case.param}"]!"custom">
  @Test
  fun test${case.name}() {
    captureOutput("${case.name}") {
      ${case.knit.package}.${case.knit.name}.main()
    }<#if method != "custom">.${method}(
        // language=TypeScript
        """
<#list case.lines as line>
          |${line}
</#list>
        """.trimMargin()
          .normalize()
      )
<#else>.also { lines ->
      check(${case.param})
    }
</#if>
  }
<#sep>

</#list>
}
