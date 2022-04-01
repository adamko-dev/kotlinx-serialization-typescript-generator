package dev.adamko.kxstsgen.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class MutableMapWithDefaultPut<K, V>(
  initial: Map<K, V> = emptyMap(),
  private val defaultValue: (key: K) -> V,
) : ReadWriteProperty<Any?, MutableMap<K, V>> {

  private var map: MutableMap<K, V> = with(initial.toMutableMap()) {
    withDefault { key -> getOrPut(key) { defaultValue(key) } }
  }

  override fun getValue(thisRef: Any?, property: KProperty<*>): MutableMap<K, V> = map

  override fun setValue(thisRef: Any?, property: KProperty<*>, value: MutableMap<K, V>) {
    this.map = value
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
    this.map = value
  }
}
