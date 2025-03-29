package com.justpe.justpy1.HotelCityScreen

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.justpe.justpy1.city_search_hotel.HotelAdapter.CityListAdapter
import com.justpe.justpy1.city_search_hotel.HotelModel.CityListModel
import com.justpe.justpy1.databinding.CitySearchHotelBinding

class SearchHotel : AppCompatActivity() {
    private lateinit var binding: CitySearchHotelBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var hotelAdapter: CityListAdapter
    private var hotelList: MutableList<CityListModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CitySearchHotelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.searchView.setQueryHint("Enter City/Location/Hotel Name")

        binding.backprash.setOnClickListener {
            moveTaskToBack(true)
            finish()
        }

        sharedPreferences = getSharedPreferences("citySearch", MODE_PRIVATE)
        val savedHotels = loadDataFromSharedPreferences()

        if (savedHotels.isNotEmpty()) {
            hotelList.addAll(savedHotels)
            hotelAdapter = CityListAdapter(hotelList, this)
            binding.recyclerView.adapter = hotelAdapter
        } else {
            Log.e("SharedPreferences", "Empty list")
        }

        binding.searchView.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterHotels(newText)
                return true
            }
        })
    }

    private fun loadDataFromSharedPreferences(): List<CityListModel> {
        val gson = Gson()
        val json = sharedPreferences.getString("hotel_list", null)
        val type = object : TypeToken<List<CityListModel>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    private fun filterHotels(query: String?) {
        val filteredList = hotelList.filter {
            it.City.contains(query ?: "", ignoreCase = true) ||
                    it.country.contains(query ?: "", ignoreCase = true)
        }
        hotelAdapter.updateList(filteredList)
    }
}
