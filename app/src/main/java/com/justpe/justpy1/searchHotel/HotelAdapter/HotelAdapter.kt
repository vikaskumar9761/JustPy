package com.justpe.justpy1.searchHotel.HotelAdapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.justpe.justpy1.R
import com.justpe.justpy1.searchHotel.dataApi.Hotel
import com.justpe.justpy1.MainActivity

class HotelAdapter(
    private var hotelList: List<Hotel>,
    private val context: Context // Context click Listener
) : RecyclerView.Adapter<HotelAdapter.HotelViewHolder>() {

    private var filteredList: List<Hotel> = hotelList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
        return HotelViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {
        val hotel = filteredList[position]
        holder.bind(hotel)
    }

    override fun getItemCount(): Int = filteredList.size

    fun filterList(query: String) {
        filteredList = if (query.isEmpty()) {
            hotelList
        } else {
            hotelList.filter { it.City.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }

    class HotelViewHolder(itemView: View, private val context: Context)
        : RecyclerView.ViewHolder(itemView) {

        private val cityName: TextView = itemView.findViewById(R.id.cityName)
        private val countryName:TextView=itemView.findViewById(R.id.countryName)

        fun bind(hotel: Hotel) {
            cityName.text = hotel.City
            countryName.text=hotel.CountryName

            // **Click Listener item**
            itemView.setOnClickListener {
                val intent = Intent(context, MainActivity::class.java).apply {
                    putExtra("city", hotel.City)
                    putExtra("country", hotel.CountryName)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }

                context.startActivity(intent)

            }

        }
    }
}
