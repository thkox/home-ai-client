package com.thkox.homeai.domain.usecase

import com.thkox.homeai.data.models.DocumentDto
import com.thkox.homeai.domain.models.Document
import com.thkox.homeai.domain.repository.DocumentRepository
import com.thkox.homeai.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetDocumentDetailsUseCase @Inject constructor(
    private val documentRepository: DocumentRepository
) {
    suspend operator fun invoke(documentId: String): Resource<Document?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = documentRepository.getDocumentDetails(documentId)
                if (response.isSuccessful) {
                    val documentDto = response.body()
                    val document = documentDto?.toDomainModel()
                    Resource.Success(document)
                } else {
                    Resource.Error("Failed to get document details: ${response.message()}")
                }
            } catch (e: Exception) {
                Resource.Error("An error occurred: ${e.localizedMessage}")
            }
        }
    }
}

private fun DocumentDto.toDomainModel(): Document {
    return Document(
        id = this.id,
        fileName = this.fileName,
        fileSize = this.size,
        uploadTime = this.uploadTime
    )
}
