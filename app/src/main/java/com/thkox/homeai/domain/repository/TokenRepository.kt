package com.thkox.homeai.domain.repository

interface TokenRepository {
    fun getToken(): String?
    fun saveToken(token: String)
}