package com.thkox.homeai.domain.usecase.user

import com.thkox.homeai.domain.models.User
import com.thkox.homeai.domain.repository.AuthRepository
import com.thkox.homeai.domain.utils.Result
import javax.inject.Inject

class GetUserDetailsUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<User> {
        return authRepository.getUserDetails()
    }
}