package com.thkox.homeai.data.repository

import com.thkox.homeai.data.models.DocumentDto
import com.thkox.homeai.data.sources.remote.ApiService
import com.thkox.homeai.domain.repository.DocumentRepository
import okhttp3.MultipartBody
import retrofit2.Response

class DocumentRepositoryImpl(
    private val apiService: ApiService
) : DocumentRepository {
    override suspend fun uploadDocuments(files: List<MultipartBody.Part>): Response<List<DocumentDto>> {
        return apiService.uploadDocuments(files)
    }

    override suspend fun deleteDocument(documentId: String): Response<Unit> {
        return apiService.deleteDocument(documentId)
    }

    override suspend fun getUserDocuments(): Response<List<DocumentDto>> {
        return apiService.getUserDocuments()
    }

    override suspend fun getDocumentDetails(documentId: String): Response<DocumentDto> {
        return apiService.getDocumentDetails(documentId)
    }
}