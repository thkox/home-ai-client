package com.thkox.homeai.domain.usecase

import com.thkox.homeai.data.models.DocumentDto
import com.thkox.homeai.domain.repository.DocumentRepository
import retrofit2.Response
import javax.inject.Inject

class GetDocumentDetailsUseCase @Inject constructor(
    private val documentRepository: DocumentRepository
) {
    suspend operator fun invoke(documentId: String): Response<DocumentDto> {
        return documentRepository.getDocumentDetails(documentId)
    }
}