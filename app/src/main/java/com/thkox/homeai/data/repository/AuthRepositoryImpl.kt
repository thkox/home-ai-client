package com.thkox.homeai.data.repository

import com.thkox.homeai.data.models.UserCreateRequest
import com.thkox.homeai.data.sources.remote.ApiService
import com.thkox.homeai.data.sources.remote.RetrofitHolder
import com.thkox.homeai.domain.models.Token
import com.thkox.homeai.domain.models.User
import com.thkox.homeai.domain.models.UserRegistration
import com.thkox.homeai.domain.repository.AuthRepository
import com.thkox.homeai.domain.repository.TokenRepository
import com.thkox.homeai.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val retrofitHolder: RetrofitHolder,
    private val tokenRepository: TokenRepository
) : AuthRepository {

    private val apiService: ApiService
        get() = retrofitHolder.getRetrofit().create(ApiService::class.java)

    override suspend fun login(email: String, password: String): Resource<Token> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.login(email, password)
                if (response.isSuccessful) {
                    val tokenDto = response.body()
                    tokenDto?.let {
                        tokenRepository.saveToken(it.accessToken)
                        Resource.Success(Token(it.accessToken, it.tokenType))
                    } ?: Resource.Error("Invalid response")
                } else {
                    Resource.Error("Login failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Resource.Error("Exception: ${e.localizedMessage}")
            }
        }
    }

    override suspend fun register(userRegistration: UserRegistration): Resource<User> {
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
                        Resource.Success(
                            User(
                                userId = it.userId,
                                firstName = it.firstName,
                                lastName = it.lastName,
                                email = it.email,
                                enabled = it.enabled,
                                role = it.role
                            )
                        )
                    } ?: Resource.Error("Invalid response")
                } else {
                    Resource.Error("Registration failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Resource.Error("Exception: ${e.localizedMessage}")
            }
        }
    }
}
