package com.justpe.justpy1.city_search_hotel.HotelModel
data class RoomDetailsResponse(
    val CheckInDate: String,
    val CheckOutDate: String,
    val HotelCount: Int,
    val Nights: Int,
    val SearchKey: String,
    val TimeSpan: String,
    val TotalHotel: Int,
    val elapsedTime: Double,
    val hotellist: List<Hotellist>
) {
    data class Hotellist(
        val AvailableRoom: List<AvailableRoom>,
        val CashBack: String,
        val Category: String,
        val ChName: String,
        val CheckInTime: String,
        val CheckOutTime: String,
        val Commission: String,
        val CouponCode: Any?,
        val Distance: String,
        val EMTCommonID: String,
        val ET: String,
        val EngineType: Int,
        val HotelHighlights: Any?,
        val HotelURL: String,
        val ImageThumbUrl: String,
        val Inclusions: Any?,
        val IsBreakFast: Boolean,
        val IsCached: Boolean,
        val IsDND: Boolean,
        val IsDefaultValue: Boolean,
        val IsMetaDND: Boolean,
        val IsSafety: Boolean,
        val Location: String,
        val MostViewed: Boolean,
        val MostViewedNo: Int,
        val PromoDescription: Any?,
        val Recommended: Double,
        val SuppDetails: Any?,
        val TotalPrice: Int,
        val address: String,
        val cdValue: Double,
        val city: Any?,
        val countryCode: String,
        val currencyCode: String,
        val dfValue: Double,
        val diffValue: Int,
        val giataId: String,
        val hdiscount: Int,
        val hotelID: String,
        val hotelName: String,
        val htlPlcy: Any?,
        val latitude: String,
        val longtitude: String,
        val nUrl: String,
        val nidhiRating: Any?,
        val nurl: String,
        val plcy: Any?,
        val price: Int,
        val ranking: Any?,
        val rankingoutof: Any?,
        val rating: String,
        val specialOffer: String,
        val strikethrough: Double,
        val surchargeTotal: Int,
        val tripAdvisorRating: Double,
        val tripAdvisorRatingUrl: String,
        val tripAdvisorReviewCount: Double,
        val tripType: Any?,
        val tripid: Any?
    )

    data class AvailableRoom(
        val Adult: Int,
        val Amenity: Any?,
        val BedType: String,
        val BookingPolicy: String,
        val CancellationPolicy: String,
        val CashBack: String,
        val Child: Int,
        val Commission: String,
        val CouponCode: Any?,
        val Description: String,
        val EMTCommonId: String,
        val EMTFee: Int,
        val Engine: Int,
        val HightRate: Any?,
        val HotelDiscount: Int,
        val HotelID: String,
        val Id: Int,
        val IsBreakFast: Boolean,
        val IsDefaultValue: Boolean,
        val IsHoldBooking: Boolean,
        val MarkupValue: Int,
        val Meal: String,
        val MealType: String,
        val NDFValue: Int,
        val NP: Int,
        val NT: Int,
        val Offer: Any?,
        val PayAtHotel: Boolean,
        val Price: Int,
        val Recommended: Int,
        val SalesTax: Int,
        val ServiceFee: Int,
        val TotalPrice: Int,
        val WithCC: Boolean,
        val available_rooms: String,
        val bedid: Any?,
        val cdValue: Int,
        val cfValue: Int,
        val currencyCode: String,
        val dfValue: Int,
        val diffValue: Int,
        val disc: Int,
        val isPayZero: Boolean,
        val payZero: Boolean,
        val promoDescription: String,
        val rateInfo: Any?,
        val rateKey: String,
        val ratePlanCode: String,
        val rm: Any?,
        val roomID: String,
        val roomImage: Any?,
        val roomRateDescription: Any?,
        val roomType: String,
        val roomTypeCode: String,
        val roomplanDescription: Any?,
        val strikethrough: Int,
        val surchargeTotal: Int
    )
}
