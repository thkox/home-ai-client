package com.thkox.homeai.domain.usecase

import com.thkox.homeai.data.models.DocumentDto
import com.thkox.homeai.domain.models.Document
import com.thkox.homeai.domain.repository.DocumentRepository
import com.thkox.homeai.domain.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUserDocumentsUseCase @Inject constructor(
    private val documentRepository: DocumentRepository
) {
    suspend operator fun invoke(): Result<List<Document>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = documentRepository.getUserDocuments()
                if (response.isSuccessful) {
                    val documentDtos = response.body()
                    val documents = documentDtos?.map { it.toDomainModel() } ?: emptyList()
                    Result.Success(documents)
                } else {
                    Result.Error("Failed to get user documents: ${response.message()}")
                }
            } catch (e: Exception) {
                Result.Error("An error occurred: ${e.localizedMessage}")
            }
        }
    }
}

private fun DocumentDto.toDomainModel(): Document {
    return Document(
        id = this.id,
        fileName = this.fileName,
        uploadTime = this.uploadTime,
        fileSize = this.size
    )
}
