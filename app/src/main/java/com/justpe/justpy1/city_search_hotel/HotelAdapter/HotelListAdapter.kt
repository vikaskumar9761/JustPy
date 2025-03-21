package com.justpe.justpy1.city_search_hotel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.justpe.justpy1.R
import com.justpe.justpy1.city_search_hotel.dataApi.hotelListData

// Adapter for displaying a list of hotels in a RecyclerView
class HotelListAdapter(
    private var hotelList: List<hotelListData.HotelData> // List of hotels to display
) : RecyclerView.Adapter<HotelListAdapter.HotelViewHolder>() {

    // Creates ViewHolder for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hotel, parent, false)
        return HotelViewHolder(view)
    }

    // Binds hotel data to the ViewHolder at the given position
    override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {
        val hotel = hotelList[position]
        holder.bind(hotel) // Call bind function to set data
    }

    // Returns the total number of hotels in the list
    override fun getItemCount(): Int = hotelList.size

    // Updates the hotel list when new data is available
    fun updateList(newList: List<hotelListData.HotelData>) {
        hotelList = newList  // Replace the old list with the new one
        notifyDataSetChanged() // Refresh the RecyclerView
    }

    // ViewHolder class for each hotel item in the RecyclerView
    class HotelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val hotelImage: ImageView = itemView.findViewById(R.id.viewPagerImages) // Hotel ImageView
        private val hotelName: TextView = itemView.findViewById(R.id.tvHotelName) // Hotel Name TextView
        private val hotelLocation: TextView = itemView.findViewById(R.id.tvLocation) // Hotel Location TextView
        private val hotelAddress: TextView = itemView.findViewById(R.id.tvAddress) // Hotel Address TextView

        // Binds hotel data to the ViewHolder
        fun bind(hotel: hotelListData.HotelData) {
            hotelName.text = hotel.hotelName // Set hotel name
            hotelLocation.text = hotel.Location ?: "Location not available" // Set location with fallback
            hotelAddress.text = hotel.address // Set address

            // Load hotel image using Glide
            Glide.with(itemView.context)
                .load(hotel.ImageThumbUrl) // Load image URL
                .placeholder(R.drawable.loader_img) // Placeholder while loading
                .error(R.drawable.img_alert_triangle) // Default image if loading fails
                .into(hotelImage) // Load into ImageView
        }
    }
}
