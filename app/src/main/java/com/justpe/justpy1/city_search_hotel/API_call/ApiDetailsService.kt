package com.justpe.justpy1.city_search_hotel.API_call

import com.justpe.justpy1.city_search_hotel.HotelModel.DetailsModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Url

interface ApiDetailsService {

    @GET
    fun getSheredDetail(
        @Url url:String,
        @Header("Authorization") token:String,
    ):Call<DetailsModel>
}