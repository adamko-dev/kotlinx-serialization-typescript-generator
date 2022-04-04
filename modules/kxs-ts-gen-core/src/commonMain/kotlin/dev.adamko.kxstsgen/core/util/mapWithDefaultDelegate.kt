package dev.adamko.kxstsgen.core.util

import kotlin.jvm.JvmName
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class MutableMapWithDefaultPut<K, V>(
  initial: Map<K, V> = emptyMap(),
  private val defaultValue: (key: K) -> V,
) : ReadWriteProperty<Any?, MutableMap<K, V>> {

  private var map: MutableMap<K, V> = initial.toMutableMap().withDefaultPut(defaultValue)

  override fun getValue(thisRef: Any?, property: KProperty<*>): MutableMap<K, V> = map

  override fun setValue(thisRef: Any?, property: KProperty<*>, value: MutableMap<K, V>) {
    this.map = value.withDefaultPut(defaultValue)
  }
}


class MapWithDefaultPut<K, V>(
  initial: Map<K, V> = emptyMap(),
  private val defaultValue: (key: K) -> V,
) : ReadWriteProperty<Any?, Map<K, V>> {

  private var map: Map<K, V> = with(initial.toMutableMap()) {
    withDefault { key -> getOrPut(key) { defaultValue(key) } }
  }

  override fun getValue(thisRef: Any?, property: KProperty<*>): Map<K, V> = map

  override fun setValue(thisRef: Any?, property: KProperty<*>, value: Map<K, V>) {
    this.map = value.toMutableMap().withDefaultPut(defaultValue)
  }
}


@JvmName("mapWithDefaultPut")
fun <K, V> Map<K, V>.withDefaultPut(defaultValue: (key: K) -> V): Map<K, V> =
  with(this.toMutableMap()) {
    withDefault { key -> getOrPut(key) { defaultValue(key) } }
  }


@JvmName("mutableMapWithDefaultPut")
fun <K, V> MutableMap<K, V>.withDefaultPut(defaultValue: (key: K) -> V): MutableMap<K, V> =
  with(this) {
    withDefault { key -> getOrPut(key) { defaultValue(key) } }
  }
