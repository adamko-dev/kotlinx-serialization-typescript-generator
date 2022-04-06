package dev.adamko.kxstsgen

import kotlinx.serialization.SerialInfo


@Target(AnnotationTarget.CLASS)
@SerialInfo
@MustBeDocumented
annotation class TsExport(
//  val name: String = "",
  val format: Format,
) {
  enum class Format {
    TUPLE,
  }
}
