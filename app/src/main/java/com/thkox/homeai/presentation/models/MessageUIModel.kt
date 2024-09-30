package com.thkox.homeai.presentation.models

import androidx.compose.runtime.Immutable

@Immutable
data class MessageUIModel(
    val sender: String,
    val text: String,
    val timestamp: String,
)