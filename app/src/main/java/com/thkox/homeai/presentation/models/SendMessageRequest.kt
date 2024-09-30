package com.thkox.homeai.presentation.models

data class SendMessageRequest(
    val userMessage: String,
    val documentIds: List<String>?
)