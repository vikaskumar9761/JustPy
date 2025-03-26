package com.justpe.justpy1.city_search_hotel.API_call

import com.justpe.justpy1.city_search_hotel.HotelModel.hotelListModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Url

interface ApiHotelService {

    @GET
    fun getSearchHotel(
        @Url url: String,
        @Header("Authorization") token: String
    ): Call<hotelListModel>
}
