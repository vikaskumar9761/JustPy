package com.justpe.justpy1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.justpe.justpy1.Hotel_List_Screen.Hotel_List
import com.justpe.justpy1.city_search_hotel.HotelModel.CityListModel
import com.justpe.justpy1.city_search_hotel.HotelModel.hotelListModel
import com.justpe.justpy1.city_search_hotel.retrofitClient.RetrofitCityClient
import com.justpe.justpy1.city_search_hotel.retrofitClient.RetrofitHotelClient
import com.justpe.justpy1.HotelCityScreen.SearchHotel
import com.justpe.justpy1.databinding.HotelMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: HotelMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesHotel: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HotelMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("citySearch", MODE_PRIVATE)
        sharedPreferencesHotel = getSharedPreferences("hotelSearch", MODE_PRIVATE)

        binding.progressBar.visibility = View.GONE

        val city = intent.getStringExtra("city") ?: "City"
        val country = intent.getStringExtra("country") ?: "Country"

        binding.cityTextView.text = city
        binding.countryTextView.text = country

        binding.selectCity.setOnClickListener {
            startActivity(Intent(this, SearchHotel::class.java))
            finish()
        }

        binding.searchButton.setOnClickListener {
            if (city == "City") {
                Toast.makeText(this, "Please Select City", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, Hotel_List::class.java)
                intent.putExtra("hotel_name", city)
                startActivity(intent)
            }
        }

        fetchHotels(city)
    }

    private fun fetchHotels(city: String) {
        val apiService = RetrofitCityClient.instance
        val token = "Bearer eyJhbGciOiJIUzUxMiJ9..."

        binding.progressBar.visibility = View.VISIBLE

        apiService.getHotels("api/hotels/search/city/delhi", token)
            .enqueue(object : Callback<List<CityListModel>> {
                override fun onResponse(call: Call<List<CityListModel>>, response: Response<List<CityListModel>>) {
                    binding.progressBar.visibility = View.GONE
                    if (response.isSuccessful && response.body() != null) {
                        val cities = response.body() ?: emptyList()
                        saveDataToSharedPreferences(cities)
                        getHotelList(city)
                    } else {
                        Log.e("API_ERROR2", "Response failed: ${response.errorBody()?.string()}")
                        Toast.makeText(this@MainActivity, "Response failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<CityListModel>>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    Log.e("API_ERROR", "Error: ${t.message}")
                    Toast.makeText(this@MainActivity, "API call failed", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun saveDataToSharedPreferences(hotelList: List<CityListModel>) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(hotelList)
        editor.putString("hotel_list", json)
        editor.apply()
    }

    private fun getHotelList(city: String) {
        val apiHotelService = RetrofitHotelClient.instance
        val token = "Bearer eyJhbGciOiJIUzUxMiJ9..."

        binding.progressBar.visibility = View.VISIBLE

        apiHotelService.getSearchHotel("api/hotels/city/$city", token)
            .enqueue(object : Callback<hotelListModel> {
                override fun onResponse(call: Call<hotelListModel>, response: Response<hotelListModel>) {
                    binding.progressBar.visibility = View.GONE
                    if (response.isSuccessful && response.body() != null) {
                        val cities = response.body()!!
                        Log.e("API_SUCCESS", "Hotel data loaded successfully: $cities")
                        saveDataToSharedPreferences1(cities)
                    } else {
                        Log.e("API_ERROR2", "Response failed: ${response.errorBody()?.string()}")
                        Toast.makeText(this@MainActivity, "Response failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<hotelListModel>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    Log.e("API_ERROR", "Error: ${t.message}")
                    Toast.makeText(this@MainActivity, "API call failed", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun saveDataToSharedPreferences1(hotelList: hotelListModel) {
        val sharedPreferences = getSharedPreferences("HotelPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(hotelList)
        editor.putString("hotel_list", json)
        editor.apply()
        Log.d("SharedPrefs", "Hotel data saved: $json")
    }
}
