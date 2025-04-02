package com.justpe.justpy1.HotelDetailScreen.FragmentScreen
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.common.reflect.TypeToken
import com.justpe.justpy1.R
import com.justpe.justpy1.city_search_hotel.HotelAdapter.OverviewAdaptor
import com.justpe.justpy1.city_search_hotel.HotelModel.DetailsModel
import com.justpe.justpy1.city_search_hotel.HotelModel.RoomDetailsResponse

class OverviewFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var roomAdapter: OverviewAdaptor

    private lateinit var getSharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_overview, container, false)
        recyclerView = view.findViewById(R.id.rvOverview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        getSharedPreferences = requireActivity().getSharedPreferences("hotelDetails1", Context.MODE_PRIVATE)
        sharedPreferences = requireActivity().getSharedPreferences("hotelDetails", Context.MODE_PRIVATE)
        roomAdapter = OverviewAdaptor(emptyList())
        recyclerView.adapter = roomAdapter
        var tvImageCount=view.findViewById<TextView>(R.id.tvTotalImage)
        loadHotelDataFromSharedPreferences(tvImageCount)
       // loadHotelDataFromSharedPreferences1()
        return view
    }

    fun loadHotelDataFromSharedPreferences(tvImageCount: TextView) {
        val gson = Gson()
        val json = sharedPreferences.getString("hotel_Details", null)

        if (json != null) {
            try {
                val type = object : TypeToken<DetailsModel>() {}.type
                val hotelData: DetailsModel = gson.fromJson(json, type)
                val roomList: List<DetailsModel.HotelImageDetail> = hotelData.HotelImagesDetail ?: emptyList()
                tvImageCount.text = "Total Images: ${roomList.size}"
                Log.d("SharedPrefs", "Loaded ${roomList.size} rooms from SharedPreferences")
                roomAdapter.updateList(roomList)

            } catch (e: Exception) {
               // tvImageCount.text = "Error Loading Images"
                Log.e("SharedPrefs", "JSON Parsing Error: ${e.message}")
            }
        } else {
           // tvImageCount.text = "No Images Available"
            Log.e("SharedPrefs", "No hotel data found in SharedPreferences")
        }
    }



}
