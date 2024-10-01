package com.thkox.homeai.data.sources.remote

import com.thkox.homeai.data.models.ChangePassword
import com.thkox.homeai.data.models.ContinueConversationRequest
import com.thkox.homeai.data.models.ConversationDto
import com.thkox.homeai.data.models.DocumentDto
import com.thkox.homeai.data.models.MessageDto
import com.thkox.homeai.data.models.TokenDto
import com.thkox.homeai.data.models.UserCreateRequest
import com.thkox.homeai.data.models.UserResponseDto
import com.thkox.homeai.data.models.UserUpdateProfile
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

    @PUT("/users/me/profile")
    suspend fun updateMyProfile(
        @Body userUpdateProfile: UserUpdateProfile
    ): Response<UserResponseDto>

    @PUT("/users/me/password")
    suspend fun changeMyPassword(
        @Body changePassword: ChangePassword
    ): Response<Unit>

    @GET("/users/me/details")
    suspend fun getUserDetails(): Response<UserResponseDto>

    @POST("/conversations/")
    suspend fun startConversation(): Response<ConversationDto>

    @GET("/conversations/{conversation_id}/details")
    suspend fun getConversationDetails(
        @Path("conversation_id") conversationId: String
    ): Response<ConversationDto>

    @GET("/conversations/me")
    suspend fun getUserConversations(): Response<List<ConversationDto>>

    @DELETE("/conversations/{conversation_id}")
    suspend fun deleteConversation(
        @Path("conversation_id") conversationId: String
    ): Response<Unit>

    @GET("/conversations/{conversation_id}/messages")
    suspend fun getConversationMessages(
        @Path("conversation_id") conversationId: String
    ): Response<List<MessageDto>>

    @POST("/conversations/{conversation_id}/continue")
    suspend fun continueConversation(
        @Path("conversation_id") conversationId: String,
        @Body request: ContinueConversationRequest
    ): Response<MessageDto>

    @Multipart
    @POST("/documents/upload")
    suspend fun uploadDocuments(
        @Part files: List<MultipartBody.Part>
    ): Response<List<DocumentDto>>

    @GET("/documents/{document_id}/details")
    suspend fun getDocumentDetails(
        @Path("document_id") documentId: String
    ): Response<DocumentDto>

    @GET("/documents/me")
    suspend fun getUserDocuments(): Response<List<DocumentDto>>

    @DELETE("/documents/{document_id}")
    suspend fun deleteDocument(
        @Path("document_id") documentId: String
    ): Response<Unit>
}