package com.thkox.homeai.data.models

data class MessageDto(
    val senderId: String?,
    val content: String,
    val timestamp: String,
    val tokensGenerated: Int,
    val responseTime: Float
)
