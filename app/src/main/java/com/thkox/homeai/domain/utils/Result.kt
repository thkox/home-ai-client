package com.thkox.homeai.domain.utils

sealed class Result<T>(
    val data: T? = null,
    val message: String? = null,
    val field: String? = null
) {
    class Success<T>(data: T) : Result<T>(data)
    class Error<T>(message: String, field: String? = null, data: T? = null) :
        Result<T>(data, message, field)

    class Loading<T>(data: T? = null) : Result<T>(data)
}