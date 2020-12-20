@file:Suppress("unused")
package com.codelezz.spigot.utils

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import kotlin.math.floor
import kotlin.math.pow

/**
 * Savable coordinates for Bukkit.
 */
data class Coordinate(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val z: Double = 0.0,
    val yaw: Float = 0f,
    val pitch: Float = 0f,
    val world: String = ""
) {

    companion object {
        fun of(location: Location) =
            Coordinate(location.x, location.y, location.z, location.yaw, location.pitch, location.world?.name ?: "")

        fun of(block: Block) =
            Coordinate(block.x.toDouble(), block.y.toDouble(), block.z.toDouble(), world = block.world.name)
    }

    /**
     * Gets the bukkit location of the coordinate,
     * if the world isn't found this wil throw an error
     */
    @Exclude
    val location: Location
        get() = Location(if (world.isNotBlank()) server.getWorld(world) else server.worlds[0], x, y, z, yaw, pitch)

    @Exclude
    val blockX: Int
        get() = Location.locToBlock(x)

    @Exclude
    val blockY: Int
        get() = Location.locToBlock(y)

    @Exclude
    val blockZ: Int
        get() = Location.locToBlock(z)

    @Exclude
    val getChunkX: Int
        get() = floor(x / 16).toInt()

    @Exclude
    val getChunkZ: Int
        get() = floor(z / 16).toInt()

    /**
     * Gets the bukkit location of the coordinate.
     */
    infix fun locationInWorld(world: World): Location = Location(world, x, y, z, yaw, pitch)

    /**
     * Adds an offset to the current coordinate.
     */
    fun add(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0, yaw: Float = 0f, pitch: Float = 0f): Coordinate =
        Coordinate(this.x + x, this.y + y, this.z + z, this.yaw + yaw, this.pitch + pitch)

    /**
     * Returns the squared distance between this location and another.
     * If the two locations are in different worlds this will return
     * [Double.MAX_VALUE]
     */
    infix fun distanceSquared(wl: Coordinate): Double = distanceSquared(wl.world, wl.x, wl.y, wl.z)

    /**
     * Returns the squared distance between this location and another.
     * If the two locations are in different worlds this will return
     * [Double.MAX_VALUE]
     */
    infix fun distanceSquared(wl: Location): Double = distanceSquared(wl.world.name, wl.x, wl.y, wl.z)

    /**
     * Returns the squared distance between this location and another.
     * If the two locations are in different worlds this will return
     * [Double.MAX_VALUE]
     */
    fun distanceSquared(world: String?, x: Double, y: Double, z: Double) =
        if (world != this.world) Double.MAX_VALUE else (x - this.x).pow(2.0) + (y - this.y).pow(2.0) + (z - this.z).pow(
            2.0
        )

    /**
     * Returns the squared distance between this location and another.
     * If the two locations are in different worlds this will return
     * [Double.MAX_VALUE]
     *
     *
     * This variant will not take the Y into account.
     */
    infix fun distanceSquared2d(wl: Coordinate): Double = distanceSquared2d(wl.world, wl.x, wl.z)

    /**
     * Returns the squared distance between this location and another.
     * If the two locations are in different worlds this will return
     * [Double.MAX_VALUE]
     *
     *
     * This variant will not take the Y into account.
     */
    infix fun distanceSquared2d(wl: Location): Double = distanceSquared2d(wl.world.name, wl.x, wl.z)

    /**
     * Returns the squared distance between this location and another.
     * If the two locations are in different worlds this will return
     * [Double.MAX_VALUE]
     *
     *
     * This variant will not take the Y into account.
     */
    fun distanceSquared2d(world: String?, x: Double, z: Double): Double =
        if (world != this.world) Double.MAX_VALUE else (x - this.x).pow(2.0) + (z - this.z).pow(2.0)

    /**
     * Returns the squared distance between this location and another.
     * If the two locations are in different worlds this will return
     * [Double.MAX_VALUE]
     */
    infix fun distanceSquared(loc: Block): Double = distanceSquared(loc.location)
}

val Location.cord: Coordinate
    get() = Coordinate.of(this)

val Block.cord: Coordinate
    get() = Coordinate.of(this)

// DSL STUFF

infix fun Coordinate.inWorld(world: String) = copy(world = world)
infix fun Coordinate.withYaw(yaw: Number) = copy(yaw = yaw.toFloat())
infix fun Coordinate.withPitch(pitch: Number) = copy(pitch = pitch.toFloat())

infix fun Number.withY(y: Number): CordsDsl = CordsDsl(this, y)
infix fun CordsDsl.withZ(z: Number): Coordinate = Coordinate(x.toDouble(), y.toDouble(), z.toDouble())
class CordsDsl(val x: Number, val y: Number)

fun cords(function: CoordinateList.() -> Unit): CoordinateList = CoordinateList().apply(function)

class CoordinateList : ArrayList<Coordinate>() {
    operator fun Coordinate.unaryPlus() = add(this)
    operator fun invoke(function: CoordinateList.() -> Unit) = apply(function)
}
