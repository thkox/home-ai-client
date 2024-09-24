package com.thkox.homeai.data.sources.local

import android.content.SharedPreferences
import com.thkox.homeai.data.api.TokenProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : TokenProvider {

    override fun getToken(): String? {
        return sharedPreferences.getString("jwt_token", null)
    }

    override fun saveToken(token: String) {
        sharedPreferences.edit().putString("jwt_token", token).apply()
    }

    fun getBaseUrl(): String? {
        return sharedPreferences.getString("base_url", "http://localhost:8000/")
    }

    fun saveBaseUrl(baseUrl: String) {
        sharedPreferences.edit().putString("base_url", baseUrl).apply()
    }
}