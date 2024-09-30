package com.thkox.homeai.domain.usecase

import com.thkox.homeai.data.models.UserCreateRequest
import com.thkox.homeai.data.models.UserResponseDto
import com.thkox.homeai.domain.repository.AuthRepository
import com.thkox.homeai.domain.utils.Resource
import javax.inject.Inject

class UpdateMyProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(userCreateRequest: UserCreateRequest): Resource<UserResponseDto> {
        return authRepository.updateMyProfile(userCreateRequest)
    }
}