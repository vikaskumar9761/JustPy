package com.justpe.justpy1.city_search_hotel.retrofitClient
import com.justpe.justpy1.city_search_hotel.API_call.ApiRoomsService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitRoomClient {
    private const val BASE_URL = "https://justb2c.grahaksathi.com/"  // अपने API का बेस URL डालें

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val instance: ApiRoomsService  by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRoomsService::class.java)
    }
}
