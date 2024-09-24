package com.thkox.homeai.data.repository

import android.content.SharedPreferences
import com.thkox.homeai.data.api.TokenProvider

class TokenProviderImpl(private val sharedPreferences: SharedPreferences) : TokenProvider {

    companion object {
        private const val KEY_TOKEN = "jwt_token"
    }

    override fun getToken(): String? {
        return sharedPreferences.getString(KEY_TOKEN, null)
    }

    override fun saveToken(token: String) {
        sharedPreferences.edit().putString(KEY_TOKEN, token).apply()
    }
}