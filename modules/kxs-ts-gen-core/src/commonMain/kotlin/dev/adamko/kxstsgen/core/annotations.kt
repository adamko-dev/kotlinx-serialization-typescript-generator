package dev.adamko.kxstsgen.core


@Target(
  AnnotationTarget.CLASS,
  AnnotationTarget.PROPERTY,
  AnnotationTarget.FUNCTION,
  AnnotationTarget.TYPEALIAS,
)
@RequiresOptIn(level = RequiresOptIn.Level.WARNING)
@MustBeDocumented
annotation class UnimplementedKxsTsGenApi
