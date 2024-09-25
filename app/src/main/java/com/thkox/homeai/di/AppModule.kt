package com.thkox.homeai.di

import android.content.Context
import android.content.SharedPreferences
import com.thkox.homeai.data.api.ApiService
import com.thkox.homeai.data.api.AuthInterceptor
import com.thkox.homeai.data.api.TokenProvider
import com.thkox.homeai.data.repository.AuthRepositoryImpl
import com.thkox.homeai.data.repository.TokenProviderImpl
import com.thkox.homeai.data.sources.local.SharedPreferencesManager
import com.thkox.homeai.domain.repository.AuthRepository
import com.thkox.homeai.domain.usecase.EnterServerAddressUseCase
import com.thkox.homeai.domain.usecase.user.LoginUseCase
import com.thkox.homeai.domain.usecase.user.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun provideTokenProvider(sharedPreferences: SharedPreferences): TokenProvider {
        return TokenProviderImpl(sharedPreferences)
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
    fun provideAuthInterceptor(tokenProvider: TokenProvider): AuthInterceptor {
        return AuthInterceptor(tokenProvider)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        sharedPreferencesManager: SharedPreferencesManager
    ): Retrofit {
        var baseUrl = sharedPreferencesManager.getBaseUrl()
        if (baseUrl.isNullOrEmpty()) {
            baseUrl = "http://default_base_url"
        } else if (!baseUrl.startsWith("http://") && !baseUrl.startsWith("https://")) {
            baseUrl = "http://$baseUrl"
        }
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        apiService: ApiService,
        tokenProvider: TokenProvider
    ): AuthRepository {
        return AuthRepositoryImpl(apiService, tokenProvider)
    }

    @Provides
    @Singleton
    fun provideEnterServerAddressUseCase(
        sharedPreferencesManager: SharedPreferencesManager,
        apiService: ApiService
    ): EnterServerAddressUseCase {
        return EnterServerAddressUseCase(sharedPreferencesManager, apiService)
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
}