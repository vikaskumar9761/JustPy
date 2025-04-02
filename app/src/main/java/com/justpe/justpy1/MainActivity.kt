package com.justpe.justpy1

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.justpe.justpy1.city_search_hotel.HotelModel.UserDetailsModel
import com.justpe.justpy1.databinding.DialogRoomGuestBinding
import com.justpe.justpy1.databinding.HotelMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: HotelMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesHotel: SharedPreferences
    private lateinit var sharedPreferencesDate: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HotelMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("citySearch", MODE_PRIVATE)
        sharedPreferencesHotel = getSharedPreferences("hotelSearch", MODE_PRIVATE)
        sharedPreferencesDate = getSharedPreferences("datePic", MODE_PRIVATE)

        val savedCheckIn = sharedPreferencesDate.getString("check_in_date", "Select Date") ?: "Select Date"
        val savedCheckOut = sharedPreferencesDate.getString("check_out_date", "Select Date") ?: "Select Date"
        binding.tvCheckIn.text = savedCheckIn
        binding.tvCheckOut.text = savedCheckOut

        binding.progressBar.visibility = View.GONE
        binding.btnCheckIn.setOnClickListener { showDatePickerDialog(true) }
        binding.btnCheckOut.setOnClickListener { showDatePickerDialog(false) }

        val city = intent.getStringExtra("city") ?: "City"
        val country = intent.getStringExtra("country") ?: "Country"

        binding.cityTextView.text = city
        binding.countryTextView.text = country
        binding.selectCity.setOnClickListener { startActivity(Intent(this, SearchHotel::class.java)) }
        binding.Adults.setOnClickListener { showBottomSheetDialog() }
        binding.searchButton.setOnClickListener {
            if (isFormValid()) {
                val intent = Intent(this, Hotel_List::class.java)
                intent.putExtra("hotel_name", city)
                startActivity(intent)
            }
        }
        fetchHotels(city)
    }

    private fun isFormValid(): Boolean {
        val city = binding.cityTextView.text.toString()
        val savedCheckIn = sharedPreferencesDate.getString("check_in_date", "Select Date") ?: "Select Date"
        val savedCheckOut = sharedPreferencesDate.getString("check_out_date", "Select Date") ?: "Select Date"
        val Adults=binding.tvAdults.text
        if (city == "City") {
            Toast.makeText(this, "Please Select a City", Toast.LENGTH_SHORT).show()
            return false
        }
        if (savedCheckIn == "Select Date") {
            Toast.makeText(this, "Please Select Check-in Date", Toast.LENGTH_SHORT).show()
            return false
        }
        if (savedCheckOut == "Select Date") {
            Toast.makeText(this, "Please Select Check-out Date", Toast.LENGTH_SHORT).show()
            return false
        }
        if (Adults=="0 Adults, 0 Room") {
            Toast.makeText(this, "Please select Rooms and Adults", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    //data fetchHotels from api
    private fun fetchHotels(city: String) {
        val savedData = sharedPreferences.getString("hotel_list", null)
        if (savedData != null) {
            // SharedPreferences load from old
            val gson = Gson()
            val savedList = gson.fromJson(savedData, Array<CityListModel>::class.java).toList()
            if (savedList.isNotEmpty()) {
                Log.d("SharedPrefs", "Loading saved hotel list from SharedPreferences")
                getHotelList(city)
                return
            }
        }
        //when data is not and run this api
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
        val binding = DialogRoomGuestBinding.inflate(layoutInflater)  // ViewBinding use kiya
        val roomOptions = arrayOf("1", "2", "3", "4", "5")
        val adultOptions = arrayOf("1", "2", "3", "4", "5")
        val childOptions = arrayOf("0", "1", "2", "3")
        binding.spinnerRooms.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roomOptions)
        binding.spinnerAdults.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, adultOptions)
        binding.spinnerChildren.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, childOptions)
        val (savedRooms, savedAdults, savedChildren, savedChildAges) = loadRoomDetails()
        binding.spinnerRooms.setSelection(savedRooms - 1)
        binding.spinnerAdults.setSelection(savedAdults - 1)
        binding.spinnerChildren.setSelection(savedChildren)
        fun updateChildAgeFields(count: Int) {
            binding.childAgeLayout.removeAllViews()
            binding.childAgeLayout.visibility = if (count > 0) View.VISIBLE else View.GONE

            val ageOptions = (1..18).map { it.toString() }
            for (i in 0 until count) {
                val spinner = Spinner(this)
                spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ageOptions)
                if (i < savedChildAges.size) spinner.setSelection(savedChildAges[i] - 1)
                binding.childAgeLayout.addView(spinner)
            }
        }
        updateChildAgeFields(savedChildren)
        binding.spinnerChildren.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateChildAgeFields(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        binding.btnClose.setOnClickListener { dialog.dismiss() }
        binding.btnDone.setOnClickListener {
            val selectedRooms = binding.spinnerRooms.selectedItem.toString().toInt()
            val selectedAdults = binding.spinnerAdults.selectedItem.toString().toInt()
            val selectedChildren = binding.spinnerChildren.selectedItem.toString().toInt()
            val childAges = mutableListOf<Int>()
            for (i in 0 until selectedChildren) {
                val spinner = binding.childAgeLayout.getChildAt(i) as Spinner
                childAges.add(spinner.selectedItem.toString().toInt())
            }
            // 🟢 Update UI in MainActivity after dismissing dialog
            this@MainActivity.binding.tvAdults.text = " Adults $selectedAdults, Rooms $selectedRooms "
            saveRoomDetails(selectedRooms, selectedAdults, selectedChildren, childAges)
            Toast.makeText(this, "Saved Successfully!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialog.setContentView(binding.root)
        dialog.show()
    }
    //load data from room details
    private fun loadRoomDetails(): UserDetailsModel {
        val sharedPreferences = getSharedPreferences("RoomDetails", MODE_PRIVATE)
        val rooms = sharedPreferences.getInt("rooms", 1)
        val adults = sharedPreferences.getInt("adults", 1)
        val children = sharedPreferences.getInt("children", 0)
        val childAgesJson = sharedPreferences.getString("child_ages", "[]")
        val childAges = Gson().fromJson(childAgesJson, Array<Int>::class.java)?.toList() ?: emptyList()
        return UserDetailsModel(rooms, adults, children,childAges)
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

    private fun showDatePickerDialog(isCheckIn: Boolean) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
            if (isCheckIn) {
                binding.tvCheckIn.text = selectedDate
                saveCheckInCheckOutDates(selectedDate, binding.tvCheckOut.text.toString())
            } else {
                binding.tvCheckOut.text = selectedDate
                saveCheckInCheckOutDates(binding.tvCheckIn.text.toString(), selectedDate)
            }
        }, year, month, day)

        datePickerDialog.show()
    }
    private fun saveCheckInCheckOutDates(checkIn: String, checkOut: String) {
        sharedPreferencesDate=getSharedPreferences("datePic", MODE_PRIVATE)
        val editor = sharedPreferencesDate.edit()
        editor.putString("check_in_date", checkIn)
        editor.putString("check_out_date", checkOut)
        editor.apply()
    }

}
