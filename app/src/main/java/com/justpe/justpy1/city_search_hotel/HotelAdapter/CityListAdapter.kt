package com.justpe.justpy1.city_search_hotel.HotelAdapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.justpe.justpy1.R
import com.justpe.justpy1.city_search_hotel.HotelModel.CityListModel
import com.justpe.justpy1.MainActivity

// RecyclerView Adapter for displaying a list of hotels
class CityListAdapter(
    private var hotelList: MutableList<CityListModel>, // Original hotel list
    private val context: Context // Context to handle click events
) : RecyclerView.Adapter<CityListAdapter.HotelViewHolder>() {

    private var filteredList: List<CityListModel> = hotelList // Filtered list for search functionality

    // Creates ViewHolder for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
        return HotelViewHolder(view, context)
    }

    // Binds data to the ViewHolder at the given position
    override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {
        val hotel = filteredList[position]
        holder.bind(hotel) // Call bind function to set data
    }

    // Returns the total number of items in the filtered list
    override fun getItemCount(): Int = filteredList.size

    // Updates the list when search results change
    fun updateList(newList: List<CityListModel>) {
        hotelList.clear()
        hotelList.addAll(newList)
        notifyDataSetChanged() // Refresh RecyclerView
    }

    // ViewHolder class for each item in RecyclerView
    class HotelViewHolder(itemView: View, private val context: Context)
        : RecyclerView.ViewHolder(itemView) {

        private val cityName: TextView = itemView.findViewById(R.id.cityName) // City Name TextView
        private val countryName: TextView = itemView.findViewById(R.id.countryName) // Country Name TextView

        // Binds hotel data to the ViewHolder
        fun bind(hotel: CityListModel) {
            cityName.text = hotel.City // Set city name
            countryName.text = hotel.CountryName // Set country name

            // Set click listener for item selection
            itemView.setOnClickListener {
                val intent = Intent(context, MainActivity::class.java).apply {
                    putExtra("city", hotel.City) // Pass city data to MainActivity
                    putExtra("country", hotel.CountryName) // Pass country data to MainActivity
                }

                context.startActivity(intent) // Open MainActivity with selected hotel data

                (context as Activity).finish()
            }
        }
    }
}
