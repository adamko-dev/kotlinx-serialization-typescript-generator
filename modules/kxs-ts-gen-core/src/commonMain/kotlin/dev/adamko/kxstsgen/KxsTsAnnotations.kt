package dev.adamko.kxstsgen

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InheritableSerialInfo

/**
 * Marks a property as required in the generated TypeScript interface.
 *
 * This annotation is inheritable, so it should be sufficient to place it on a base class of hierarchy.
 *
 * This annotation should only be used if [kotlinx.serialization.json.JsonConfiguration.encodeDefaults]
 * is set to true. If it is false (which it is by default), then properties with default values are
 * potentially omitted from the generated JSON.
 */
@InheritableSerialInfo
@Target(AnnotationTarget.PROPERTY)
@ExperimentalSerializationApi
annotation class KxsTsRequired
