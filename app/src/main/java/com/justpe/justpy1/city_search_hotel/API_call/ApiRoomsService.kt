package com.justpe.justpy1.city_search_hotel.API_call


import com.justpe.justpy1.city_search_hotel.HotelModel.HotelRequestBody
import com.justpe.justpy1.city_search_hotel.HotelModel.RoomDetailsResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiRoomsService {
    @Headers("Content-Type: application/json")
    @POST("api/v1/hotels/rooms/search")  // अपना API endpoint यहाँ डालें
    fun getHotelDetails(@Body request: HotelRequestBody): Call<RoomDetailsResponse>

}
