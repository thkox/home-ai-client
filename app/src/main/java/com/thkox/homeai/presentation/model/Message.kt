package com.thkox.homeai.presentation.model

import android.media.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Immutable
import java.sql.Timestamp

@Immutable
data class Message(
    val author: String,
    val text: String,
    val timestamp: String,
    val image: Image? = null,
    val authorImage: Image? = null
) {

}