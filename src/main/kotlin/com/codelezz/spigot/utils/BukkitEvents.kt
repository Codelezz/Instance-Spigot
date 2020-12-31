package com.codelezz.spigot.utils

import org.bukkit.event.*
import org.bukkit.plugin.Plugin

/**
 * Add a bukkit event listener.
 */
inline fun <reified T : Event> Plugin.listen(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    listener: Listener = object : Listener {},
    crossinline callback: (T) -> Unit
) = server.pluginManager.registerEvent(
    T::class.java, listener,
    priority, { _, event -> if (event is T) callback(event) },
    this, ignoreCancelled
)

/**
 * Register bukkit event listener.
 */
fun Plugin.registerEvents(
    listener: Listener
) = server.pluginManager.registerEvents(
    listener,
    this
)
