package com.thkox.homeai.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class Message(
    val sender: String,
    val text: String,
    val timestamp: String,
)