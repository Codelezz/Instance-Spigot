package com.codelezz.spigot

import com.codelezz.kotlin.CodelezzBase
import com.codelezz.spigot.commands.CodelezzCommand
import org.bukkit.plugin.java.JavaPlugin

class Codelezz : JavaPlugin() {
	companion object {
		/// Firebase api keys can be publicly available.
		const val firebaseApiKey: String = "AIzaSyAxuGMDUrIqBb-Xnn4AzyuKeMqWj8xQtd0"
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
