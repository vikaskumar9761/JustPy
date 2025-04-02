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
import com.justpe.justpy1.city_search_hotel.HotelAdapter.RoomAdapter
import com.justpe.justpy1.city_search_hotel.HotelModel.DetailsModel
import com.justpe.justpy1.city_search_hotel.HotelModel.RoomDetailsResponse

class RoomFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var roomAdapter: RoomAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var getSharedPreferences: SharedPreferences
    private lateinit var noDataText: TextView  // 🔹 No Data Message View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_room, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        noDataText = view.findViewById(R.id.noDataText)  // 🔹 No Data Message

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        sharedPreferences = requireActivity().getSharedPreferences("hotelDetails", Context.MODE_PRIVATE)
        getSharedPreferences = requireActivity().getSharedPreferences("hotelDetails1", Context.MODE_PRIVATE)

        roomAdapter = RoomAdapter(emptyList())
        recyclerView.adapter = roomAdapter

        loadHotelDataFromSharedPreferences()
        loadHotelDataFromSharedPreferences1()
        return view
    }

    fun loadHotelDataFromSharedPreferences() {
        val gson = Gson()
        val json = sharedPreferences.getString("hotel_Details", "empty")

        if (json == "empty") {
            showNoDataMessage()  // 🔹 No Data Available दिखाओ
            return
        }

        try {
            val type = object : TypeToken<DetailsModel>() {}.type
            val hotelData: DetailsModel = gson.fromJson(json, type)
            val roomList: List<DetailsModel.RoomImage> = hotelData.RoomImageList ?: emptyList()

            if (roomList.isEmpty()) {
                showNoDataMessage()
            } else {
                hideNoDataMessage()
                roomAdapter.updateList(roomList)  // 🔹 Update RecyclerView
            }
        } catch (e: Exception) {
            Log.e("SharedPrefs", "JSON Parsing Error: ${e.message}")
            showNoDataMessage()
        }
    }

    fun loadHotelDataFromSharedPreferences1() {
        val gson = Gson()
        val json = getSharedPreferences.getString("hotel_details1", "empty")

        if (json == "empty") {
            showNoDataMessage()
            return
        }

        if (json != null) {
            try {
                val type = object : TypeToken<RoomDetailsResponse>() {}.type
                val hotelData: RoomDetailsResponse = gson.fromJson(json, type)

                val roomList1: List<RoomDetailsResponse.AvailableRoom> =
                    hotelData.hotellist?.flatMap { it.AvailableRoom ?: emptyList() }
                        ?: emptyList()

                if (roomList1.isEmpty()) {
                    showNoDataMessage()
                } else {
                    hideNoDataMessage()
                    roomAdapter.updateList1(roomList1)
                }
            } catch (e: Exception) {
                Log.e("SharedPrefs", "JSON Parsing Error: ${e.message}")
                showNoDataMessage()
            }
        } else {
            showNoDataMessage()
        }
    }



    private fun showNoDataMessage() {
        recyclerView.visibility = View.GONE
        noDataText.visibility = View.VISIBLE
    }

    private fun hideNoDataMessage() {
        recyclerView.visibility = View.VISIBLE
        noDataText.visibility = View.GONE
    }


}