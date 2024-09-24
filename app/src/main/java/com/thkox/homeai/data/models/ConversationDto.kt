package com.thkox.homeai.data.models

data class ConversationDto(
    val id: String,
    val startTime: String,
    val endTime: String?,
    val title: String?,
    val status: String,
    val userId: String,
    val selectedDocumentIds: List<String>
)
