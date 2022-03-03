package buildsrc.convention

import org.gradle.kotlin.dsl.`jacoco-report-aggregation`
import org.gradle.kotlin.dsl.base
import org.gradle.kotlin.dsl.dependencies

plugins {
    base
    `jacoco-report-aggregation`
}


dependencies {
    subprojects
        .filter { it.buildFile.exists() }
        .forEach { jacocoAggregation(it) }
}


@Suppress("UnstableApiUsage") // jacoco-report-aggregation is incubating
val testCodeCoverageReport by reporting.reports.creating(JacocoCoverageReport::class) {
    testType.set(TestSuiteType.UNIT_TEST)
}


tasks.check {
    dependsOn(testCodeCoverageReport.reportTask)
}
