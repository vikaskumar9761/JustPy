package com.justpe.justpy1.HotelDetailScreen.FragmentScreen

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.justpe.justpy1.R
import com.justpe.justpy1.city_search_hotel.HotelAdapter.RoomAdapter
import com.justpe.justpy1.city_search_hotel.HotelModel.*
import com.justpe.justpy1.city_search_hotel.retrofitClient.RetrofitRoomClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RoomFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var roomAdapter: RoomAdapter
    private lateinit var tvTotalAmount: TextView
    private lateinit var noDataText: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesDate: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_room, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        tvTotalAmount = view.findViewById(R.id.tvAmaount)
        noDataText = view.findViewById(R.id.noDataText)

        sharedPreferences = requireContext().getSharedPreferences("hotelDetails", Context.MODE_PRIVATE)
        sharedPreferencesDate = requireContext().getSharedPreferences("datePic", MODE_PRIVATE)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ✅ Pass Callback for selected room price
        roomAdapter = RoomAdapter { selectedPrice ->
            tvTotalAmount.text = "Pay $selectedPrice USD"?:"0"
        }
        recyclerView.adapter = roomAdapter

        val emtCommonID = requireActivity().intent.getStringExtra("EMTCommonID") ?: ""
        fetchHotelDetails(emtCommonID)

        return view
    }

    private fun fetchHotelDetails(emtCommonID: String) {
        val adultData = loadRoomDetails()
        val dates = loadAndSetDates()

        val requestBody = HotelRequestBody(
            checkInDate = dates.first ?: "2025-10-08",
            checkOutDate = dates.second ?: "2025-10-12",
            hotelId = emtCommonID,
            currency = "USD",
            nationality = "AE",
            rooms = listOf(
                HotelRequestBody.RoomRequest(
                    adultData.adults, adultData.children, adultData.childAges
                )
            )
        )

        RetrofitRoomClient.instance.getHotelDetails(requestBody)
            .enqueue(object : Callback<RoomDetailsResponse> {
                override fun onResponse(call: Call<RoomDetailsResponse>, response: Response<RoomDetailsResponse>) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data != null) {
                            val roomList = data.hotellist?.flatMap { it.AvailableRoom ?: emptyList() } ?: emptyList()

                            if (roomList.isEmpty()) {
                                showNoDataMessage()
                            } else {
                                hideNoDataMessage()
                                roomAdapter.updateList1(roomList)
                                // 👇 Ye line ab zaroori nahi because adapter callback hi handle kar raha
                                // tvTotalAmount.text = "Pay ${roomList.firstOrNull()?.Price?.RoomPrice ?: "0"} USD"
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<RoomDetailsResponse>, t: Throwable) {
                    Log.e("API_FAILURE", "Error: ${t.message}")
                }
            })
    }

    private fun loadRoomDetails(): UserDetailsModel {
        val sharedPreferences = requireContext().getSharedPreferences("RoomDetails", MODE_PRIVATE)
        val adults = sharedPreferences.getInt("adults", 1)
        val children = sharedPreferences.getInt("children", 0)
        val childAgesJson = sharedPreferences.getString("child_ages", "[]")
        val childAges = Gson().fromJson(childAgesJson, Array<Int>::class.java)?.toList() ?: emptyList()
        return UserDetailsModel(1, adults, children, childAges)
    }

    private fun loadAndSetDates(): Pair<String?, String?> {
        return Pair(
            sharedPreferencesDate.getString("check_in_date", "2025-10-08"),
            sharedPreferencesDate.getString("check_out_date", "2025-10-12")
        )
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
