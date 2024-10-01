package com.thkox.homeai.data.models

import com.google.gson.annotations.SerializedName

data class ChangePassword(
    @SerializedName("old_password") val oldPassword: String,
    @SerializedName("new_password") val newPassword: String
)