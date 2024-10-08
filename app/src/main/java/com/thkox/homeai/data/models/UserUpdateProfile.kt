package com.thkox.homeai.data.models

import com.google.gson.annotations.SerializedName

data class UserUpdateProfile(
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    val email: String
)