package com.codelezz.spigot.commands

import com.codelezz.kotlin.CodelezzBase
import com.codelezz.spigot.Codelezz.Companion.plugin
import com.codelezz.spigot.utils.launch
import com.codelezz.spigot.utils.msgCode
import org.bukkit.command.*

class CodelezzCommand : TabExecutor {
	override fun onCommand(
		sender: CommandSender?,
		command: Command?,
		label: String?,
		args: Array<out String>?
	): Boolean {
		if (args == null || args.size != 1) return false

		if (args[0] == "reload") {
			sender?.msgCode("Reloading modules...")
			plugin.launch {
				CodelezzBase.reInitialize()
				sender?.msgCode("Reloaded modules.")
			}
			return true
		}
		return false
	}

	override fun onTabComplete(
		sender: CommandSender?,
		command: Command?,
		alias: String?,
		args: Array<out String>?
	): MutableList<String> {
		return mutableListOf("reload")
	}
}