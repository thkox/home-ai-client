package com.thkox.homeai.data.api

import com.thkox.homeai.data.models.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET
    suspend fun isFastApiApp(@Url address: String): Response<ResponseBody>

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

    @PUT("/users/{user_id}")
    suspend fun updateProfile(
        @Path("user_id") userId: String,
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

    @DELETE("/documents/{documentId}")
    suspend fun deleteDocument(
        @Path("documentId") documentId: String
    ): Response<Unit>

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