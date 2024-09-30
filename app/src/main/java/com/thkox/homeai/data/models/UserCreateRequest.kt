package com.thkox.homeai.data.models

import com.google.gson.annotations.SerializedName

data class UserCreateRequest(
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("email") val username: String,
    val password: String,
    val enabled: Boolean = true
)