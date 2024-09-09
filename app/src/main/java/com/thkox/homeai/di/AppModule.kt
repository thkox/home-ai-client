package com.thkox.homeai.di

import com.thkox.homeai.domain.usecase.user.LoginUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLoginUseCase(): LoginUseCase {
        return LoginUseCase()
    }
}