package com.thkox.homeai.domain.usecase

import com.thkox.homeai.data.sources.local.SharedPreferencesManager
import com.thkox.homeai.presentation.viewModel.welcome.EnterServerAddressState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import javax.inject.Inject

class EnterServerAddressUseCase @Inject constructor(
    private val sharedPreferencesManager: SharedPreferencesManager
) {
    suspend fun invoke(address: String): EnterServerAddressState {
        val formattedAddress = formatAddress(address)
        return withContext(Dispatchers.IO) {
            if (isFastApiApp(formattedAddress)) {
                sharedPreferencesManager.saveBaseUrl(formattedAddress)
                EnterServerAddressState.Success
            } else {
                EnterServerAddressState.Error("Invalid server address or not a FastAPI app")
            }
        }
    }

    private fun formatAddress(address: String): String {
        val defaultPort = 8000
        val regex = Regex("^(https?://)?([^:/]+)(:\\d+)?$")
        val matchResult = regex.matchEntire(address)

        return if (matchResult != null) {
            val (protocol, domain, port) = matchResult.destructured
            val finalProtocol = if (protocol.isEmpty()) "https://" else protocol
            val finalPort = if (port.isEmpty()) ":$defaultPort" else port
            "$finalProtocol$domain$finalPort"
        } else {
            "https://$address:$defaultPort"
        }
    }

    private fun isFastApiApp(address: String): Boolean {
        val client = OkHttpClient()
        val httpsRequest = Request.Builder().url(address).build()
        val httpRequest = Request.Builder().url(address.replace("https://", "http://")).build()

        return try {
//            // This is a workaround for the issue with the FastAPI app not being able to handle HTTPS requests
//            val httpsResponse = client.newCall(httpsRequest).execute()
//            if (httpsResponse.isSuccessful && httpsResponse.body?.string()?.contains("Home AI API") == true) {
//                true
//            } else {
//                val httpResponse = client.newCall(httpRequest).execute()
//                httpResponse.isSuccessful && httpResponse.body?.string()?.contains("Home AI API") == true
//            }
            val httpResponse = client.newCall(httpRequest).execute()
            httpResponse.isSuccessful && httpResponse.body?.string()?.contains("Home AI API") == true
        } catch (e: IOException) {
            false
        }
    }
}