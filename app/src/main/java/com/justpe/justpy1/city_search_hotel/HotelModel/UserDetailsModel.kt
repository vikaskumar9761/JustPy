package com.justpe.justpy1.city_search_hotel.HotelModel

data class UserDetailsModel(
    val rooms: Int,
    val adults: Int,
    val children: Int,
    val childAges: List<Int>
)
