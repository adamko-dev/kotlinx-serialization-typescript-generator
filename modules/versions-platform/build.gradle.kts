plugins {
  buildsrc.convention.subproject
  buildsrc.convention.`maven-publish`

  `java-platform`
}

javaPlatform {
  allowDependencies()
}

dependencies {

  api(platform(libs.kotlin.bom))
  api(platform(libs.kotlinx.serialization.bom))
  api(platform(libs.kotlinx.coroutines.bom))

  api(platform(libs.kotest.bom))

  constraints {
    api(libs.classgraph)
    api(libs.kotlinSymbolProcessing)
    api(libs.kotlinCompileTesting)
    api(libs.kotlinCompileTesting.ksp)
  }
}

publishing {
  publications {
    create<MavenPublication>("versions-platform") {
      from(components["javaPlatform"])
    }
  }
}
