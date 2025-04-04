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
import com.justpe.justpy1.city_search_hotel.HotelModel.UserDetailsModel
import com.justpe.justpy1.city_search_hotel.retrofitClient.RetrofitDetailsClient
import com.justpe.justpy1.databinding.HotelDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HotelDetails : AppCompatActivity() {
    private lateinit var binding: HotelDetailsBinding
    private var selectedButton: Button? = null
    private lateinit var hotelSharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesDate: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = HotelDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SharedPreferences
        sharedPreferencesDate = getSharedPreferences("datePic", MODE_PRIVATE)
        hotelSharedPreferences = getSharedPreferences("hotelDetails", MODE_PRIVATE)

        // ✅ Receive data from intent
        val hotelName = intent.getStringExtra("hotelName") ?: "N/A"
        val location = intent.getStringExtra("location") ?: "N/A"
        val address = intent.getStringExtra("address") ?: "N/A"
        val rating = intent.getStringExtra("rating") ?: "0"

        // ✅ Set data to UI components
        binding.tvHotelName.text = hotelName
        binding.tvLocation.text = location
        binding.tvAddress.text = address
        binding.tvRating.text = rating

        // ✅ Receive hotel ID
        val EMTCommonID = intent.getStringExtra("EMTCommonID") ?: ""
        if (EMTCommonID.isEmpty()) {
            Toast.makeText(this, "Invalid Hotel ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // ✅ Load Room Data
        val adultData = loadRoomDetails()
        binding.tvAdults.text = "${adultData.adults} Adults, ${adultData.rooms} Rooms, ${adultData.children} Children"

        // ✅ Load hotel images
        val imageUrls = intent.getStringArrayListExtra("imageUrls") ?: arrayListOf()
        setImageSlider(imageUrls)

        // ✅ Fetch hotel details from API
        fetchDetails(EMTCommonID)
        loadAndSetDates()

        // ✅ Set default fragment (RoomFragment)
        replaceFragment(RoomFragment().apply {
            arguments = Bundle().apply {
                putString("EMTCommonID", EMTCommonID)
            }
        })

        // ✅ Button click listeners for fragment switching
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
    }

    // ✅ Function to replace the current fragment
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }

    // ✅ Function to update button styles when switching fragments
    private fun updateButton(newSelectedButton: Button) {
        selectedButton?.setBackgroundColor(ContextCompat.getColor(this, R.color.white_A700))
        newSelectedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_500))
        selectedButton = newSelectedButton
    }

    // ✅ Function to fetch hotel details from API
    private fun fetchDetails(EMTCommonID: String) {
        val apiService = RetrofitDetailsClient.instance
        val token = "Bearer your_actual_token"

        apiService.getSheredDetail("api/hotels/details/$EMTCommonID?engineId=1", token)
            .enqueue(object : Callback<DetailsModel> {
                override fun onResponse(call: Call<DetailsModel>, response: Response<DetailsModel>) {
                    if (response.isSuccessful && response.body() != null) {
                        val details = response.body()!!
                        saveHotelSharedPreferences(details)

                        // ✅ Load hotel images from API response
                        val imageUrls = details.HotelImagesDetail?.map { it.Url } ?: emptyList()
                        setImageSlider(imageUrls)

                        Log.d("API_SUCCESS", "Hotel data loaded successfully: ${Gson().toJson(details)}")
                    } else {
                        handleApiError(response)
                    }
                }

                override fun onFailure(call: Call<DetailsModel>, t: Throwable) {
                    Log.e("API_ERROR", "Error: ${t.message}")
                    Toast.makeText(this@HotelDetails, "API call failed", Toast.LENGTH_SHORT).show()
                    clearHotelSharedPreferences()
                }
            })
    }

    // ✅ Function to set images in the image slider
    private fun setImageSlider(imageUrls: List<String>) {
        val imageSlider: ImageSlider = binding.imageSlide

        // ✅ Handle empty URLs
        val filteredImages = imageUrls.filter { it.isNotEmpty() }
        val slideModels = if (filteredImages.isNotEmpty()) {
            filteredImages.map { SlideModel(it, ScaleTypes.FIT) }
        } else {
            listOf(SlideModel(R.drawable.img_globe, ScaleTypes.FIT)) // Default image
        }

        imageSlider.setImageList(slideModels)
        imageSlider.startSliding(1500)
    }

    // ✅ Function to save hotel details in SharedPreferences
    private fun saveHotelSharedPreferences(hotelDetails: DetailsModel) {
        val editor = hotelSharedPreferences.edit()
        editor.putString("hotel_Details", Gson().toJson(hotelDetails))
        editor.commit()
        Log.d("SharedPreferences", "Hotel details saved successfully")
    }

    // ✅ Function to load room details from SharedPreferences
    private fun loadRoomDetails(): UserDetailsModel {
        val sharedPreferences = getSharedPreferences("RoomDetails", MODE_PRIVATE)
        val rooms = sharedPreferences.getInt("rooms", 1)
        val adults = sharedPreferences.getInt("adults", 1)
        val children = sharedPreferences.getInt("children", 0)
        val childAgesJson = sharedPreferences.getString("child_ages", "[]")
        val childAges = Gson().fromJson(childAgesJson, Array<Int>::class.java)?.toList() ?: emptyList()
        return UserDetailsModel(rooms, adults, children, childAges)
    }

    // ✅ Function to clear hotel details from SharedPreferences on API failure
    private fun clearHotelSharedPreferences() {
        val editor = hotelSharedPreferences.edit()
        editor.putString("hotel_Details", "empty")
        editor.commit()
        Log.d("SharedPreferences", "Hotel details cleared due to API failure")
    }

    // ✅ Function to load check-in and check-out dates from SharedPreferences
    private fun loadAndSetDates() {
        val checkInDate = sharedPreferencesDate.getString("check_in_date", "No Check-in Date")
        val checkOutDate = sharedPreferencesDate.getString("check_out_date", "No Check-out Date")
        binding.tvCheckIn.text = "Check In Date : $checkInDate"
        binding.tvCheckOut.text = "Check Out Date : $checkOutDate"
    }

    // ✅ Function to handle API errors
    private fun handleApiError(response: Response<DetailsModel>) {
        Log.e("API_DETAILS", "Response failed: ${response.errorBody()?.string()}")
        Toast.makeText(this@HotelDetails, "Response details failed", Toast.LENGTH_SHORT).show()
        clearHotelSharedPreferences()
    }
}
