package com.justpe.justpy1.searchHotel.retrofitClient

import com.justpe.justpy1.searchHotel.API_call.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://justb2c.grahaksathi.com/"

    private val client = OkHttpClient.Builder().build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
}
