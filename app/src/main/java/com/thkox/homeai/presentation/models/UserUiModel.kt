package com.thkox.homeai.presentation.models

data class UserUiModel(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String?,
    val enabled: Boolean,
    val role: String
)