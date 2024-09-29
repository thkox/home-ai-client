package com.thkox.homeai.data.sources.remote

import com.thkox.homeai.data.models.ContinueConversationRequest
import com.thkox.homeai.data.models.ConversationDto
import com.thkox.homeai.data.models.DocumentDto
import com.thkox.homeai.data.models.MessageDto
import com.thkox.homeai.data.models.TokenDto
import com.thkox.homeai.data.models.UserCreateRequest
import com.thkox.homeai.data.models.UserResponseDto
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Url

interface ApiService {

    @GET
    suspend fun root(@Url address: String): Response<ResponseBody>

    @POST("/token")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") email: String,
        @Field("password") password: String
    ): Response<TokenDto>

    @POST("/users/")
    suspend fun registerUser(
        @Body userCreateRequest: UserCreateRequest
    ): Response<UserResponseDto>

    @PUT("/users/me")
    suspend fun updateMyProfile(
        @Body userCreateRequest: UserCreateRequest
    ): Response<UserResponseDto>

    @POST("/conversations/")
    suspend fun startConversation(): Response<ConversationDto>

    @POST("/conversations/{conversationId}/continue")
    suspend fun continueConversation(
        @Path("conversationId") conversationId: String,
        @Body request: ContinueConversationRequest
    ): Response<MessageDto>

    @Multipart
    @POST("/documents/upload")
    suspend fun uploadDocuments(
        @Part files: List<MultipartBody.Part>
    ): Response<List<DocumentDto>>

    @DELETE("/documents/{id}")
    suspend fun deleteDocument(@Path("id") documentId: String): Response<Unit>

    @GET("/documents/me")
    suspend fun getUserDocuments(): Response<List<DocumentDto>>

    @DELETE("/conversations/{conversationId}")
    suspend fun deleteConversation(
        @Path("conversationId") conversationId: String
    ): Response<Unit>

    @GET("/conversations/me")
    suspend fun getUserConversations(): Response<List<ConversationDto>>

    @GET("/conversations/{conversationId}/messages")
    suspend fun getConversationMessages(
        @Path("conversationId") conversationId: String
    ): Response<List<MessageDto>>
}