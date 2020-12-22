package com.codelezz.spigot.backend

import com.codelezz.spigot.Codelezz.Companion.firebaseApiKey
import com.codelezz.spigot.Codelezz.Companion.gson
import com.codelezz.spigot.Codelezz.Companion.plugin
import com.codelezz.spigot.utils.genericTypeInline
import com.codelezz.spigot.utils.get
import kotlin.reflect.KProperty

val authToken: String by AuthTokenProvider()

private class AuthTokenProvider {

	val file by lazy { plugin.dataFolder["auth.json"] }

	var token: AuthToken? = null
		set(value) {
			value?.let {
				if (!file.parentFile.exists()) file.parentFile.mkdirs()
				file.writeText(gson.toJson(it))
			}
			field = value
		}

	fun loadTokenFromFile(): String {
		if (!file.exists()) return requestNewToken()
		val authToken = gson.fromJson<AuthToken>(file.bufferedReader(), genericTypeInline<AuthToken>())
		if (authToken.expireTime < System.currentTimeMillis()) return requestRefreshToken(authToken.refreshToken)
		token = authToken
		return authToken.token
	}

	fun requestRefreshToken(refreshToken: String): String {
		val result = post {
			url = "https://securetoken.googleapis.com/v1/token?key=$firebaseApiKey"
			json {
				+("grant_type" to "refresh_token")
				+("refresh_token" to refreshToken)
			}
		}

		val data: Map<String, Any> = gson.fromJson(result, genericTypeInline<Map<String, Any>>())

		token = AuthToken(
			token = data["id_token"] as String? ?: throw Exception(),
			refreshToken = data["refresh_token"] as String? ?: throw Exception(),
			expireTime = ((data["expires_in"] as String?)?.toIntOrNull() ?: 3600) * 1000 + System.currentTimeMillis(),
		)

		return token!!.token
	}

	fun requestNewToken(): String {
		val instanceResult = post {
			url = "https://accounts.codelezz.com/create-instance"
			json {}
		}

		val token = gson.fromJson<Map<String, Any>>(instanceResult, genericTypeInline<Map<String, Any>>())["token"]
				?: throw Exception()

		val result = post {
			url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithCustomToken?key=$firebaseApiKey"
			json {
				+("token" to token)
				+("returnSecureToken" to true)
			}
		}

		val data: Map<String, Any> = gson.fromJson(result, genericTypeInline<Map<String, Any>>())

		this.token = AuthToken(
			token = data["idToken"] as String? ?: throw Exception(),
			refreshToken = data["refreshToken"] as String? ?: throw Exception(),
			expireTime = ((data["expiresIn"] as String?)?.toIntOrNull() ?: 3600) * 1000 + System.currentTimeMillis(),
		)

		return this.token!!.token
	}

	operator fun getValue(thisRef: Any?, property: KProperty<*>): String =
		token?.token ?: loadTokenFromFile()
}

private data class AuthToken(
	val token: String,
	val expireTime: Long,
	val refreshToken: String,
)