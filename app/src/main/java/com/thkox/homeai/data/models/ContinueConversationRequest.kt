package com.thkox.homeai.data.models

import com.google.gson.annotations.SerializedName

data class ContinueConversationRequest(
    val message: String,
    @SerializedName("selected_documents") val selectedDocuments: List<String>?
)