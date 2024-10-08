package com.thkox.homeai.domain.usecase

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.thkox.homeai.data.models.DocumentDto
import com.thkox.homeai.domain.models.Document
import com.thkox.homeai.domain.repository.DocumentRepository
import com.thkox.homeai.domain.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

class UploadDocumentUseCase @Inject constructor(
    private val documentRepository: DocumentRepository
) {
    suspend operator fun invoke(context: Context, uri: Uri): Result<List<Document>> {
        return withContext(Dispatchers.IO) {
            try {
                val contentResolver: ContentResolver = context.contentResolver
                val displayName =
                    contentResolver.query(uri, null, null, null, null)?.use { cursor ->
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

                    val requestFile =
                        tempFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val body =
                        MultipartBody.Part.createFormData("files", tempFile.name, requestFile)

                    Log.d("UploadDocument", "Uploading file: ${tempFile.name}")

                    val response = documentRepository.uploadDocuments(listOf(body))
                    tempFile.delete()

                    if (response.isSuccessful) {
                        val documentDtos = response.body()
                        val documents = documentDtos?.map { it.toDomainModel() } ?: emptyList()
                        Result.Success(documents)
                    } else {
                        Result.Error("Failed to upload document: ${response.message()}")
                    }
                } else {
                    Result.Error("Failed to open file input stream")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("UploadDocument", "Error uploading document", e)
                Result.Error("An error occurred: ${e.localizedMessage}")
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
