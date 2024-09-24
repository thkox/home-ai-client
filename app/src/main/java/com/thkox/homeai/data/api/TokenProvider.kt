package com.thkox.homeai.data.api

interface TokenProvider {
    fun getToken(): String?
    fun saveToken(token: String)
}