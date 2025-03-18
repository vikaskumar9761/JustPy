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
                }

                context.startActivity(intent)

            }

        }
    }
}
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.justpe.apicall.City
//import com.justpe.apicall.R
//
//class CityAdapter(private val cityList: List<City>) :
//    RecyclerView.Adapter<CityAdapter.CityViewHolder>() {
//
//    class CityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val cityName: TextView = view.findViewById(R.id.cityName)
//        val countryName: TextView = view.findViewById(R.id.countryName)
//        val countryCode: TextView = view.findViewById(R.id.countryCode)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_city, parent, false)
//        return CityViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
//        val city = cityList[position]
//        holder.cityName.text = city.City
//        holder.countryName.text = city.CountryName
//        holder.countryCode.text = "Code: ${city.country}"
//    }
//
//    override fun getItemCount(): Int = cityList.size
//}
//
