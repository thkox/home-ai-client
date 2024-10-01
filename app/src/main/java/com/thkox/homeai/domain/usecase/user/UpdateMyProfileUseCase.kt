package com.thkox.homeai.domain.usecase.user

import com.thkox.homeai.domain.models.User
import com.thkox.homeai.domain.models.UserProfileUpdate
import com.thkox.homeai.domain.repository.AuthRepository
import com.thkox.homeai.domain.utils.Resource
import javax.inject.Inject

class UpdateMyProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(profileUpdate: UserProfileUpdate): Resource<User> {
        return authRepository.updateProfile(profileUpdate)
    }
}