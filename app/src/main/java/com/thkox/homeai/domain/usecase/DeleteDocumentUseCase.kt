package com.thkox.homeai.domain.usecase

import com.thkox.homeai.domain.repository.DocumentRepository
import com.thkox.homeai.domain.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteDocumentUseCase @Inject constructor(
    private val documentRepository: DocumentRepository
) {
    suspend fun invoke(documentId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = documentRepository.deleteDocument(documentId)
                if (response.isSuccessful) {
                    Result.Success(Unit)
                } else {
                    Result.Error("Failed to delete document: ${response.message()}")
                }
            } catch (e: Exception) {
                Result.Error("An error occurred: ${e.localizedMessage}")
            }
        }
    }
}