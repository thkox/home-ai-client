package com.thkox.homeai.data.models

import com.google.gson.annotations.SerializedName

data class UserResponseDto(
    @SerializedName("user_id") val userId: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("email") val email: String,
    val enabled: Boolean,
    val role: String
)