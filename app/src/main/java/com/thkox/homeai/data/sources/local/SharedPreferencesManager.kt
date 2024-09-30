package com.thkox.homeai.data.sources.local

import android.content.SharedPreferences
import com.thkox.homeai.domain.repository.TokenRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : TokenRepository {

    override fun getToken(): String? {
        return sharedPreferences.getString("jwt_token", null)
    }

    override fun saveToken(token: String) {
        sharedPreferences.edit().putString("jwt_token", token).apply()
    }

    fun getBaseUrl(): String? {
        return sharedPreferences.getString("base_url", null)
    }

    fun saveBaseUrl(baseUrl: String) {
        sharedPreferences.edit().putString("base_url", baseUrl).apply()
    }
}