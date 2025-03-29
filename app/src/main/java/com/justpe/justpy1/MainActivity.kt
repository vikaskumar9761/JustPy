package com.justpe.justpy1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.justpe.justpy1.Hotel_List_Screen.Hotel_List
import com.justpe.justpy1.city_search_hotel.HotelModel.CityListModel
import com.justpe.justpy1.city_search_hotel.HotelModel.hotelListModel
import com.justpe.justpy1.city_search_hotel.retrofitClient.RetrofitCityClient
import com.justpe.justpy1.city_search_hotel.retrofitClient.RetrofitHotelClient
import com.justpe.justpy1.HotelCityScreen.SearchHotel
import com.justpe.justpy1.city_search_hotel.HotelModel.RoomDetailsModel
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

        binding.Adults.setOnClickListener {
            showBottomSheetDialog()
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

    //data fetchHotels from api
    private fun fetchHotels(city: String) {
        val apiService = RetrofitCityClient.instance
        val token = "Bearer eyJhbGciOiJIUzUxMiJ9..."

        binding.progressBar.visibility = View.VISIBLE

        apiService.getHotels("api/hotels/search/city/delhi", token)
            .enqueue(object : Callback<List<CityListModel>> {
                override fun onResponse(
                    call: Call<List<CityListModel>>,
                    response: Response<List<CityListModel>>
                ) {
                    binding.progressBar.visibility = View.GONE
                    if (response.isSuccessful && response.body() != null) {
                        val cities = response.body() ?: emptyList()
                        saveDataToSharedPreferences(cities)
                        getHotelList(city)
                        binding.Adults.setOnClickListener {
                            showBottomSheetDialog()
                        }
                    } else {
                        Log.e("API_ERROR2", "Response failed: ${response.errorBody()?.string()}")
                        Toast.makeText(this@MainActivity, "Response failed", Toast.LENGTH_SHORT)
                            .show()

                    }
                }

                override fun onFailure(call: Call<List<CityListModel>>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    Log.e("API_ERROR", "Error: ${t.message}")
                    Toast.makeText(this@MainActivity, "API call failed", Toast.LENGTH_SHORT).show()
                }
            })
    }

    //save data from fetchHotels
    private fun saveDataToSharedPreferences(hotelList: List<CityListModel>) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(hotelList)
        editor.putString("hotel_list", json)
        editor.apply()
    }

    //data getHoteList from api
    private fun getHotelList(city: String) {
        val apiHotelService = RetrofitHotelClient.instance
        val token = "Bearer eyJhbGciOiJIUzUxMiJ9..."

        binding.progressBar.visibility = View.VISIBLE

        apiHotelService.getSearchHotel("api/hotels/city/$city", token)
            .enqueue(object : Callback<hotelListModel> {
                override fun onResponse(
                    call: Call<hotelListModel>,
                    response: Response<hotelListModel>
                ) {
                    binding.progressBar.visibility = View.GONE
                    if (response.isSuccessful && response.body() != null) {
                        val cities = response.body()!!
                        Log.e("API_SUCCESS", "Hotel data loaded successfully: $cities")
                        saveDataToSharedPreferences1(cities)
                    } else {
                        Log.e("API_ERROR2", "Response failed: ${response.errorBody()?.string()}")
                        Toast.makeText(this@MainActivity, "Response failed", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<hotelListModel>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    Log.e("API_ERROR", "Error: ${t.message}")
                    Toast.makeText(this@MainActivity, "API call failed", Toast.LENGTH_SHORT).show()
                }
            })
    }

    //data save getHotelList
    private fun saveDataToSharedPreferences1(hotelList: hotelListModel) {
        val sharedPreferences = getSharedPreferences("HotelPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(hotelList)
        editor.putString("hotel_list", json)
        editor.apply()
        Log.d("SharedPrefs", "Hotel data saved: $json")
    }


    //show bottom sheets dialog box
    private fun showBottomSheetDialog() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_room_guest, null)

        val btnClose: ImageView = view.findViewById(R.id.btnClose)
        val btnDone: Button = view.findViewById(R.id.btnDone)

        val spinnerRooms: Spinner = view.findViewById(R.id.spinnerRooms)
        val spinnerAdults: Spinner = view.findViewById(R.id.spinnerAdults)
        val spinnerChildren: Spinner = view.findViewById(R.id.spinnerChildren)
        val childAgeLayout: LinearLayout = view.findViewById(R.id.childAgeLayout)

        val roomOptions = arrayOf("1", "2", "3", "4", "5")
        val adultOptions = arrayOf("1", "2", "3", "4", "5")
        val childOptions = arrayOf("0", "1", "2", "3")

        spinnerRooms.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roomOptions)
        spinnerAdults.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, adultOptions)
        spinnerChildren.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, childOptions)

        val (savedRooms, savedAdults, savedChildren, savedChildAges) = loadRoomDetails()
        spinnerRooms.setSelection(savedRooms - 1)
        spinnerAdults.setSelection(savedAdults - 1)
        spinnerChildren.setSelection(savedChildren)



        fun updateChildAgeFields(count: Int) {
            childAgeLayout.removeAllViews()
            if (count > 0) childAgeLayout.visibility = View.VISIBLE else childAgeLayout.visibility = View.GONE
            val ageOptions = (1..18).map { it.toString() }
            for (i in 0 until count) {
                val spinner = Spinner(this)
                spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ageOptions)
                if (i < savedChildAges.size) spinner.setSelection(savedChildAges[i] - 1)
                childAgeLayout.addView(spinner)
            }
        }
        updateChildAgeFields(savedChildren)
        spinnerChildren.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateChildAgeFields(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        btnClose.setOnClickListener { dialog.dismiss() }
        btnDone.setOnClickListener {
            val selectedRooms = spinnerRooms.selectedItem.toString().toInt()
            val selectedAdults = spinnerAdults.selectedItem.toString().toInt()
            val selectedChildren = spinnerChildren.selectedItem.toString().toInt()

            val childAges = mutableListOf<Int>()
            for (i in 0 until selectedChildren) {
                val spinner = childAgeLayout.getChildAt(i) as Spinner
                childAges.add(spinner.selectedItem.toString().toInt())
            }

            saveRoomDetails(selectedRooms, selectedAdults, selectedChildren, childAges)
            Toast.makeText(this, "Saved Successfully!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialog.setContentView(view)
        dialog.show()
    }


    //load data from room details
    private fun loadRoomDetails(): RoomDetailsModel {
        val sharedPreferences = getSharedPreferences("RoomDetails", MODE_PRIVATE)

        val rooms = sharedPreferences.getInt("rooms", 1)
        val adults = sharedPreferences.getInt("adults", 1)
        val children = sharedPreferences.getInt("children", 0)

        val childAgesJson = sharedPreferences.getString("child_ages", "[]")
        val childAges = Gson().fromJson(childAgesJson, Array<Int>::class.java)?.toList() ?: emptyList()

        return RoomDetailsModel(rooms, adults, children, childAges)
    }

    //load data save from roomdetails
    private fun saveRoomDetails(rooms: Int, adults: Int, children: Int, childAges: List<Int>) {
        val sharedPreferences = getSharedPreferences("RoomDetails", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("rooms", rooms)
        editor.putInt("adults", adults)
        editor.putInt("children", children)

        val gson = Gson()
        val childAgesJson = gson.toJson(childAges)
        editor.putString("child_ages", childAgesJson)

        editor.apply()
    }


}
