package com.thkox.homeai.domain.models

data class SendMessageRequest(
    val userMessage: String,
    val documentIds: List<String>?
)