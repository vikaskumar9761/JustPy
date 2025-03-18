package com.justpe.justpy1.searchHotel.API_call

import com.justpe.justpy1.searchHotel.dataApi.Hotel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Url

interface ApiService {
    @GET
    fun getHotels(
        @Url url: String,
        @Header("Authorization") token: String
    ): Call<List<Hotel>>
}
