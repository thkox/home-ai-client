package com.thkox.homeai.domain.usecase.user

import com.thkox.homeai.domain.models.PasswordChange
import com.thkox.homeai.domain.repository.AuthRepository
import com.thkox.homeai.domain.utils.Resource
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(passwordChange: PasswordChange): Resource<Unit> {
        return authRepository.changePassword(passwordChange)
    }
}