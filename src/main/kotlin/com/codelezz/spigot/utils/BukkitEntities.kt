@file:Suppress("unused")
package com.codelezz.spigot.utils

import org.bukkit.GameMode
import org.bukkit.entity.*


inline val Entity.id
    get() = uniqueId.toString()

fun Player.factoryReset(gamemode: GameMode = GameMode.SURVIVAL, level: Int = 0, experience: Float = 0f) {
    inventory.clear()
    activePotionEffects.forEach { pot -> removePotionEffect(pot.type) }
    gameMode = gamemode
    setLevel(level)
    exp = experience.clamp()
    resetPlayerTime() //We only clean this in lobby cases, where it won't get used much.
    resetPlayerWeather()
    factoryResetLightweight()
}

fun Float.clamp(): Float {
    return coerceAtMost(1.0f).coerceAtLeast(0.0f)
}


fun Player.factoryResetLightweight() {
    walkSpeed = 0.2f
    health = maxHealth
    foodLevel = 20
    saturation = 5.0f
    fireTicks = 0
}


inline fun <reified T : Entity> Entity.getTrueDamager(): T? {
    return if (this is T) this
    else if (this is Projectile && shooter is T) shooter as T
    else if (this is Tameable && owner is T) owner as T
    else null
}
