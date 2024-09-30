package com.thkox.homeai.domain.repository

import com.thkox.homeai.data.models.DocumentDto
import okhttp3.MultipartBody
import retrofit2.Response

interface DocumentRepository {
    suspend fun uploadDocuments(files: List<MultipartBody.Part>): Response<List<DocumentDto>>
    suspend fun deleteDocument(documentId: String): Response<Unit>
    suspend fun getUserDocuments(): Response<List<DocumentDto>>
    suspend fun getDocumentDetails(documentId: String): Response<DocumentDto>
}