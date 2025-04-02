package com.justpe.justpy1.HotelDetailScreen

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.Gson
import com.justpe.justpy1.HotelDetailScreen.FragmentScreen.DetailsFragment
import com.justpe.justpy1.HotelDetailScreen.FragmentScreen.OverviewFragment
import com.justpe.justpy1.HotelDetailScreen.FragmentScreen.RoomFragment
import com.justpe.justpy1.R
import com.justpe.justpy1.city_search_hotel.HotelModel.DetailsModel
import com.justpe.justpy1.city_search_hotel.HotelModel.HotelRequestBody
import com.justpe.justpy1.city_search_hotel.HotelModel.RoomDetailsResponse
import com.justpe.justpy1.city_search_hotel.HotelModel.UserDetailsModel
import com.justpe.justpy1.city_search_hotel.retrofitClient.RetrofitDetailsClient
import com.justpe.justpy1.city_search_hotel.retrofitClient.RetrofitRoomClient
import com.justpe.justpy1.databinding.HotelDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HotelDetails : AppCompatActivity() {
    private lateinit var binding: HotelDetailsBinding
    private var selectedButton: Button? = null // To track the currently selected fragment button
    private lateinit var hotelSharedPreferences: SharedPreferences // For storing hotel details
    private lateinit var sharedPreferencesDate: SharedPreferences // For storing check-in/check-out dates

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enables edge-to-edge display

        binding = HotelDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferencesDate = getSharedPreferences("datePic", MODE_PRIVATE)
        hotelSharedPreferences = getSharedPreferences("hotelDetails", MODE_PRIVATE)

        // Load room details from SharedPreferences and display them
        val adultData = loadRoomDetails()
        binding.tvAdults.text = "${adultData.adults} Adults, ${adultData.rooms} Rooms, ${adultData.children} Children"

        // Get hotel details from the intent
        val hotelName = intent.getStringExtra("hotelName") ?: "N/A"
        val location = intent.getStringExtra("location") ?: "N/A"
        val address = intent.getStringExtra("address") ?: "N/A"
        val rating = intent.getStringExtra("rating") ?: "N/A"
        val price = intent.getStringExtra("price") ?: "N/A"
        val EMTCommonID = intent.getStringExtra("EMTCommonID") ?: "N/A"

        // Set hotel details in the UI
        binding.tvHotelName.text = hotelName
        binding.tvLocation.text = location
        binding.tvRating.text = rating
        binding.tvAddress.text = address

        // Load and display hotel images using ImageSlider
        val imageUrls = intent.getStringArrayListExtra("imageUrls") ?: arrayListOf()
        val imageSlider: ImageSlider = binding.imageSlide
        val slideModels = imageUrls.map { SlideModel(it, ScaleTypes.FIT) }
        imageSlider.setImageList(slideModels)
        imageSlider.startSliding(3000)

        // Initialize with RoomFragment and select Room button
        replaceFragment(RoomFragment())
        updateButton(binding.btRoom)

        // Set click listeners for fragment buttons
        binding.btRoom.setOnClickListener {
            replaceFragment(RoomFragment())
            updateButton(binding.btRoom)
        }
        binding.btOverView.setOnClickListener {
            replaceFragment(OverviewFragment())
            updateButton(binding.btOverView)
        }
        binding.btDetails.setOnClickListener {
            replaceFragment(DetailsFragment())
            updateButton(binding.btDetails)
        }


        // Fetch hotel details from the API
        fetchDetails(EMTCommonID)
        fetchHotelDetails(EMTCommonID)
        loadAndSetDates()
    }

    // Function to replace the current fragment in the FrameLayout
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    // Function to update the selected button's background color
    private fun updateButton(newSelectedButton: Button) {
        selectedButton?.setBackgroundColor(ContextCompat.getColor(this, R.color.white_A700))
        newSelectedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_500))
        selectedButton = newSelectedButton
    }

    // Function to fetch hotel details from the API
    private fun fetchDetails(EMTCommonID: String) {
        val apiService = RetrofitDetailsClient.instance
        val token = "Bearer eyJhbGciOiJIUzUxMiJ9..." // Replace with your actual token
        Log.d("API_SUCCESSID", "Hotel data loaded successfully: $EMTCommonID")

        apiService.getSheredDetail("api/hotels/details/$EMTCommonID?engineId=1", token)
            .enqueue(object : Callback<DetailsModel> {
                override fun onResponse(call: Call<DetailsModel>, response: Response<DetailsModel>) {
                    if (response.isSuccessful && response.body() != null) {
                        val details = response.body()!!
                        saveHotelSharedPreferences(details)
                        val imageUrls = details.HotelImagesDetail?.map { it.Url } ?: emptyList()
                        setImageSlider(imageUrls)
                        Log.d("API_SUCCESS", "Hotel data loaded successfully: ${Gson().toJson(details)}")

                        // Notify RoomFragment to load data from SharedPreferences
                        val fragment = supportFragmentManager.findFragmentById(R.id.frameLayout)
                        if (fragment is RoomFragment) {
                            fragment.loadHotelDataFromSharedPreferences()
                        }
                    } else {
                        Log.e("API_DETAILS", "Response failed: ${response.errorBody()?.string()}")
                        Toast.makeText(this@HotelDetails, "Response details failed", Toast.LENGTH_SHORT).show()
                        clearHotelSharedPreferences() // Clear old data on API failure
                    }
                }

                override fun onFailure(call: Call<DetailsModel>, t: Throwable) {
                    Log.e("API_ERROR", "Error: ${t.message}")
                    Toast.makeText(this@HotelDetails, "API call failed", Toast.LENGTH_SHORT).show()
                    clearHotelSharedPreferences() // Clear old data on API failure
                }
            })
    }
    // ✅ Function to set Image URLs in ImageSlider
    private fun setImageSlider(imageUrls: List<String>) {
        val imageSlider: ImageSlider = binding.imageSlide
        val slideModels = imageUrls.map { SlideModel(it, ScaleTypes.FIT) }
        imageSlider.setImageList(slideModels)
        imageSlider.startSliding(1500) // 3 सेकंड बाद ऑटो स्लाइडिंग होगी
    }

    // Function to save hotel details to SharedPreferences
    private fun saveHotelSharedPreferences(hotelDetails: DetailsModel) {
        val sharedPreferences = getSharedPreferences("hotelDetails", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(hotelDetails)
        editor.putString("hotel_Details", json)
        editor.apply()
        Log.d("SharedPreferences", "Hotel details saved successfully")
    }

    // Function to load room details from SharedPreferences
    private fun loadRoomDetails(): UserDetailsModel {
        val sharedPreferences = getSharedPreferences("RoomDetails", MODE_PRIVATE)
        val rooms = sharedPreferences.getInt("rooms", 1)
        val adults = sharedPreferences.getInt("adults", 1)
        val children = sharedPreferences.getInt("children", 0)
        val childAgesJson = sharedPreferences.getString("child_ages", "[]")
        val childAges = Gson().fromJson(childAgesJson, Array<Int>::class.java)?.toList() ?: emptyList()
        return UserDetailsModel(rooms, adults, children, childAges)
    }

    // Function to clear hotel details from SharedPreferences
    private fun clearHotelSharedPreferences() {
        val sharedPreferences = getSharedPreferences("hotelDetails", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("hotel_Details", "empty")
        editor.apply()
        Log.d("SharedPreferences", "Hotel details cleared due to API failure")
    }

    // Function to fetch hotel room details from the API
    private fun fetchHotelDetails(EMTCommonID: String) {
        val adultData = loadRoomDetails()
        val dates = loadAndSetDates()
        val requestBody = HotelRequestBody(
            checkInDate = "${dates.first}", // Replace with dynamic dates 2025-10-08
            checkOutDate = "${dates.second}", // Replace with dynamic dates 2025-10-12
            hotelId = EMTCommonID,
            currency = "USD",
            nationality = "AE",
            rooms = listOf(HotelRequestBody.RoomRequest(adultData.adults, adultData.children, adultData.childAges))
        )

        RetrofitRoomClient.instance.getHotelDetails(requestBody)
            .enqueue(object : Callback<RoomDetailsResponse> {
                override fun onResponse(call: Call<RoomDetailsResponse>, response: Response<RoomDetailsResponse>) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data != null) {
                            saveHotelDetailsToSharedPreferences(data)
                            Log.d("HOTEL_DATA", "Total Hotels: ${data.hotellist}")
                            data.hotellist.forEach { hotel ->
                                Log.d("HOTEL_NAME", "Hotel Name: ${hotel.hotelName}")
                            }
                        }
                    } else {
                        Log.e("API_ERROR", "Response Code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<RoomDetailsResponse>, t: Throwable) {
                    Log.e("API_FAILURE", "Error: ${t.message}")
                }
            })
    }

    // Function to save hotel room details to SharedPreferences
    private fun saveHotelDetailsToSharedPreferences(hotelDetails: RoomDetailsResponse?) {
        val sharedPreferences = getSharedPreferences("hotelDetails1", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(hotelDetails)
        editor.putString("hotel_details1", json)
        editor.apply()
        Log.d("SharedPreferences", "Hotel details saved successfully")
    }

    //Function to load and display check-in/check-out dates.
    private fun loadAndSetDates(): Pair<String?, String?> {
        val checkInDate = sharedPreferencesDate.getString("check_in_date", "No Check-in Date")
        val checkOutDate = sharedPreferencesDate.getString("check_out_date", "No Check-out Date")
//        binding.tvCheckIn.setText("Check In Date : $checkInDate")
//        binding.tvCheckOut.setText("Check Out Date : $checkOutDate")
        return Pair(checkInDate,checkOutDate);



    }
}