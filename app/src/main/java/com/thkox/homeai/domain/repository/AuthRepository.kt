package com.thkox.homeai.domain.repository

import com.thkox.homeai.data.models.UserCreateRequest
import com.thkox.homeai.data.models.UserResponseDto
import com.thkox.homeai.domain.models.Token
import com.thkox.homeai.domain.models.User
import com.thkox.homeai.domain.models.UserRegistration
import com.thkox.homeai.domain.utils.Resource

interface AuthRepository {
    suspend fun login(email: String, password: String): Resource<Token>
    suspend fun register(userRegistration: UserRegistration): Resource<User>
    suspend fun getUserDetails(): Resource<User>
    suspend fun updateMyProfile(userCreateRequest: UserCreateRequest): Resource<UserResponseDto>
}