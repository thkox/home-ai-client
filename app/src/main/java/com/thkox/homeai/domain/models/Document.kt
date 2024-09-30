package com.thkox.homeai.domain.models

data class Document(
    val id: String,
    val fileName: String,
    val fileSize: Float,
    val uploadTime: String
)