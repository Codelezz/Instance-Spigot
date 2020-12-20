@file:Suppress("Unused")
package com.codelezz.spigot.utils

import org.bukkit.ChatColor
import org.bukkit.entity.Player

private const val ERROR_MESSAGE = "Something went wrong."
private const val FETAL_ERROR_MESSAGE = "&cAn internal error occurred, check the logs"

/**
 * Translate minecraft '&' to a minecraft color-code.
 */
fun String.translateColorCode() = replace(Regex("&([A-Za-z0-9])")) { "§" + it.groups[1]!!.value }

/**
 * Strips the minecraft color-code.
 */
fun String.stripColor() = ChatColor.stripColor(this) ?: this

/**
 * Simple exception.
 */
class PluginException(msg: String) : Exception("&c$msg")

/**
 * Simple exception.
 */
fun error(msg: String): Nothing = throw PluginException(msg)

/**
 * Color a string.
 */
inline fun colored(msg: String?, f: (String) -> Unit) {
    if (!msg.isNullOrBlank()) f(msg.translateColorCode())
}

/**
 * Send a message directly to the player.
 * Message (global) variables wil be parsed, and the chat color will be translated.
 * @see Text
 *
 * @sample
 * val p: BukkitPlayer = ...
 * p.msg("Hey this is a message")
 */
fun Player.msg(msg: String?) {
    colored(msg, ::sendMessage)
}