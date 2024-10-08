package com.thkox.homeai.domain.usecase

import com.thkox.homeai.domain.repository.SpeechRecognitionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecognizeSpeechUseCase @Inject constructor(
    private val speechRecognitionRepository: SpeechRecognitionRepository
) {
    fun startListening(): Flow<String> {
        return speechRecognitionRepository.startListening()
    }

    fun stopListening() {
        speechRecognitionRepository.stopListening()
    }
}