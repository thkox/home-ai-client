package com.thkox.homeai.data.models

data class UserResponseDto(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val enabled: Boolean,
    val role: String
)