package dev.adamko.kxstsgen

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InheritableSerialInfo

@InheritableSerialInfo
@Target(AnnotationTarget.PROPERTY)
@ExperimentalSerializationApi
annotation class KxsTsRequired
