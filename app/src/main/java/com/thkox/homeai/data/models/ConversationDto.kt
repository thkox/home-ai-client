package com.thkox.homeai.data.models

import com.google.gson.annotations.SerializedName

data class ConversationDto(
    val id: String,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("end_time") val endTime: String?,
    val title: String?,
    val status: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("selected_document_ids") val selectedDocumentIds: List<String>
)
