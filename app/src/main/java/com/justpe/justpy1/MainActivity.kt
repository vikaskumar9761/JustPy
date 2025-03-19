package com.justpe.justpy1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.justpe.justpy1.searchHotel.HotelAdapter.HotelAdapter
import com.justpe.justpy1.searchHotel.dataApi.Hotel
import com.justpe.justpy1.searchHotel.retrofitClient.RetrofitClient
import com.justpe.justpy1.searchHotel.searchHotel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var btnNext: LinearLayout
    private lateinit var progressBar: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hotel_main)
        btnNext = findViewById(R.id.selectCity)
        progressBar = findViewById(R.id.progressBar)
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        progressBar.visibility = View.GONE
//call the function
        fetchHotels()

        //get the item city or country from recyclerView
        val city = intent.getStringExtra("city")?:"City"
        val country = intent.getStringExtra("country")?:"Country"

        findViewById<TextView>(R.id.cityTextView).text=city
        findViewById<TextView>(R.id.countryTextView).text=country


        // Next Activity Open
        btnNext.setOnClickListener {
            startActivity(Intent(this, searchHotel::class.java))

        }
    }
    private fun fetchHotels() {
        val apiService = RetrofitClient.instance
        val token = "Bearer eyJhbGciOiJIUzUxMiJ9..."

        progressBar.visibility = View.VISIBLE

        apiService.getHotels("api/hotels/search/city/delhi", token).enqueue(object : Callback<List<Hotel>> {
            override fun onResponse(call: Call<List<Hotel>>, response: Response<List<Hotel>>) {

                progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body() != null) {

                    val cities = response.body() ?: emptyList()
                    saveDataToSharedPreferences(cities)

                } else {

                    Log.e("API_ERROR2", "Response failed: ${response.errorBody()?.string()}")
                    Toast.makeText(this@MainActivity, "Response failed", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<List<Hotel>>, t: Throwable) {
                progressBar.visibility=View.GONE
                Log.e("API_ERROR", "Error: ${t.message}")
                Toast.makeText(this@MainActivity, "API call failed", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun saveDataToSharedPreferences(hotelList: List<Hotel>) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(hotelList)
        editor.putString("hotel_list", json)
        editor.apply()
    }
}
