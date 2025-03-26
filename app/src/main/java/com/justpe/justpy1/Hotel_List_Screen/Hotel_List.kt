package com.justpe.justpy1.Hotel_List_Screen

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.justpe.justpy1.city_search_hotel.HotelAdapter.HotelListAdapter
import com.justpe.justpy1.city_search_hotel.HotelModel.hotelListModel
import com.justpe.justpy1.databinding.HotelListBinding

class Hotel_List : AppCompatActivity() {

    private lateinit var binding: HotelListBinding
    private lateinit var hotelAdapter: HotelListAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HotelListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve selected city name from intent
        val receivedHotelName = intent.getStringExtra("hotel_name")
        binding.etCity.text = receivedHotelName

        // Handle back button click
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        // Initialize RecyclerView with LinearLayoutManager
        binding.rvHotels.layoutManager = LinearLayoutManager(this)
        hotelAdapter = HotelListAdapter(emptyList(),this)
        binding.rvHotels.adapter = hotelAdapter

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("HotelPrefs", MODE_PRIVATE)

        // Load hotel data from SharedPreferences
        loadHotelDataFromSharedPreferences()
    }

    // Function to load hotel data from SharedPreferences
    private fun loadHotelDataFromSharedPreferences() {
        val gson = Gson()
        val json = sharedPreferences.getString("hotel_list", null)

        if (json != null) {
            try {
                // Deserialize JSON string into hotelListData object
                val type = object : TypeToken<hotelListModel>() {}.type
                val hotelData: hotelListModel = gson.fromJson(json, type)
                val hotelList: List<hotelListModel.HotelData> = hotelData.hotellist

                // Update UI with the number of available properties
                binding.tvPropertyCount.text = "${hotelList.size} Properties Available"

                // Log retrieved data for debugging purposes
                Log.d("SharedPrefs", "Loaded ${hotelList.size} hotels from SharedPreferences")

                // Update RecyclerView with the hotel list
                hotelAdapter.updateList(hotelList)
            } catch (e: Exception) {
                // Handle JSON parsing errors
                Log.e("SharedPrefs", "JSON Parsing Error: ${e.message}")
                Toast.makeText(this, "Data parsing failed", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Display a message if no data is found in SharedPreferences
            Toast.makeText(this, "No data found in SharedPreferences", Toast.LENGTH_SHORT).show()
            Log.e("SharedPrefs", "No hotel data found in SharedPreferences")
        }
    }
}