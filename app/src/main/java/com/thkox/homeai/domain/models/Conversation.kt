package com.thkox.homeai.domain.models

data class Conversation(
    val id: String,
    val title: String?,
    val startTime: String,
    val selectedDocumentIds: List<String>?
)