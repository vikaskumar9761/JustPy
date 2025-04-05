package com.justpe.justpy1.city_search_hotel.retrofitClient

import com.justpe.justpy1.city_search_hotel.API_call.ApiServices
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitDetailsClient {

    private const val BASE_URL= "https://justb2c.grahaksathi.com/"
    private val client = OkHttpClient.Builder().build()

    val instance: ApiServices by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiServices::class.java)
    }
}