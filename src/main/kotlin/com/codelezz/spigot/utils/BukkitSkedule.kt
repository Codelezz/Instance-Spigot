@file:JvmName("Async")
@file:Suppress("unused")
package com.codelezz.spigot.utils

import com.codelezz.spigot.Codelezz.Companion.plugin
import com.okkero.skedule.BukkitDispatcher
import com.okkero.skedule.dispatcher
import kotlinx.coroutines.*
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.CompletableFuture

/**
 * Launches code on a thread (default async).
 * This can be used for coroutines/suspended.
 *
 * Errors thrown wil be catched, and reported to google-cloud.
 *
 * @sample
 * ~kotlin
 * plugin.launch {
 *      // CODE
 * }
 */
fun JavaPlugin.launch(async: Boolean = true, block: suspend CoroutineScope.() -> Unit) =
    GlobalScope.launch(this.dispatcher(async)) {
        try {
            block()
        } catch (e: Throwable) {
            throw e
        }
    }

/**
 * Launches code on a thread (default async).
 * This can be used for coroutines/suspended.
 *
 * Errors thrown wil be catched, and reported to google-cloud.
 *
 * @sample
 * ~kotlin
 * plugin.launch {
 *      // CODE
 * }
 */
fun <T : Any?> JavaPlugin.async(async: Boolean = true, block: suspend CoroutineScope.() -> T?): Deferred<T?> =
    GlobalScope.async(this.dispatcher(async)) {
        try {
            block()
        } catch (e: Throwable) {
            throw e
        }
    }

/**
 * Launch a code on a thread (default async) and get the result on a completable future.
 * This is mostly relevant for java.
 *
 * Errors thrown wil be catched, and reported to google-cloud.
 *
 * @sample
 * ~java
 * CompletableFuture<String> string = Async.launch(ServerlezzPlugin.plugin, true, (s, a) -> {
 *      // CODE
 *      return "A simple string";
 * });
 */
fun <T : Any> JavaPlugin.launchFuture(
    async: Boolean = true,
    block: suspend CoroutineScope.() -> T
): CompletableFuture<T> {
    val cf: CompletableFuture<T> = CompletableFuture()
    launch(async) {
        catch<Exception>(err = {
            cf.completeExceptionally(it)
        }) {
            val t = block()
            cf.complete(t)
        }
    }
    return cf
}

/**
 * Dispatches a coroutine on the main minecraft thread.
 */
val Dispatchers.SYNC: BukkitDispatcher
    get() = plugin.dispatcher(false)

/**
 * Dispatches a coroutine on an async minecraft thread.
 */
val Dispatchers.ASYNC: BukkitDispatcher
    get() = plugin.dispatcher(true)
