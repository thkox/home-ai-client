package com.thkox.homeai.domain.usecase

import com.thkox.homeai.data.models.DocumentDto
import com.thkox.homeai.domain.repository.DocumentRepository
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class UploadDocumentUseCase @Inject constructor(
    private val documentRepository: DocumentRepository
) {
    suspend operator fun invoke(files: List<MultipartBody.Part>): Response<List<DocumentDto>> {
        return documentRepository.uploadDocuments(files)
    }
}