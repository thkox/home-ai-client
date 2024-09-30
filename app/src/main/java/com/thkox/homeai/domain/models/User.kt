package com.thkox.homeai.domain.models

data class User(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val enabled: Boolean,
    val role: String
)