package com.thkox.homeai.di

import com.thkox.homeai.domain.usecase.EnterServerAddressUseCase
import com.thkox.homeai.domain.usecase.user.LoginUseCase
import com.thkox.homeai.domain.usecase.user.SignUpUseCase
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

    @Provides
    @Singleton
    fun provideSignUpUseCase(): SignUpUseCase {
        return SignUpUseCase()
    }

    @Provides
    @Singleton
    fun provideEnterServerAddressUseCase(): EnterServerAddressUseCase {
        return EnterServerAddressUseCase()
    }
}