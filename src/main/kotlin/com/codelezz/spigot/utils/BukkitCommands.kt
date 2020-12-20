@file:Suppress("unused")
package com.codelezz.spigot.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * Register in-game command.
 */
object BukkitCommands {

    private val cmdMap: CommandMap
    private val syncCommandMethod: Method

    init {
        val bukkitCommandMap: Field = server.javaClass.getDeclaredField("commandMap")
        bukkitCommandMap.isAccessible = true

        cmdMap = bukkitCommandMap.get(server) as CommandMap
        syncCommandMethod = server.javaClass.getDeclaredMethod("syncCommands")
        syncCommandMethod.isAccessible = true
    }

    /**
     * Register a minecraft command.
     */
    fun register(
        name: String,
        description: String,
        usageMessage: String,
        aliases: List<String>,
        executor: (CommandSender, String, Array<String>) -> Unit
    ) {
        val command = object : Command(name, description, usageMessage, aliases) {
            override fun execute(sender: CommandSender, label: String, args: Array<String>): Boolean {
                executor(sender, label, args)
                return true
            }
        }
        cmdMap.register("serverlezz", command)
    }

    suspend fun syncCommands() {
        withContext(Dispatchers.SYNC) {
            syncCommandMethod.invoke(server)
        }
    }
}

/**
 * Register a minecraft command.
 */
fun Plugin.command(
    name: String,
    description: String,
    usageMessage: String,
    vararg aliases: String,
    executor: (CommandSender, String, Array<String>) -> Unit
) = BukkitCommands.register(name, description, usageMessage, aliases.toList(), executor)

/**
 * Register a minecraft command.
 */
inline fun Plugin.command(
    name: String,
    description: String,
    usageMessage: String,
    vararg aliases: String,
    crossinline executor: (CommandSender, Array<String>) -> Unit
) = command(name, description, usageMessage, *aliases) { sender, _, args -> executor(sender, args) }

/**
 * Register a minecraft command.
 */
inline fun Plugin.command(
    name: String,
    description: String,
    usageMessage: String,
    vararg aliases: String,
    crossinline executor: (Array<String>) -> Unit
) = command(name, description, usageMessage, *aliases) { _, args -> executor(args) }
