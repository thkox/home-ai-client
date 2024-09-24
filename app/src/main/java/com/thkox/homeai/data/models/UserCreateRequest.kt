package com.thkox.homeai.data.models

data class UserCreateRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val enabled: Boolean = true
)