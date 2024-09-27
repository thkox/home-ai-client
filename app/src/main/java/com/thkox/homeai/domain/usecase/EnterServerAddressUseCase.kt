package com.thkox.homeai.domain.usecase

import com.thkox.homeai.data.api.ApiService
import com.thkox.homeai.data.api.RetrofitHolder
import com.thkox.homeai.data.sources.local.SharedPreferencesManager
import com.thkox.homeai.presentation.viewModel.welcome.EnterServerAddressState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EnterServerAddressUseCase @Inject constructor(
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val retrofitHolder: RetrofitHolder
) {
    suspend fun invoke(address: String): EnterServerAddressState {
        val formattedAddress = formatAddress(address)
        return withContext(Dispatchers.IO) {
            try {
                retrofitHolder.updateRetrofit()
                val apiService = retrofitHolder.getRetrofit().create(ApiService::class.java)
                val response = apiService.isFastApiApp(formattedAddress)
                if (response.isSuccessful && response.body()?.string()?.contains("Home AI API") == true) {
                    sharedPreferencesManager.saveBaseUrl(formattedAddress)
                    EnterServerAddressState.Success
                } else {
                    EnterServerAddressState.Error("Invalid server address or not a FastAPI app")
                }
            } catch (e: Exception) {
                EnterServerAddressState.Error("Error: ${e.localizedMessage}")
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
