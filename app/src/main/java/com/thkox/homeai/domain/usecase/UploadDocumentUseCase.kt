package com.thkox.homeai.domain.usecase

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.thkox.homeai.data.models.DocumentDto
import com.thkox.homeai.domain.repository.DocumentRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

class UploadDocumentUseCase @Inject constructor(
    private val documentRepository: DocumentRepository
) {
    suspend operator fun invoke(context: Context, uri: Uri): Response<List<DocumentDto>> {
        try {
            val contentResolver: ContentResolver = context.contentResolver
            val displayName = contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                cursor.getString(nameIndex)
            } ?: "temp_upload_file"

            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            if (inputStream != null) {
                val tempFile = File(context.cacheDir, displayName)
                val outputStream = FileOutputStream(tempFile)
                inputStream.copyTo(outputStream)
                inputStream.close()
                outputStream.close()

                val requestFile = tempFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("files", tempFile.name, requestFile)

                // Log the request details
                Log.d("UploadDocument", "Uploading file: ${tempFile.name}")

                val response = documentRepository.uploadDocuments(listOf(body))
                tempFile.delete() // Clean up the temporary file
                return response
            } else {
                throw Exception("Failed to open file")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("UploadDocument", "Error uploading document", e)
            throw e
        }
    }
}
