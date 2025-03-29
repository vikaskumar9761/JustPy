package com.justpe.justpy1.city_search_hotel.HotelModel


data class DetailsModel(
    val HotelImagesDetail: List<HotelImageDetail>,
    val RoomImageList: List<RoomImage>,
    val address: String,
    val city: String,
    val country: String,
    val lat: String,
    val lon: String,
    val nUrl: String,
    val name: String,
    val nurl: String,
    val rating: String,
    val zipcode: String
){
    data class HotelImageDetail(
        val EngineType: Int,
        val ID: String,
        val ImageCategory: String,
        val ImageDescription: String,
        val Url: String,
    )
    data class RoomImage(
        val EngineType: Int,
        val ID: String,
        val RoomType: String,
        val Url: String,
    )
}