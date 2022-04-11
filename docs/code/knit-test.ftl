<#--@formatter:off-->
<#-- @ftlvariable name="test.name" type="java.lang.String" -->
<#-- @ftlvariable name="test.package" type="java.lang.String" -->
// This file was automatically generated from ${file.name} by Knit tool. Do not edit.
@file:Suppress("JSUnusedLocalSymbols")
package ${test.package}

import dev.adamko.kxstsgen.util.*
<#--import io.kotest.assertions.*-->
<#--import io.kotest.core.*-->
import io.kotest.core.spec.style.*
import io.kotest.matchers.*
<#--import io.kotest.matchers.string.*-->
import kotlinx.knit.test.*

class ${test.name} : FunSpec({

  tags(Knit)

<#list cases as case><#assign method = test["mode.${case.param}"]!"custom">
  context("${case.name}") {
    val actual = captureOutput("${case.name}") {
      ${case.knit.package}.${case.knit.name}.main()
    }.normalizeJoin()

    test("expect actual matches TypeScript") {
      actual.shouldBe(
        <#if case.param != "TS_COMPILE_OFF">
        // language=TypeScript
        </#if>
        """
          <#list case.lines as line>
          |${line}
          </#list>
        """.trimMargin()
        .normalize()
      )
    }

    <#if case.param == "TS_COMPILE_OFF">
    // TS_COMPILE_OFF
    // test("expect actual compiles").config(tags = tsCompile) {
    //   actual.shouldTypeScriptCompile()
    // }
    <#else>
    test("expect actual compiles").config(tags = tsCompile) {
      actual.shouldTypeScriptCompile()
    }
    </#if>
  }
<#sep>

</#list>
})
