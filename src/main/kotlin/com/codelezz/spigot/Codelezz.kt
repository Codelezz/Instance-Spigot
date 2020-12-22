package com.codelezz.spigot

import com.codelezz.spigot.backend.Backend
import com.codelezz.spigot.utils.Exclude
import com.codelezz.spigot.utils.getExclusionStrategy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.bukkit.plugin.java.JavaPlugin

class Codelezz : JavaPlugin() {
	companion object {
		/// Firebase api keys can be publicly available.
		const val firebaseApiKey: String = "AIzaSyAxuGMDUrIqBb-Xnn4AzyuKeMqWj8xQtd0"
		lateinit var plugin: Codelezz
		lateinit var gson: Gson
	}

	override fun onLoad() {
		plugin = this
		gson = GsonBuilder()
			.enableComplexMapKeySerialization()
			.addSerializationExclusionStrategy(getExclusionStrategy(Exclude.During.SERIALIZATION))
			.addDeserializationExclusionStrategy(getExclusionStrategy(Exclude.During.DESERIALIZATION))
			.setExclusionStrategies(getExclusionStrategy())
			.serializeNulls()
			.create()
	}

	override fun onEnable() {
		Backend.init()
	}
}
