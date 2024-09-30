package com.thkox.homeai.domain.usecase

import com.thkox.homeai.domain.repository.DocumentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class DeleteDocumentUseCase @Inject constructor(
    private val documentRepository: DocumentRepository
) {
    suspend fun invoke(documentId: String): Response<Unit> {
        return withContext(Dispatchers.IO) {
            documentRepository.deleteDocument(documentId)
        }
    }
}