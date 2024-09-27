package com.thkox.homeai.data.api

import com.thkox.homeai.data.sources.local.SharedPreferencesManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitHolder @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val sharedPreferencesManager: SharedPreferencesManager
) {
    @Volatile
    private var retrofit: Retrofit = createRetrofit()

    private fun createRetrofit(): Retrofit {
        var baseUrl = sharedPreferencesManager.getBaseUrl()
        if (baseUrl.isNullOrEmpty()) {
            baseUrl = "http://default_base_url"
        } else if (!baseUrl.startsWith("http://") && !baseUrl.startsWith("https://")) {
            baseUrl = "http://$baseUrl"
        }

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getRetrofit(): Retrofit {
        return retrofit
    }

    fun updateRetrofit() {
        retrofit = createRetrofit()
    }
}
