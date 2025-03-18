package com.justpe.justpy1.searchHotel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.justpe.justpy1.R
import com.justpe.justpy1.searchHotel.HotelAdapter.HotelAdapter
import com.justpe.justpy1.searchHotel.dataApi.Hotel
import com.justpe.justpy1.searchHotel.retrofitClient.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class searchHotel : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var hotelAdapter: HotelAdapter
    private var hotelList: List<Hotel> = listOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.city_search_hotel)

        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        searchView.setQueryHint("Enter City/Location/Hotel Name")

        fetchHotels()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Check karo ki adapter null to nahi hai
                if (::hotelAdapter.isInitialized) {
                    hotelAdapter.filterList(newText.orEmpty())
                }
                return true
            }
        })
    }


    private fun fetchHotels() {
        val apiService = RetrofitClient.instance
        val token = "Bearer eyJhbGciOiJIUzUxMiJ9..."

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility=View.GONE
        progressBar.visibility=View.VISIBLE


        apiService.getHotels("api/hotels/search/city/delhi", token).enqueue(object : Callback<List<Hotel>> {
            override fun onResponse(call: Call<List<Hotel>>, response: Response<List<Hotel>>) {

                progressBar.visibility=View.VISIBLE
                progressBar.visibility=View.GONE

                if (response.isSuccessful && response.body() != null) {
                    hotelList = response.body()!!
                    hotelAdapter = HotelAdapter(hotelList,this@searchHotel)
                    recyclerView.adapter = hotelAdapter

                } else {
                    Toast.makeText(this@searchHotel, "Response failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Hotel>>, t: Throwable) {
                progressBar.visibility=View.GONE
                Log.e("API_ERROR", "Error: ${t.message}")
                Toast.makeText(this@searchHotel, "API call failed", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
