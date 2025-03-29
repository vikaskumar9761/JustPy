package com.justpe.justpy1.city_search_hotel.HotelModel

// Data class representing the structure of hotel list data retrieved from API
data class hotelListModel(
    val hotellist: List<HotelData> // List containing multiple hotels
) {

    // Nested data class representing individual hotel details
    data class HotelData(
        val ImageThumbUrl: String, // URL of the hotel thumbnail image
        val Location: String?, // Location of the hotel (nullable in case it's missing)
        val address: String, // Full address of the hotel
        val hotelName: String , // Name of the hotel
        val rating: String ,//Rating of hotel
        val SurchargeTotal:String,
        val TotalPrice:String,
        val EMTCommonID: String,
    )
}
