package dev.adamko.kxstsgen


@Target(
  AnnotationTarget.CLASS,
  AnnotationTarget.PROPERTY,
  AnnotationTarget.FUNCTION,
  AnnotationTarget.TYPEALIAS
)
@RequiresOptIn(level = RequiresOptIn.Level.WARNING)
@MustBeDocumented
annotation class UnimplementedKxTsGenApi
