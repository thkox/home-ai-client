package com.thkox.homeai.di

import android.content.Context
import android.content.SharedPreferences
import com.thkox.homeai.data.sources.remote.ApiService
import com.thkox.homeai.data.sources.remote.AuthInterceptor
import com.thkox.homeai.data.sources.remote.RetrofitHolder
import com.thkox.homeai.domain.repository.TokenRepository
import com.thkox.homeai.data.repository.AuthRepositoryImpl
import com.thkox.homeai.data.repository.ConversationRepositoryImpl
import com.thkox.homeai.data.repository.DocumentRepositoryImpl
import com.thkox.homeai.data.repository.TokenRepositoryImpl
import com.thkox.homeai.data.sources.local.SharedPreferencesManager
import com.thkox.homeai.domain.repository.AuthRepository
import com.thkox.homeai.domain.repository.ConversationRepository
import com.thkox.homeai.domain.repository.DocumentRepository
import com.thkox.homeai.domain.usecase.DeleteConversationUseCase
import com.thkox.homeai.domain.usecase.EnterServerAddressUseCase
import com.thkox.homeai.domain.usecase.GetConversationMessagesUseCase
import com.thkox.homeai.domain.usecase.GetUserConversationsUseCase
import com.thkox.homeai.domain.usecase.SendMessageUseCase
import com.thkox.homeai.domain.usecase.user.LoginUseCase
import com.thkox.homeai.domain.usecase.user.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideTokenProvider(sharedPreferences: SharedPreferences): TokenRepository {
        return TokenRepositoryImpl(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesManager(sharedPreferences: SharedPreferences): SharedPreferencesManager {
        return SharedPreferencesManager(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenRepository: TokenRepository): AuthInterceptor {
        return AuthInterceptor(tokenRepository)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)  // Increase connection timeout
            .readTimeout(120, TimeUnit.SECONDS)    // Increase read timeout
            .writeTimeout(60, TimeUnit.SECONDS)    // Increase write timeout
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitHolder(
        okHttpClient: OkHttpClient,
        sharedPreferencesManager: SharedPreferencesManager
    ): RetrofitHolder {
        return RetrofitHolder(okHttpClient, sharedPreferencesManager)
    }

    @Provides
    @Singleton
    fun provideApiService(retrofitHolder: RetrofitHolder): ApiService {
        return retrofitHolder.getRetrofit().create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        retrofitHolder: RetrofitHolder,
        tokenRepository: TokenRepository
    ): AuthRepository {
        return AuthRepositoryImpl(retrofitHolder, tokenRepository)
    }

    @Provides
    @Singleton
    fun provideConversationRepository(apiService: ApiService): ConversationRepository {
        return ConversationRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideDocumentRepository(apiService: ApiService): DocumentRepository {
        return DocumentRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideEnterServerAddressUseCase(
        sharedPreferencesManager: SharedPreferencesManager,
        retrofitHolder: RetrofitHolder
    ): EnterServerAddressUseCase {
        return EnterServerAddressUseCase(sharedPreferencesManager, retrofitHolder)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase {
        return LoginUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(
        authRepository: AuthRepository,
        loginUseCase: LoginUseCase
    ): RegisterUseCase {
        return RegisterUseCase(authRepository, loginUseCase)
    }

    @Provides
    @Singleton
    fun provideSendMessageUseCase(conversationRepository: ConversationRepository): SendMessageUseCase {
        return SendMessageUseCase(conversationRepository)
    }

    @Provides
    @Singleton
    fun providesGetUserConversationsUseCase(conversationRepository: ConversationRepository): GetUserConversationsUseCase {
        return GetUserConversationsUseCase(conversationRepository)
    }

    @Provides
    @Singleton
    fun provideGetConversationMessagesUseCase(conversationRepository: ConversationRepository): GetConversationMessagesUseCase {
        return GetConversationMessagesUseCase(conversationRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteConversationUseCase(conversationRepository: ConversationRepository): DeleteConversationUseCase {
        return DeleteConversationUseCase(conversationRepository)
    }
}