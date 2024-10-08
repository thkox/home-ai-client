package com.thkox.homeai.domain.repository

import kotlinx.coroutines.flow.Flow

interface SpeechRecognitionRepository {
    fun startListening(): Flow<String>
    fun stopListening()
}
