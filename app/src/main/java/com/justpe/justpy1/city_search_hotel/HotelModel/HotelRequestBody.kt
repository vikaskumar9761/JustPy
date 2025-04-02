package com.justpe.justpy1.city_search_hotel.HotelModel

data class HotelRequestBody(
    val checkInDate: String,
    val checkOutDate: String,
    val hotelId: String,
    val currency: String,
    val nationality: String,
    val rooms: List<RoomRequest>
) {

    data class RoomRequest(
        val numberOfAdults: Int,
        val numberOfChildren: Int,
        val childAges: List<Int>
    )
}