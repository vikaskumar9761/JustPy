package com.justpe.justpy1.searchHotel

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
import com.justpe.justpy1.R
import com.justpe.justpy1.searchHotel.HotelAdapter.HotelAdapter
import com.justpe.justpy1.searchHotel.dataApi.Hotel

class searchHotel : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var hotelAdapter: HotelAdapter
    private var hotelList: MutableList<Hotel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.city_search_hotel)

        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        searchView.setQueryHint("Enter City/Location/Hotel Name")

        findViewById<ImageView>(R.id.backprash).setOnClickListener {
           onBackPressed()
        }

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        // set data from recycleView

        val savedHotels = loadDataFromSharedPreferences()

        if (savedHotels.isNotEmpty()) {
            hotelList.addAll(savedHotels)
            hotelAdapter = HotelAdapter(hotelList, this)
            recyclerView.adapter = hotelAdapter
        } else {
            Log.e("SharedPreferences", "empty list")
        }

        //  Search Functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //call search city or county function
                filterHotels(newText)
                return true
            }
        })
    }

    //get list from MainActivity to searchHotel
    private fun loadDataFromSharedPreferences(): List<Hotel> {
        val gson = Gson()
        val json = sharedPreferences.getString("hotel_list", null)
        val type = object : TypeToken<List<Hotel>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    //search for city or country function
    private fun filterHotels(query: String?) {
        val filteredList = hotelList.filter {
                    it.City.contains(query ?: "", ignoreCase = true) ||
                    it.country.contains(query ?: "", ignoreCase = true)
        }
        hotelAdapter.updateList(filteredList)
    }
}
