package com.justpe.justpy1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.justpe.justpy1.Hotel_List.Hotel_List
import com.justpe.justpy1.city_search_hotel.dataApi.Hotel
import com.justpe.justpy1.city_search_hotel.dataApi.hotelListData
import com.justpe.justpy1.city_search_hotel.retrofitClient.RetrofitClient
import com.justpe.justpy1.city_search_hotel.retrofitClient.RetrofitHotelClient
import com.justpe.justpy1.city_search_hotel.searchHotel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    // SharedPreferences for storing hotel search data
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesHotel: SharedPreferences

    // UI elements
    private lateinit var btnNext: LinearLayout
    private lateinit var progressBar: LinearLayout
    private lateinit var search_button: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hotel_main)

        // Initialize UI elements
        search_button = findViewById(R.id.search_button)
        btnNext = findViewById(R.id.selectCity)
        progressBar = findViewById(R.id.progressBar)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("citySearch", MODE_PRIVATE)
        sharedPreferencesHotel = getSharedPreferences("hotelSearch", MODE_PRIVATE)

        // Hide progress bar initially
        progressBar.visibility = View.GONE


        // Get selected city and country from intent
        val city = intent.getStringExtra("city") ?: "City"
        val country = intent.getStringExtra("country") ?: "Country"

        // Set selected city and country to TextViews
        val searchCity = findViewById<TextView>(R.id.cityTextView)
        findViewById<TextView>(R.id.countryTextView).text = country
        searchCity.text = city

        // Open search hotel activity when clicking on select city
        btnNext.setOnClickListener {
            startActivity(Intent(this, searchHotel::class.java))
            finish()
        }

        // Open hotel list activity when clicking on search button
        search_button.setOnClickListener {
            if (city == "City") {
                Toast.makeText(this, "Please Select City", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, Hotel_List::class.java)
                intent.putExtra("hotel_name", "$city") // ✅ Sahi tarika
                startActivity(intent)
            }
        }
        // Call function to fetch hotels from API
        fetchHotels(city)
        // Fetch hotel list for the selected city

    }

    // Function to fetch hotel data from API 1
    private fun fetchHotels(city: String) {
        val apiService = RetrofitClient.instance
        val token = "Bearer eyJhbGciOiJIUzUxMiJ9..." // Replace with your actual token

        // Show progress bar while fetching data
        progressBar.visibility = View.VISIBLE

        apiService.getHotels("api/hotels/search/city/delhi", token)
            .enqueue(object : Callback<List<Hotel>> {
                override fun onResponse(
                    call: Call<List<Hotel>>,
                    response: Response<List<Hotel>>
                ) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful && response.body() != null) {
                        val cities = response.body() ?: emptyList()
                        saveDataToSharedPreferences(cities) // Save hotel data
                        getHotelList(city)
                    } else {
                        Log.e("API_ERROR2", "Response failed: ${response.errorBody()?.string()}")
                        Toast.makeText(this@MainActivity, "Response failed", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<List<Hotel>>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Log.e("API_ERROR", "Error: ${t.message}")
                    Toast.makeText(this@MainActivity, "API call failed", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    // Function to save hotel list from API 1 to SharedPreferences
    private fun saveDataToSharedPreferences(hotelList: List<Hotel>) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(hotelList)
        editor.putString("hotel_list", json)
        editor.apply()
    }

    // Function to fetch hotel data from API 2 based on city
    private fun getHotelList(city: String) {
        val apiHotelService = RetrofitHotelClient.instance
        val token = "Bearer eyJhbGciOiJIUzUxMiJ9..." // Replace with actual token

        // Show progress bar while fetching data
        progressBar.visibility = View.VISIBLE

        apiHotelService.getSearchHotel("api/hotels/city/${city}", token)
            .enqueue(object : Callback<hotelListData> {
                override fun onResponse(
                    call: Call<hotelListData>,
                    response: Response<hotelListData>
                ) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful && response.body() != null) {
                        val cities = response.body()!!
                        Log.e("API_SUCCESS", "Hotel data loaded successfully: ${cities}")

                        // Save data to SharedPreferences
                        saveDataToSharedPreferences1(cities)
                    } else {
                        Log.e("API_ERROR2", "Response failed: ${response.errorBody()?.string()}")
                        Toast.makeText(this@MainActivity, "Response failed", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<hotelListData>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Log.e("API_ERROR", "Error: ${t.message}")
                    Toast.makeText(this@MainActivity, "API call failed", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    // Function to save hotel list from API 2 to SharedPreferences
    private fun saveDataToSharedPreferences1(hotelList: hotelListData) {
        val sharedPreferences = getSharedPreferences("HotelPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(hotelList)

        // Save hotel list to SharedPreferences
        editor.putString("hotel_list", json)
        editor.apply()

        // Show toast message on successful save
        Toast.makeText(this, "Data saved successfully", Toast.LENGTH_LONG).show()

        // Log saved hotel data
        Log.d("SharedPrefs", "Hotel data saved: $json")
    }
}
