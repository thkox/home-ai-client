package com.thkox.homeai.domain.utils

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val field: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, field: String? = null, data: T? = null) :
        Resource<T>(data, message, field)
}
