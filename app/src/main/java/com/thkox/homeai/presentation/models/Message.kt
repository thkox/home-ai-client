package com.thkox.homeai.presentation.models

import androidx.compose.runtime.Immutable

@Immutable
data class Message(
    val sender: String,
    val text: String,
    val timestamp: String,
)