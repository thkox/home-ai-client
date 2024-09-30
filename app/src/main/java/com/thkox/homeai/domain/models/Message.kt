package com.thkox.homeai.domain.models

data class Message(
    val senderId: String,
    val content: String,
    val timestamp: String
)