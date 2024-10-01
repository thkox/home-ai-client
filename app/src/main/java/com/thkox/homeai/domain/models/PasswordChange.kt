package com.thkox.homeai.domain.models

data class PasswordChange(
    val oldPassword: String,
    val newPassword: String
)