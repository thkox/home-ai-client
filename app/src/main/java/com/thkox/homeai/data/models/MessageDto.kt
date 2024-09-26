package com.thkox.homeai.data.models

import com.google.gson.annotations.SerializedName

data class MessageDto(
    @SerializedName("sender_id") val senderId: String?,
    val content: String,
    val timestamp: String,
)
