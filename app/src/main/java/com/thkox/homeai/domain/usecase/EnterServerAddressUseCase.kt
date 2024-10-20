package com.thkox.homeai.domain.usecase

import com.thkox.homeai.data.sources.local.SharedPreferencesManager
import com.thkox.homeai.data.sources.remote.ApiService
import com.thkox.homeai.data.sources.remote.RetrofitHolder
import com.thkox.homeai.domain.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EnterServerAddressUseCase @Inject constructor(
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val retrofitHolder: RetrofitHolder
) {
    suspend fun invoke(address: String): Result<Unit> {
        val formattedAddress = formatAddress(address)
        return withContext(Dispatchers.IO) {
            try {
                val apiService = retrofitHolder.getRetrofit().create(ApiService::class.java)
                val response = apiService.root(formattedAddress)
                if (response.isSuccessful && response.body()?.string()
                        ?.contains("Home AI API") == true
                ) {
                    sharedPreferencesManager.saveBaseUrl(formattedAddress)
                    retrofitHolder.updateRetrofit()
                    Result.Success(Unit)
                } else {
                    Result.Error("Invalid server address or not a FastAPI app")
                }
            } catch (e: Exception) {
                Result.Error("Error: ${e.localizedMessage}")
            }
        }
    }

    private fun formatAddress(address: String): String {
        val defaultPort = 8000
        val regex = Regex("^(https?://)?([^:/]+)(:\\d+)?$")
        val matchResult = regex.matchEntire(address)

        return if (matchResult != null) {
            val (protocol, domain, port) = matchResult.destructured
            val finalProtocol = if (protocol.isEmpty()) "http://" else protocol
            val finalPort = if (port.isEmpty()) ":$defaultPort" else port
            "$finalProtocol$domain$finalPort"
        } else {
            "http://$address:$defaultPort"
        }
    }
}