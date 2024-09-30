package com.thkox.homeai.domain.usecase

import com.thkox.homeai.domain.repository.DocumentRepository
import com.thkox.homeai.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteDocumentUseCase @Inject constructor(
    private val documentRepository: DocumentRepository
) {
    suspend fun invoke(documentId: String): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = documentRepository.deleteDocument(documentId)
                if (response.isSuccessful) {
                    Resource.Success(Unit)
                } else {
                    Resource.Error("Failed to delete document: ${response.message()}")
                }
            } catch (e: Exception) {
                Resource.Error("An error occurred: ${e.localizedMessage}")
            }
        }
    }
}