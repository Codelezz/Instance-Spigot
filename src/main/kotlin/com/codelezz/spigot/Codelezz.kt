package com.codelezz.spigot

import com.codelezz.kotlin.CodelezzBase
import com.codelezz.spigot.commands.CodelezzCommand
import org.bukkit.plugin.java.JavaPlugin

class Codelezz : JavaPlugin() {
	companion object {
		lateinit var plugin: Codelezz
	}

	override fun onLoad() {
		plugin = this
	}

	override fun onEnable() {
		CodelezzBase.initialize(dataFolder)
		getCommand("codelezz").executor = CodelezzCommand()
	}
}
