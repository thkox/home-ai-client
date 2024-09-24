package com.thkox.homeai.domain.usecase.user

import com.thkox.homeai.domain.repository.AuthRepository
import com.thkox.homeai.domain.util.Resource

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Resource<Unit> {
        val result = authRepository.login(email, password)
        return if (result is Resource.Success) {
            Resource.Success(Unit)
        } else {
            Resource.Error(result.message ?: "Unknown error")
        }
    }
}