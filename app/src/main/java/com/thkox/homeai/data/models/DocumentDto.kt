package com.thkox.homeai.data.models

import com.google.gson.annotations.SerializedName

data class DocumentDto(
    val id: String,
    @SerializedName("file_name") val fileName: String,
    @SerializedName("upload_time") val uploadTime: String,
    val size: Float,
    val checksum: String
)
