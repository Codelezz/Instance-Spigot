@file:JvmName("Token")
@file:Suppress("unused")
package com.codelezz.spigot.utils

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Merge two maps to one map.
 */
fun <K, V> Map<K, V>.mergeReduce(other: Map<K, V>, reduce: (V, V) -> V = { _, b -> b }): Map<K, V> {
    val result = LinkedHashMap<K, V>(this.size + other.size)
    result.putAll(this)
    other.forEach { e -> result[e.key] = result[e.key]?.let { reduce(e.value, it) } ?: e.value }
    return result
}

/**
 * Get a generic type for a type token.
 */
fun <T> genericType(): Type = object : TypeToken<T>() {}.type

/**
 * Inline a generic type token.
 */
inline fun <reified T> genericTypeInline(): Type = object : TypeToken<T>() {}.type
