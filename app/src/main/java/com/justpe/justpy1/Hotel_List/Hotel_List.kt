package com.justpe.justpy1.Hotel_List

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.justpe.justpy1.R
import com.justpe.justpy1.city_search_hotel.adapter.HotelListAdapter
import com.justpe.justpy1.city_search_hotel.dataApi.hotelListData

class Hotel_List : AppCompatActivity() {

    // Declare variables for RecyclerView, Adapter, and SharedPreferences
    private lateinit var hotelAdapter: HotelListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hotel_list)
        // Get selected city and country from intent
        val receivedHotelName = intent.getStringExtra("hotel_name")
        findViewById<TextView>(R.id.etCity).text=receivedHotelName

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }
        // Initialize RecyclerView
        recyclerView = findViewById(R.id.rvHotels)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the adapter with an empty list
        hotelAdapter = HotelListAdapter(emptyList())
        recyclerView.adapter = hotelAdapter

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("HotelPrefs", MODE_PRIVATE)

        // Call function to retrieve and load hotel data from SharedPreferences
        loadHotelDataFromSharedPreferences()
    }

    // Function to load hotel data from SharedPreferences
    private fun loadHotelDataFromSharedPreferences() {
        val gson = Gson()
        val json = sharedPreferences.getString("hotel_list", null) // Retrieve JSON string

        if (json != null) {
            try {
                // Convert JSON string to `hotelListData` object
                val type = object : TypeToken<hotelListData>() {}.type
                val hotelData: hotelListData = gson.fromJson(json, type)

                // Extract hotel list from `hotelListData`
                val hotelList: List<hotelListData.HotelData> = hotelData.hotellist

                // Update UI with the number of available properties
                findViewById<TextView>(R.id.tvPropertyCount).text = "${hotelList.size} Properties Available"

                // Log the data for debugging
                Log.d("SharedPrefs", "Loaded ${hotelList.size} hotels from SharedPreferences")

                // Update RecyclerView with the retrieved hotel data
                hotelAdapter.updateList(hotelList)

            } catch (e: Exception) {
                // Handle JSON parsing errors
                Log.e("SharedPrefs", "JSON Parsing Error: ${e.message}")
                Toast.makeText(this, "Data parsing failed", Toast.LENGTH_SHORT).show()
            }
        } else {
            // If no data found in SharedPreferences, show a message
            Toast.makeText(this, "No data found in SharedPreferences", Toast.LENGTH_SHORT).show()
            Log.e("SharedPrefs", "No hotel data found in SharedPreferences")
        }
    }
}
