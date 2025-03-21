package com.justpe.justpy1.city_search_hotel

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.justpe.justpy1.MainActivity
import com.justpe.justpy1.R
import com.justpe.justpy1.city_search_hotel.HotelAdapter.HotelAdapter
import com.justpe.justpy1.city_search_hotel.dataApi.Hotel

class searchHotel : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var hotelAdapter: HotelAdapter
    private var hotelList: MutableList<Hotel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.city_search_hotel)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize SearchView
        searchView = findViewById(R.id.searchView)
        searchView.setQueryHint("Enter City/Location/Hotel Name")

        // Handle back button click
        findViewById<ImageView>(R.id.backprash).setOnClickListener {
         moveTaskToBack(true)
            finish()
        }

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("citySearch", MODE_PRIVATE)

        // Load hotel data from SharedPreferences
        val savedHotels = loadDataFromSharedPreferences()

        // Check if hotel list is not empty and set data to RecyclerView
        if (savedHotels.isNotEmpty()) {
            hotelList.addAll(savedHotels) // Add loaded hotels to hotelList
            hotelAdapter = HotelAdapter(hotelList, this) // Initialize adapter
            recyclerView.adapter = hotelAdapter // Set adapter to RecyclerView
        } else {
            Log.e("SharedPreferences", "Empty list") // Log if no data found
        }

        // Implement SearchView functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false // No action needed when query is submitted
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Call function to filter hotels based on search query
                filterHotels(newText)
                return true
            }
        })
    }

    // Function to load hotel data from SharedPreferences
    private fun loadDataFromSharedPreferences(): List<Hotel> {
        val gson = Gson()
        val json = sharedPreferences.getString("hotel_list", null)
        val type = object : TypeToken<List<Hotel>>() {}.type
        return gson.fromJson(json, type) ?: emptyList() // Return parsed list or empty list if null
    }

    // Function to filter hotels based on city or country
    private fun filterHotels(query: String?) {
        val filteredList = hotelList.filter {
            it.City.contains(query ?: "", ignoreCase = true) || // Match city name
                    it.country.contains(query ?: "", ignoreCase = true) // Match country name
        }
        hotelAdapter.updateList(filteredList) // Update adapter with filtered results
    }
}
