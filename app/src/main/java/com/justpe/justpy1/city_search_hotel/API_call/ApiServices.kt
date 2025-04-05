package com.justpe.justpy1.city_search_hotel.API_call

import com.justpe.justpy1.city_search_hotel.HotelModel.CityListModel
import com.justpe.justpy1.city_search_hotel.HotelModel.DetailsModel
import com.justpe.justpy1.city_search_hotel.HotelModel.HotelRequestBody
import com.justpe.justpy1.city_search_hotel.HotelModel.RoomDetailsResponse
import com.justpe.justpy1.city_search_hotel.HotelModel.hotelListModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiServices {

    /**
     * Fetches a list of cities available for hotel booking.
     * @param url The endpoint URL.
     * @param token The Authorization token for authentication.
     * @return A Call object containing a list of CityListModel.
     */
    @GET
    fun getHotels(
        @Url url: String,
        @Header("Authorization") token: String
    ): Call<List<CityListModel>>

    /**
     * Fetches detailed information about a specific hotel or room.
     * @param url The endpoint URL.
     * @param token The Authorization token for authentication.
     * @return A Call object containing DetailsModel with hotel/room details.
     */
    @GET
    fun getSheredDetail(
        @Url url: String,
        @Header("Authorization") token: String,
    ): Call<DetailsModel>

    /**
     * Searches for hotels based on a keyword, city, or other search parameters.
     * @param url The endpoint URL.
     * @param token The Authorization token for authentication.
     * @return A Call object containing hotelListModel with search results.
     */
    @GET
    fun getSearchHotel(
        @Url url: String,
        @Header("Authorization") token: String
    ): Call<hotelListModel>

    /**
     * Fetches room details for a specific hotel.
     * @param request The request body containing search parameters like hotel ID, dates, guests, etc.
     * @return A Call object containing RoomDetailsResponse with available rooms.
     */
    @Headers("Content-Type: application/json")
    @POST("api/v1/hotels/rooms/search")
    fun getHotelDetails(@Body request: HotelRequestBody): Call<RoomDetailsResponse>
}
