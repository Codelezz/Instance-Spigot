package com.codelezz.spigot

import com.codelezz.spigot.backend.Backend
import org.bukkit.plugin.java.JavaPlugin

class Codelezz : JavaPlugin() {
	companion object {
		lateinit var plugin: Codelezz;
	}

	override fun onLoad() {
		plugin = this
	}

	override fun onEnable() {
		Backend.init()
	}

}
