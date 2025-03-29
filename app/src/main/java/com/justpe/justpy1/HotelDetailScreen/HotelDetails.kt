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
import com.justpe.justpy1.city_search_hotel.retrofitClient.RetrofitDetailsClient
import com.justpe.justpy1.databinding.HotelDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HotelDetails : AppCompatActivity() {
    private lateinit var binding: HotelDetailsBinding
    private var selectedButton: Button? = null // Keeps track of the previously selected button
    private lateinit var hotelSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = HotelDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hotelSharedPreferences = getSharedPreferences("hotelDetails", MODE_PRIVATE)

        // Get data from the Hotel List screen (passed via Intent)
        val hotelName = intent.getStringExtra("hotelName") ?: "N/A"
        val location = intent.getStringExtra("location") ?: "N/A"
        val address = intent.getStringExtra("address") ?: "N/A"
        val rating = intent.getStringExtra("rating") ?: "N/A"
        val price = intent.getStringExtra("price") ?: "N/A"
        val EMTCommonID = intent.getStringExtra("EMTCommonID") ?: "N/A"



        // Set text values in the UI from received data
        binding.tvHotelName.text = hotelName
        binding.tvLocation.text = location
        binding.tvRating.text = rating
        binding.tvAddress.text = address

        // Set images in the Image Slider
        val imageUrls = intent.getStringArrayListExtra("imageUrls")
            ?: arrayListOf("https://your-default-image-url.com/default.jpg")
        val imageSlider: ImageSlider = binding.imageSlide
        val slideModels = imageUrls.map { SlideModel(it, ScaleTypes.FIT) }
        imageSlider.setImageList(slideModels)
        imageSlider.startSliding(3000)

        // Load the default fragment (RoomFragment)
        replaceFragment(RoomFragment())
        updateButton(binding.btRoom) // Highlight the "Room" button as selected by default

        // Button click listeners to switch fragments
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

        fetchDetails(EMTCommonID)
    }

    // Function to replace the current fragment with a new fragment
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    // Function to update the button colors when a new button is selected
    private fun updateButton(newSelectedButton: Button) {
        selectedButton?.setBackgroundColor(
            ContextCompat.getColor(this, R.color.white_A700)
        ) // Reset previous button color
        newSelectedButton.setBackgroundColor(
            ContextCompat.getColor(this, R.color.blue_500)
        ) // Highlight the new button
        selectedButton = newSelectedButton
    }

    private fun fetchDetails(EMTCommonID: String) {
        val apiService = RetrofitDetailsClient.instance
        val token = "Bearer eyJhbGciOiJIUzUxMiJ9..."

        apiService.getSheredDetail("api/hotels/details/${EMTCommonID}?engineId=1", token)
            .enqueue(object : Callback<DetailsModel> {
                override fun onResponse(
                    call: Call<DetailsModel>,
                    response: Response<DetailsModel>,
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val details = response.body()!!
                        saveHotelSharedPreferences(details)
                        Log.e("API_SUCCESS", "Hotel data loaded successfully: $details")
                        Toast.makeText(this@HotelDetails, "Hotel data loaded successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        try {
                            Log.e("API_DETAILS", "Response failed: ${response.errorBody()?.string()}")
                        } catch (e: Exception) {
                            Log.e("API_DETAILS", "Error parsing error response: ${e.message}")
                        }
                        Toast.makeText(this@HotelDetails, "Response details failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DetailsModel>, t: Throwable) {
                    Log.e("API_ERROR", "Error: ${t.message}")
                    Toast.makeText(this@HotelDetails, "API call failed", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun saveHotelSharedPreferences(hotelDetails: DetailsModel) {
        val sharedPreferences = getSharedPreferences("hotelDetails", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(hotelDetails)
        editor.putString("hotel_Details", json)
        editor.apply()
        Log.d("SharedPreferences", "Hotel details saved successfully")
    }
}
