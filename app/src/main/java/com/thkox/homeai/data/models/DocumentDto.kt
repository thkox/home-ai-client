package com.thkox.homeai.data.models

data class DocumentDto(
    val id: String,
    val fileName: String,
    val uploadTime: String,
    val size: Float,
    val checksum: String
)
