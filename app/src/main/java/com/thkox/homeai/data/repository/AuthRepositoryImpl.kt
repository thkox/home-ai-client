package com.thkox.homeai.data.repository

import com.thkox.homeai.data.models.ChangePassword
import com.thkox.homeai.data.models.UserCreateRequest
import com.thkox.homeai.data.models.UserUpdateProfile
import com.thkox.homeai.data.sources.remote.ApiService
import com.thkox.homeai.data.sources.remote.RetrofitHolder
import com.thkox.homeai.domain.models.PasswordChange
import com.thkox.homeai.domain.models.Token
import com.thkox.homeai.domain.models.User
import com.thkox.homeai.domain.models.UserProfileUpdate
import com.thkox.homeai.domain.models.UserRegistration
import com.thkox.homeai.domain.repository.AuthRepository
import com.thkox.homeai.domain.repository.TokenRepository
import com.thkox.homeai.domain.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val retrofitHolder: RetrofitHolder,
    private val tokenRepository: TokenRepository
) : AuthRepository {

    private val apiService: ApiService
        get() = retrofitHolder.getRetrofit().create(ApiService::class.java)

    override suspend fun login(email: String, password: String): Result<Token> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.login(email, password)
                if (response.isSuccessful) {
                    val tokenDto = response.body()
                    tokenDto?.let {
                        tokenRepository.saveToken(it.accessToken)
                        Result.Success(Token(it.accessToken, it.tokenType))
                    } ?: Result.Error("Invalid response")
                } else {
                    Result.Error("Login failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Result.Error("Exception: ${e.localizedMessage}")
            }
        }
    }

    override suspend fun register(userRegistration: UserRegistration): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val request = UserCreateRequest(
                    firstName = userRegistration.firstName,
                    lastName = userRegistration.lastName,
                    username = userRegistration.username,
                    password = userRegistration.password
                )
                val response = apiService.registerUser(request)
                if (response.isSuccessful) {
                    val userDto = response.body()
                    userDto?.let {
                        Result.Success(
                            User(
                                userId = it.userId,
                                firstName = it.firstName,
                                lastName = it.lastName,
                                email = it.email,
                                enabled = it.enabled,
                                role = it.role
                            )
                        )
                    } ?: Result.Error("Invalid response")
                } else {
                    Result.Error("Registration failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Result.Error("Exception: ${e.localizedMessage}")
            }
        }
    }

    override suspend fun getUserDetails(): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getUserDetails()
                if (response.isSuccessful) {
                    val userDto = response.body()
                    userDto?.let {
                        Result.Success(
                            User(
                                userId = it.userId,
                                firstName = it.firstName,
                                lastName = it.lastName,
                                email = it.email,
                                enabled = it.enabled,
                                role = it.role
                            )
                        )
                    } ?: Result.Error("Invalid response")
                } else {
                    Result.Error(
                        "Failed to fetch user details: ${
                            response.errorBody()?.string()
                        }"
                    )
                }
            } catch (e: Exception) {
                Result.Error("Exception: ${e.localizedMessage}")
            }
        }
    }

    override suspend fun updateProfile(profileUpdate: UserProfileUpdate): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val request = UserUpdateProfile(
                    firstName = profileUpdate.firstName,
                    lastName = profileUpdate.lastName,
                    email = profileUpdate.email
                )
                val response = apiService.updateMyProfile(request)
                if (response.isSuccessful) {
                    val userDto = response.body()
                    userDto?.let {
                        val user = User(
                            userId = it.userId,
                            firstName = it.firstName,
                            lastName = it.lastName,
                            email = it.email,
                            enabled = it.enabled,
                            role = it.role
                        )
                        Result.Success(user)
                    } ?: Result.Error("Invalid response")
                } else {
                    Result.Error("Update profile failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Result.Error("Exception: ${e.localizedMessage}")
            }
        }
    }

    override suspend fun changePassword(passwordChange: PasswordChange): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val request = ChangePassword(
                    oldPassword = passwordChange.oldPassword,
                    newPassword = passwordChange.newPassword
                )
                val response = apiService.changeMyPassword(request)
                if (response.isSuccessful) {
                    Result.Success(Unit)
                } else {
                    Result.Error("Change password failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Result.Error("Exception: ${e.localizedMessage}")
            }
        }
    }
}